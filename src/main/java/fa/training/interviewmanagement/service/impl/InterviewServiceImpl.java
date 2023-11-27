package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.entities.*;
import fa.training.interviewmanagement.enums.EResult;
import fa.training.interviewmanagement.enums.EStatus;
import fa.training.interviewmanagement.repository.*;
import fa.training.interviewmanagement.service.EmailService;
import fa.training.interviewmanagement.service.InterviewService;
import fa.training.interviewmanagement.web.request.InterviewRequest;
import fa.training.interviewmanagement.web.request.InterviewSearch;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private final InterviewerScheduleRepository interviewerScheduleRepository;
    private final ResultInterviewRepository resultInterviewRepository;
    private final InterviewScheduleRepository interviewRepository;
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;


    @Override
    public Page<InterviewSchedule> findAllInterviewSchedule(InterviewSearch interviewSearch) {
        Pageable pageable = PageRequest.of(interviewSearch.getPageNumber() - 1, interviewSearch.getPageSize());
        if (interviewSearch.getStatus() == null) {
            return interviewRepository.findAll(
                    interviewSearch.getParam(),
                    interviewSearch.getInterview(),
                    pageable
            );
        }
        return interviewRepository.findAll(
                interviewSearch.getParam(),
                interviewSearch.getInterview(),
                interviewSearch.getStatus(),
                pageable
        );
    }

    @Override
    @Transactional
    public InterviewSchedule saveInterviewSchedule(InterviewRequest interviewRequest) throws MessagingException {
        InterviewSchedule interviewSchedule = interviewRepository.save(getInterviewSchedule(new InterviewSchedule(), interviewRequest));
        emailService.sendMailInterviewScheduleToInterviewer(interviewSchedule.getInterviewer());
        emailService.sendMailInterviewScheduleToCandidate(interviewSchedule.getResultInterviews());
        return interviewSchedule;
    }

    @Override
    @Transactional
    public InterviewSchedule updateInterviewSchedule(InterviewSchedule interviewSchedule, InterviewRequest interviewRequest)
            throws MessagingException {
        CheckUpdate checkUpdate = checkUpdate(interviewSchedule, interviewRequest);
        interviewSchedule = interviewRepository.save(interviewSchedule);
        EStatus eStatus = interviewSchedule.getResultInterviews().getCandidate().getStatus();
        sendMailUpdate(checkUpdate, eStatus, interviewSchedule);
        return interviewSchedule;
    }

    @Override
    @Transactional
    public InterviewSchedule saveInterviewSchedule(InterviewSchedule interviewSchedule) {
        return interviewRepository.save(interviewSchedule);
    }

    @Override
    @Transactional
    public void deleteInterviewScheduleById(Long id) throws MessagingException {
        InterviewSchedule interviewSchedule = interviewRepository.findById(id).orElseThrow();
        Candidate candidate = interviewSchedule.getResultInterviews().getCandidate();
        interviewerScheduleRepository.deleteBySchedule_Id(id);
        resultInterviewRepository.deleteByInterviewSchedule_Id(id);
        interviewRepository.deleteByInterviewScheduleID(id);
        if (candidate.getStatus().equals(EStatus.WAITING_FOR_INTERVIEW)) {
            candidate.setStatus(EStatus.OPEN);
            candidateRepository.save(candidate);
        }
    }

    @Override
    public Optional<InterviewSchedule> findByInterviewScheduleById(Long id) {
        return interviewRepository.findById(id);
    }

    private InterviewSchedule getInterviewSchedule(InterviewSchedule interviewSchedule, InterviewRequest interviewRequest) {
        modelMapper.map(interviewRequest, interviewSchedule);
        Candidate candidate = candidateRepository.findById(interviewRequest.getCandidateId()).orElseThrow();
        if (candidate.getStatus().equals(EStatus.OPEN)) {
            candidate.setStatus(EStatus.WAITING_FOR_INTERVIEW);
        }
        Users recruiter = userRepository.findById(interviewRequest.getRecruiterId()).orElseThrow();
        interviewSchedule.setRecruiter(recruiter);
        List<Users> interviewers = userRepository.findAllById(interviewRequest.getInterviewId());
        List<InterviewerSchedule> interviewerSchedule = getInterviewerSchedules(interviewers, interviewSchedule);
        ResultInterview resultInterview = getResultInterview(candidate, interviewSchedule, interviewRequest.getNote());
        interviewSchedule.setInterviewer(interviewerSchedule);
        interviewSchedule.setResultInterviews(resultInterview);
        return interviewSchedule;
    }

    private List<InterviewerSchedule> getInterviewerSchedules(List<Users> interviewers, InterviewSchedule interviewSchedule) {
        return interviewers.stream().map(i ->
                InterviewerSchedule.builder()
                        .interviewer(i)
                        .schedule(interviewSchedule)
                        .build()
        ).toList();
    }

    private ResultInterview getResultInterview(Candidate candidate, InterviewSchedule interviewSchedule, String notes) {
        return ResultInterview.builder()
                .candidate(candidate)
                .note(notes)
                .result(EResult.NA)
                .interviewSchedule(interviewSchedule)
                .build();
    }

    private CheckUpdate checkUpdate(InterviewSchedule interviewSchedule, InterviewRequest interviewRequest) {
        CheckUpdate checkUpdate = new CheckUpdate();
        interviewSchedule.setStatus(interviewRequest.getStatus());
        interviewSchedule.setTitle(interviewRequest.getTitle());
        interviewSchedule.getResultInterviews().setNote(interviewRequest.getNote());
        if (!Objects.equals(interviewSchedule.getSchedule(), interviewRequest.getSchedule()) ||
                !Objects.equals(interviewSchedule.getMeeting(), interviewRequest.getMeeting())) {
            interviewSchedule.setLocation(interviewRequest.isLocation());
            interviewSchedule.setMeeting(interviewRequest.getMeeting());
            interviewSchedule.setSchedule(interviewRequest.getSchedule());
            checkUpdate.setCheckLocation(true);
        }

        List<Long> ids = new ArrayList<>();
        interviewSchedule.getInterviewer().forEach(x -> ids.add(x.getInterviewer().getId()));
        if (!Objects.equals(ids, interviewRequest.getInterviewId())) {
            setInterviewUpdate(ids, interviewRequest.getInterviewId(), checkUpdate);
            List<Users> interviewers = userRepository.findAllById(interviewRequest.getInterviewId());
            if (!checkUpdate.getInterviewsRemove().isEmpty()) {
                List<InterviewerSchedule> interviewerSchedules = interviewerScheduleRepository
                        .findInterviewerScheduleByInterviewer_IdInAndSchedule_Id(checkUpdate.getInterviewsRemove(),
                                interviewSchedule.getId());
                interviewerScheduleRepository.deleteAll(interviewerSchedules);
            }
            List<InterviewerSchedule> interviewerSchedule = new ArrayList<>();
            for (Users u : interviewers) {
                interviewerSchedule.add(InterviewerSchedule
                        .builder()
                        .interviewer(u)
                        .schedule(interviewSchedule)
                        .build());
            }
            checkUpdate.setCheckInterview(true);
            interviewSchedule.setInterviewer(interviewerSchedule);
        }
        return checkUpdate;
    }

    private void setInterviewUpdate(List<Long> interviewIdsOld, List<Long> interviewIdsNew, CheckUpdate checkUpdate) {
        Set<Long> common = new HashSet<>();
        for (Long i : interviewIdsOld) {
            for (Long l : interviewIdsNew) {
                if (Objects.equals(l, i)) {
                    common.add(l);
                }
            }
        }
        for (Long l : common) {
            interviewIdsNew.remove(l);
            interviewIdsOld.remove(l);
        }
        checkUpdate.setInterviewsRemove(interviewIdsOld);
        checkUpdate.setInterviewAdd(interviewIdsNew);
    }

    private void sendMailUpdate(CheckUpdate checkUpdate, EStatus eStatus, InterviewSchedule interviewSchedule)
            throws MessagingException {
        if (checkUpdate.isCheckInterview) {
            if (!checkUpdate.interviewAdd.isEmpty()) {
                List<Users> interviewerNew = userRepository.findAllById(checkUpdate.interviewAdd);
                emailService.sendMailUpdateInterviewScheduleToInterviewer(interviewerNew, interviewSchedule);
            }
            if (!checkUpdate.interviewsRemove.isEmpty()) {
                List<Users> interviewerOld = userRepository.findAllById(checkUpdate.interviewsRemove);
                emailService.sendMailUpdateInterviewScheduleToInterviewer(interviewerOld, interviewSchedule);
            }
        }
        if (checkUpdate.isCheckLocation && eStatus.equals(EStatus.WAITING_FOR_INTERVIEW)) {
            if (!checkUpdate.isCheckInterview) {
                emailService.sendMailUpdateInterviewScheduleToInterviewer(interviewSchedule.getInterviewer());
            }
            emailService.sendMailUpdateInterviewScheduleToCandidate(interviewSchedule.getResultInterviews());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CheckUpdate {
        private boolean isCheckLocation = false;
        private boolean isCheckInterview = false;
        private List<Long> interviewsRemove;
        private List<Long> interviewAdd;
    }

}

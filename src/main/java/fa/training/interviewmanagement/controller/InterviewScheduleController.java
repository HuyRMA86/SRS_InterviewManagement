package fa.training.interviewmanagement.controller;

import fa.training.interviewmanagement.entities.*;
import fa.training.interviewmanagement.enums.EResult;
import fa.training.interviewmanagement.enums.EStatus;
import fa.training.interviewmanagement.repository.CandidateRepository;
import fa.training.interviewmanagement.repository.UserRepository;

import fa.training.interviewmanagement.service.UserService;
import fa.training.interviewmanagement.utils.SessionUtil;
import fa.training.interviewmanagement.web.request.InterviewRequest;
import fa.training.interviewmanagement.web.request.InterviewSearch;
import fa.training.interviewmanagement.service.InterviewService;
import fa.training.interviewmanagement.web.request.ResultRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/interview")
@RequiredArgsConstructor
public class InterviewScheduleController {

    private final InterviewService interviewService;
    private final CandidateRepository candidateRepository;
    private final UserService userService;
    private final SessionUtil sessionUtil;
    private final UserRepository userRepository;
    private static final String USERS = "users";
    private static final String MESSAGE = "message";
    private static final String INTERVIEW_SCHEDULE = "interviewSchedule";

    @GetMapping
    public String interview(HttpServletRequest req,
                            @ModelAttribute InterviewSearch interviewSearchRep,
                            Model model) {
        sessionUtil.setTitle(req, "INTERVIEW SCHEDULE");
        InterviewSearch interviewSearch = checkInputSearch(interviewSearchRep);
        Long recruiterId = getRecruiterId();
        Page<InterviewSchedule> interviewSchedules = interviewService.findAllInterviewSchedule(interviewSearch);
        List<Users> users = userService.getUserByRoleInterviewAndRecruiter();
        int pageMax = interviewSchedules.getTotalPages();

        if (pageMax > 0) {
            List<Integer> integers = new ArrayList<>();
            for (int i = 0; i < pageMax; i++) {
                integers.add(i);
            }
            interviewSearch.setPageMaxNumber(integers);
        } else {
            interviewSearch.setMessage("InterviewSchedule don't found");
        }
        model.addAttribute(USERS, users);
        model.addAttribute("interviewSearch", interviewSearch);
        model.addAttribute("interviewSchedules", interviewSchedules);
        return "ui/InterviewSchedule/interview-schedule-list";
    }

    @GetMapping("/create")
    public String getCreateInterview(Model model,
                                     @ModelAttribute InterviewRequest interviewRequest
    ) {

        List<Candidate> candidates = candidateRepository.findAll();
        List<Users> users = userService.getUserByRoleInterviewAndRecruiter();
        model.addAttribute("candidates", candidates);
        model.addAttribute(USERS, users);
        return "ui/InterviewSchedule/interview-schedule-add";
    }

    @PostMapping("/create")
    public String postCreateInterview(RedirectAttributes ra,
                                      Model model,
                                      @ModelAttribute @Validated InterviewRequest interviewRequest,
                                      BindingResult bindingResult)
            throws MessagingException {
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute(MESSAGE, "Fail");
            return getCreateInterview(model,interviewRequest);
        }
        interviewService.saveInterviewSchedule(interviewRequest);
        ra.addFlashAttribute(MESSAGE, "add new interview schedule success");
        return "redirect:/interview/create";
    }

    @GetMapping("/{id}")
    public String getInterview(@PathVariable Long id, Model model) {
        InterviewSchedule interviewSchedule = interviewService.findByInterviewScheduleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interview Schedule don't found"));
        model.addAttribute(INTERVIEW_SCHEDULE, interviewSchedule);
        return "ui/InterviewSchedule/interview-schedule-detail";
    }

    @GetMapping("/result/{id}")
    public String getResult(@PathVariable Long id,
                            Model model) {
        InterviewSchedule interviewSchedule = interviewService.findByInterviewScheduleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interview Schedule don't found"));
        model.addAttribute(INTERVIEW_SCHEDULE, interviewSchedule);
        return "ui/InterviewSchedule/interview-schedule-result";
    }

    @PostMapping("/result/{id}")
    public String setResult(@PathVariable Long id,
                            Model model,
                            ResultRequest resultRequest) {

        InterviewSchedule interviewSchedule = interviewService.findByInterviewScheduleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interview Schedule don't found"));
        EStatus status = this.getStatus(resultRequest.getResult());
        interviewSchedule.setStatus(status);
        interviewSchedule.getResultInterviews().setResult(resultRequest.getResult());
        interviewSchedule.getResultInterviews().setNote(resultRequest.getNotes());
        interviewSchedule.getResultInterviews().getCandidate().setStatus(status);
        model.addAttribute(INTERVIEW_SCHEDULE, interviewService.saveInterviewSchedule(interviewSchedule));
        model.addAttribute(MESSAGE, "update result success");
        return "ui/InterviewSchedule/interview-schedule-result";
    }

    @GetMapping("/edit/{id}")
    public String getEditInterview(@PathVariable Long id,
                                   @ModelAttribute InterviewRequest interviewRequest,
                                   Model model) {
        InterviewSchedule interviewSchedule = interviewService.findByInterviewScheduleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interview Schedule don't found"));
        List<Users> users = userService.getUserByRoleInterviewAndRecruiter();
        List<Long> ids = interviewSchedule.getInterviewer().stream().map(x -> x.getInterviewer().getId()).toList();
        model.addAttribute(USERS, users);
        model.addAttribute("ids", ids);
        model.addAttribute(INTERVIEW_SCHEDULE, interviewSchedule);
        return "ui/InterviewSchedule/interview-schedule-edit";
    }

    @PostMapping("/edit/{id}")
    public String editInterview(@PathVariable long id,
                                Model model,
                                @ModelAttribute @Validated InterviewRequest interviewRequest,
                                BindingResult bindingResult) throws MessagingException {
        if (bindingResult.hasErrors()) {
            return getEditInterview(id, interviewRequest, model);
        }
        InterviewSchedule interviewSchedule = interviewService.findByInterviewScheduleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interview schedule don't found"));
        EResult result = interviewSchedule.getResultInterviews().getResult();
        if (result != EResult.PASS && result != EResult.FAIL) {
            interviewService.updateInterviewSchedule(interviewSchedule, interviewRequest);
            model.addAttribute(MESSAGE, "update success");
        } else {
            model.addAttribute(MESSAGE, "Can't update");
        }
        return "redirect:/interview/edit/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteInterview(@PathVariable Long id) throws MessagingException {
        interviewService.deleteInterviewScheduleById(id);
        return "redirect:/interview";
    }


    private InterviewSearch checkInputSearch(InterviewSearch interviewSearch) {
        if (interviewSearch.getPageNumber() == null) {
            interviewSearch.setPageNumber(1);
        }
        if (interviewSearch.getParam() == null || interviewSearch.getParam().isEmpty()) {
            interviewSearch.setParam("");
        }
        if (interviewSearch.getInterview() == null || interviewSearch.getInterview().isEmpty()) {
            interviewSearch.setInterview("");
        }
        return interviewSearch;
    }


    private EStatus getStatus(EResult result) {
        if (result == EResult.FAIL) {
            return EStatus.FAILED_INTERVIEW;
        }
        if (result == EResult.CANCEL) {
            return EStatus.CANCELED_INTERVIEW;
        }
        return EStatus.PASSED_INTERVIEW;
    }

    private Long getRecruiterId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users users = userRepository.findByAccount_Email(authentication.getName()).orElseThrow();
        return users.getId();
    }


}

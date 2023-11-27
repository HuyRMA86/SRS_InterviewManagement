package fa.training.interviewmanagement.scheduling;

import fa.training.interviewmanagement.entities.*;
import fa.training.interviewmanagement.enums.EStatus;
import fa.training.interviewmanagement.repository.InterviewScheduleRepository;
import fa.training.interviewmanagement.repository.JobRepository;
import fa.training.interviewmanagement.service.EmailService;
import fa.training.interviewmanagement.service.InterviewService;
import fa.training.interviewmanagement.service.JobService;
import lombok.Data;
import fa.training.interviewmanagement.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AutoUpdate {

    final private InterviewScheduleRepository interviewScheduleRepository;
    final private EmailService emailService;
    final private JobService jobService;
    final private UserService userService;

    @Scheduled(cron = "01 00 00 * * *")
    public void updateStatusInterviewerSchedule() {
        LocalDate localDate = LocalDate.now();
        List<InterviewSchedule> interviewSchedules = interviewScheduleRepository.findAllBySchedule(localDate);
        interviewSchedules.forEach(x -> {
            x.setStatus(EStatus.IN_PROGRESS);
            x.getResultInterviews().getCandidate().setStatus(EStatus.IN_PROGRESS);
        });
        interviewScheduleRepository.saveAll(interviewSchedules);
    }

    @Scheduled(cron = "01 00 00 * * *")
    public void updateStatusJob() {
        LocalDate localDate = LocalDate.now();
        List<Job> jobs = jobService.findJobByEndDate(localDate);

        jobs.forEach(job -> job.setStatus(EStatus.IN_PROGRESS));
        jobService.saveAllJob(jobs);
    }

    @Scheduled(cron = "01 00 08 * * *")
    public void sendMailRemind() throws MessagingException {
        LocalDate localDate = LocalDate.now();
        List<InterviewSchedule> interviewSchedules = interviewScheduleRepository.findAllBySchedule(localDate);
        Set<InterviewerSchedule> interviewers = new HashSet<>();
        interviewSchedules.forEach(x -> interviewers.addAll(x.getInterviewer()));
        emailService.sendMailInterviewScheduleToInterviewer(interviewers);
    }
}
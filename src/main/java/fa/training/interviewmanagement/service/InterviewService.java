package fa.training.interviewmanagement.service;

import fa.training.interviewmanagement.entities.InterviewSchedule;
import fa.training.interviewmanagement.web.request.InterviewRequest;
import fa.training.interviewmanagement.web.request.InterviewSearch;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface InterviewService {
    Page<InterviewSchedule> findAllInterviewSchedule(InterviewSearch interviewSearch);

    InterviewSchedule saveInterviewSchedule(InterviewRequest interviewRequest) throws MessagingException;

    InterviewSchedule updateInterviewSchedule(InterviewSchedule interviewSchedule, InterviewRequest interviewRequest) throws MessagingException;

    InterviewSchedule saveInterviewSchedule(InterviewSchedule interviewSchedule);

    void deleteInterviewScheduleById(Long id) throws MessagingException;

    Optional<InterviewSchedule> findByInterviewScheduleById(Long id);
}

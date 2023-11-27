package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entities.InterviewerSchedule;
import fa.training.interviewmanagement.entities.KeyInterviewerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterviewerScheduleRepository extends JpaRepository<InterviewerSchedule, KeyInterviewerSchedule> {
    List<InterviewerSchedule> findInterviewerScheduleByInterviewer_IdInAndSchedule_Id(List<Long> interviewId, Long interviewScheduleId);

    @Modifying
    @Query("DELETE InterviewerSchedule where schedule.id = ?1")
    void deleteBySchedule_Id(Long id);
}

package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entities.InterviewSchedule;
import fa.training.interviewmanagement.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {
    @Query("SELECT DISTINCT i FROM InterviewSchedule i " +
            "JOIN i.resultInterviews ri " +
            "JOIN i.interviewer iv " +
            "WHERE concat(i.title, ri.candidate.fullName) like %?1% " +
            "AND iv.interviewer.fullName like %?2% " )
    Page<InterviewSchedule> findAll(String param, String interviewer,  Pageable pageable);

    @Query("SELECT DISTINCT i FROM InterviewSchedule i " +
            "JOIN i.resultInterviews ri " +
            "JOIN i.interviewer iv " +
            "WHERE concat(i.title, ri.candidate.fullName) like %?1% " +
            "AND iv.interviewer.fullName like %?2% " +
            "AND i.status = ?3 ")
    Page<InterviewSchedule> findAll(String param, String interviewer, EStatus status, Pageable pageable);

    @Query("select i from InterviewSchedule i " +
            "where concat(i.schedule,'') like %?1% AND i.status = 'OPEN'")
    List<InterviewSchedule> findAllBySchedule(LocalDate localDate);

    @Modifying
    @Query("delete InterviewSchedule where id = ?1")
    void deleteByInterviewScheduleID(Long id);
}

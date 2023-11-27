package fa.training.interviewmanagement.repository;


import fa.training.interviewmanagement.entities.Job;
import fa.training.interviewmanagement.entities.Level;
import fa.training.interviewmanagement.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("from Job j WHERE j.title like %?1% and j.status = ?2")
    Page<Job> getAllJob(String jobTitle, EStatus status, Pageable pageable);

    @Query("from Job j WHERE j.title like %?1%")
    Page<Job> getAllJob(String jobTitle, Pageable pageable);


    List<Job> findJobByTitleAndLevel(String title, Level level);

    List<Job> findJobByEndDate(LocalDate localDate);


}

package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entities.Candidate;
import fa.training.interviewmanagement.entities.ResultInterview;
import fa.training.interviewmanagement.entities.Skill;
import fa.training.interviewmanagement.enums.EGender;
import fa.training.interviewmanagement.enums.EHighestLevel;
import fa.training.interviewmanagement.enums.EPosition;
import fa.training.interviewmanagement.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {


    @Query("select c from Candidate c where " +
            "c.fullName LIKE %?1% " +
            " AND c.status = ?2")
    Page<Candidate> findAll(String nameKeyword, EStatus status, Pageable pageable);

    @Query("select c from Candidate c where " +
            "c.fullName LIKE %?1%")
    Page<Candidate> findAll(String nameKeyword, Pageable pageable);

}

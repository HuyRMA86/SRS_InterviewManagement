package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entities.KeySkillCandidate;
import fa.training.interviewmanagement.entities.SkillCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillCandidateRepository extends JpaRepository<SkillCandidate, KeySkillCandidate> {
    void deleteByCandidate_Id(Long candidateId);
}

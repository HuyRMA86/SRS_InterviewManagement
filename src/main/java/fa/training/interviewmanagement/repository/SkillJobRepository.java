package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entities.KeySkillJob;
import fa.training.interviewmanagement.entities.SkillJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillJobRepository extends JpaRepository<SkillJob, KeySkillJob> {

    SkillJob findByJob_IdAndSkill_Id(Long jobId, Long skillId);
    List<SkillJob> findByJob_Id(Long id);
    void deleteAllByJob_Id(Long id);
}

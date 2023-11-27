package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}

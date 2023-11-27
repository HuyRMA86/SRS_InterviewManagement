package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entities.Level;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepository extends JpaRepository<Level, Long> {
}

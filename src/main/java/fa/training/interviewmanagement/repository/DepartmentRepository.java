package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}

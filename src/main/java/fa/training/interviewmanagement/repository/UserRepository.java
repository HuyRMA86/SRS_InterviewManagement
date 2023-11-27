package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entities.Users;
import fa.training.interviewmanagement.enums.ERole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    List<Users> findUsersByAccount_RoleIn(List<ERole> roles);
    List<Users> findUsersByAccount_Role(ERole roles);

    @Query("from Users u WHERE u.fullName like %?1% and u.account.role = ?2")
    Page<Users> getAllUser(String userName, ERole role, Pageable pageable);

    @Query("from Users u WHERE u.fullName like %?1%")
    Page<Users> getAllUser(String userName, Pageable pageable);

    Optional<Users> findByPhoneNumber(String phoneNumber);
    Optional<Users> findByAccount_Email(String email);
}

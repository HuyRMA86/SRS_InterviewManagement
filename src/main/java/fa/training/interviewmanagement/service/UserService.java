package fa.training.interviewmanagement.service;

import fa.training.interviewmanagement.entities.Users;
import fa.training.interviewmanagement.web.request.UserChangePassword;
import fa.training.interviewmanagement.web.request.UserRequest;
import fa.training.interviewmanagement.web.request.UserSave;
import fa.training.interviewmanagement.web.request.UserSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<Users> getUserByRoleInterviewAndRecruiter();
    List<Users> getUserByRoleManager();
    List<Users> getUserByRoleRecruiterAndManager();
    Page<Users> findAllUser(UserSearch userSearch);
    Users createUserByUserRequest(UserRequest userRequest);

    UserSave saveUser(Users users);
    UserSave updateUser(Users users);

    Users findUserByID(Long id);

    UserSave changePassword(UserChangePassword userChangePassword);


}

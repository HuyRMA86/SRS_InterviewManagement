package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.entities.Account;
import fa.training.interviewmanagement.entities.Department;
import fa.training.interviewmanagement.entities.Users;
import fa.training.interviewmanagement.enums.ERole;
import fa.training.interviewmanagement.repository.DepartmentRepository;
import fa.training.interviewmanagement.repository.UserRepository;
import fa.training.interviewmanagement.service.UserService;
import fa.training.interviewmanagement.web.request.UserChangePassword;
import fa.training.interviewmanagement.web.request.UserRequest;
import fa.training.interviewmanagement.web.request.UserSave;
import fa.training.interviewmanagement.web.request.UserSearch;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    @Override
    public List<Users> getUserByRoleInterviewAndRecruiter() {
        return userRepository.findUsersByAccount_RoleIn(List.of(ERole.ROLE_INTERVIEW, ERole.ROLE_RECRUITER));
    }

    @Override
    public List<Users> getUserByRoleManager() {
        return userRepository.findUsersByAccount_Role(ERole.ROLE_MANAGER);
    }

    @Override
    public List<Users> getUserByRoleRecruiterAndManager() {
        return userRepository.findUsersByAccount_RoleIn(List.of(ERole.ROLE_MANAGER, ERole.ROLE_RECRUITER));
    }

    @Override
    public Page<Users> findAllUser(UserSearch userSearch) {
        Pageable pageable = PageRequest.of(userSearch.getPageIndex() - 1, 4);
        Page<Users> users = null;
        if (userSearch.getRole() == null) {
            users = userRepository.getAllUser(userSearch.getNameSearch(), pageable);
        }else {
            users = userRepository.getAllUser(userSearch.getNameSearch(), userSearch.getRole(), pageable);
        }
        return users;
    }

    @Override
    public Users createUserByUserRequest(UserRequest userRequest) {
        Users users = modelMapper.map(userRequest, Users.class);
        Account account = modelMapper.map(userRequest, Account.class);
        account.setPassword(generatePassWord());
        account.setUser(users);
        account.setCheckPassword(false);
        Department department = departmentRepository.findById(userRequest.getDepartment()).orElseThrow();
        users.setDepartment(department);
        users.setAccount(account);

        return users;
    }

    @Override
    public UserSave saveUser(Users users) {
        UserSave userSave = new UserSave();
        Users user1 = userRepository.findByPhoneNumber(users.getPhoneNumber()).orElse(null);
        Users user2 = userRepository.findByAccount_Email(users.getAccount().getEmail()).orElse(null);
        if(user1 == null && user2 == null) {
            userRepository.save(users);
            userSave.setStatus("Success");
            userSave.setMessage("Save job success!!!");
        }else {
            userSave.setStatus("Fail");
            userSave.setMessage("Save job fail!!!");
        }
        return userSave;
    }

    @Override
    public UserSave updateUser(Users users) {
        UserSave userSave = new UserSave();
        Users user1 = userRepository.findByPhoneNumber(users.getPhoneNumber()).orElse(null);
        Users user2 = userRepository.findByAccount_Email(users.getAccount().getEmail()).orElse(null);
        if(users.getId() != null && (user1 == null || user1.getId().equals(users.getId())) &&
            (user2 == null || user2.getId().equals(users.getId()))) {
            userRepository.save(users);
            userSave.setStatus("Success");
            userSave.setMessage("Update job success!!!");
        }else {
            userSave.setStatus("Fail");
            userSave.setMessage("Update job fail!!!");
        }

        return userSave;
    }

    @Override
    public Users findUserByID(Long id) {
        return userRepository.findById(id).orElseThrow();
    }


    @Transactional
    @Override
    public UserSave changePassword(UserChangePassword userChangePassword) {
        UserSave userSave = new UserSave();
        if(!Objects.equals(userChangePassword.getNewPassword(), userChangePassword.getNewPasswordRepeat())) {
            userSave.setStatus("false");
            userSave.setMessage("The entered passwords are not the same !!!");
        }else {
            Users users = userRepository.findByAccount_Email(userChangePassword.getEmail()).orElseThrow();
            users.getAccount().setCheckPassword(true);
            users.getAccount().setPassword(passwordEncoder.encode(userChangePassword.getNewPassword()));
            userSave.setStatus("success");
            userSave.setMessage("Password changed successfully !!!");
        }
        return userSave;
    }

    public String generatePassWord() {
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            randomString.append(randomChar);
        }
        return randomString.toString();
    }

}

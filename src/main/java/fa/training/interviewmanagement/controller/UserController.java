package fa.training.interviewmanagement.controller;

import fa.training.interviewmanagement.entities.Account;
import fa.training.interviewmanagement.entities.Department;
import fa.training.interviewmanagement.entities.Users;
import fa.training.interviewmanagement.repository.DepartmentRepository;
import fa.training.interviewmanagement.service.EmailService;
import fa.training.interviewmanagement.service.UserService;
import fa.training.interviewmanagement.web.request.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @GetMapping("")
    public String userList(HttpServletRequest req, @ModelAttribute UserSearch userSearch, Model model) {
        setTitle(req);
        userSearch = checkUserSearch(userSearch);
        Page<Users> users = userService.findAllUser(userSearch);
        int totalPage = users.getTotalPages();
        if(totalPage > 0) {
            List<Integer> integers = new ArrayList<>();
            for (int i = 1; i <= totalPage; i++) {
                integers.add(i);
            }
            userSearch.setPageNumbers(integers);
        }else {
            userSearch.setMessage("No user result!!!");
        }
        model.addAttribute("users", users);
        model.addAttribute("userSearch", userSearch);
        return "ui/user/user-list";
    }

    @GetMapping("/add")
    public String userCreate(@ModelAttribute("userRequest") UserRequest userRequest, Model model,
                             HttpServletRequest req) {
        HttpSession session = req.getSession();
        model.addAttribute("departments", session.getAttribute("departments"));
        return "ui/user/user-create";
    }

    @GetMapping("/changePassword")
    public String changePassword(@ModelAttribute("userChangePassword") UserChangePassword userChangePassword) {
        return "ui/user/change-password";
    }

    @PostMapping("/changePassword")
    public String changePasswordForm(@ModelAttribute("userChangePassword") @Validated UserChangePassword userChangePassword,
            BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("userEmail", userChangePassword.getEmail());
            return "ui/user/change-password";
        }
        UserSave userSave = userService.changePassword(userChangePassword);
        model.addAttribute("userSave", userSave);
        model.addAttribute("userEmail", userChangePassword.getEmail());
        return "ui/user/change-password";
    }

    @PostMapping("/add")
    public String userCreateForm(@ModelAttribute("userRequest") @Validated UserRequest userRequest, BindingResult bindingResult,
                         RedirectAttributes ra, HttpServletRequest req, Model model) throws MessagingException {

        if(bindingResult.hasErrors()) {
            HttpSession session = req.getSession();
            model.addAttribute("departments", session.getAttribute("departments"));
            return "ui/user/user-create";
        }
        Users users = userService.createUserByUserRequest(userRequest);
        emailService.sendMailToUser(users.getAccount());
        users.getAccount().setPassword(passwordEncoder.encode(users.getAccount().getPassword()));
        UserSave userSave = userService.saveUser(users);

        ra.addFlashAttribute("userSave", userSave);
        return "redirect:/user/add";
    }

    @GetMapping("/edit/{id}")
    public String userUpdate(Model model, @RequestParam(value = "subModule", required = false) String subModule,
                             @PathVariable Long id, HttpServletRequest req) {

        Users users = userService.findUserByID(id);
        Account account = users.getAccount();
        Long departmentId = users.getDepartment().getId();
        users.setDepartment(null);

        UserRequest userRequest  = modelMapper.map(users, UserRequest.class);
        modelMapper.map(account, userRequest);
        userRequest.setDepartment(departmentId);

        HttpSession session = req.getSession();
        model.addAttribute("departments", session.getAttribute("departments"));
        model.addAttribute("subModule", subModule);
        model.addAttribute("userRequest", userRequest);
        return "ui/user/user-update";
    }

    @PostMapping("/edit")
    public String userUpdateForm(@ModelAttribute("userRequest") @Validated UserRequest userRequest, BindingResult bindingResult, Model model,
                                 HttpServletRequest req, RedirectAttributes ra, @RequestParam("subModule") String subModule) {

        if(bindingResult.hasErrors()) {
            HttpSession session = req.getSession();
            model.addAttribute("departments", session.getAttribute("departments"));
            model.addAttribute("subModule", subModule);
            model.addAttribute("userRequest", userRequest);
            return "ui/user/user-update";
        }
        Users users = userService.createUserByUserRequest(userRequest);
        UserSave userSave = userService.updateUser(users);

        ra.addFlashAttribute("userSave", userSave);
        ra.addAttribute("id", userRequest.getId());
        ra.addAttribute("subModule", subModule);
        return  "redirect:/user/edit/{id}";
    }


    @GetMapping("/detail/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        Users users = userService.findUserByID(id);
        model.addAttribute("users", users);
        return "ui/user/user-detail";
    }


    public UserSearch checkUserSearch(UserSearch userSearch) {
        if(userSearch.getPageIndex() == null) {
            userSearch.setPageIndex(1);
        }
        if(userSearch.getNameSearch() == null || userSearch.getNameSearch().isEmpty()) {
            userSearch.setNameSearch("");
        }
        return userSearch;
    }
    private void setTitle(HttpServletRequest req) {
        List<Department> departments = departmentRepository.findAll();
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("title", "USER");
        httpSession.setAttribute("departments", departments);
    }
}

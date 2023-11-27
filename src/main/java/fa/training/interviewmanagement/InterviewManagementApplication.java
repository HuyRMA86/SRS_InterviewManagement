package fa.training.interviewmanagement;

import fa.training.interviewmanagement.entities.*;
import fa.training.interviewmanagement.enums.EGender;
import fa.training.interviewmanagement.enums.ERole;
import fa.training.interviewmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
@EnableAsync
public class InterviewManagementApplication extends SpringBootServletInitializer implements CommandLineRunner {


    final private SkillRepository skillRepository;
    final private BenefitsRepository benefitsRepository;
    final private LevelRepository levelRepository;
    final private DepartmentRepository departmentRepository;
    final private CandidateRepository candidateRepository;
    final private JobRepository jobRepository;
    final private UserRepository userRepository;
    final private InterviewScheduleRepository interviewScheduleRepository;
    final private PasswordEncoder passwordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(InterviewManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Skill> skills = List.of(Skill.builder().name("Java").build(),
                Skill.builder().name("Nodejs").build(),
                Skill.builder().name(".net").build(),
                Skill.builder().name("C++").build(),
                Skill.builder().name("BA").build(),
                Skill.builder().name("Communication").build()
        );

        List<Benefits> benefits = List.of(
                Benefits.builder().name("Health insurance").build(),
                Benefits.builder().name("Dental insurance").build(),
                Benefits.builder().name("Vision insurance").build(),
                Benefits.builder().name("Life insurance").build(),
                Benefits.builder().name("Paid time off").build(),
                Benefits.builder().name("Mentorship").build(),
                Benefits.builder().name("Travel").build(),
                Benefits.builder().name("Company mission").build()
        );

        List<Department> departments = List.of(
                Department.builder().name("IT").build(),
                Department.builder().name("HR").build(),
                Department.builder().name("Finance").build(),
                Department.builder().name("Communication").build(),
                Department.builder().name("Marketing").build(),
                Department.builder().name("Accounting").build()
        );
        List<Level> levels = List.of(
                Level.builder().name("Fresher 1").build(),
                Level.builder().name("Junior 2.1").build(),
                Level.builder().name("Junior 2.2").build(),
                Level.builder().name("Junior 3.1").build(),
                Level.builder().name("Junior 3.2").build(),
                Level.builder().name("Delivery").build(),
                Level.builder().name("Leader").build(),
                Level.builder().name("Manager").build(),
                Level.builder().name("Vice Head").build()
        );



        List<Users> users = List.of(
                Users.builder()
                        .fullName("Admin")
                        .account(
                        Account.builder()
                                .email("admin@gmail.com")
                                .password(passwordEncoder.encode("123456"))
                                .status(true)
                                .role(ERole.ROLE_ADMIN)
                                .checkPassword(true)
                                .user(Users.builder().id(1L).build())
                                .build())
                        .department(Department.builder().id(3L).build())
                        .address("Hưng Yên")
                        .dob(LocalDate.of(1998,10,20))
                        .gender(EGender.MALE)
                        .note("N/A")
                        .phoneNumber("0962248930")
                        .build(),
                Users.builder()
                        .fullName("Duong")
                        .account(
                                Account.builder()
                                        .email("Duongdoan@gmail.com")
                                        .password(passwordEncoder.encode("123456"))
                                        .status(true)
                                        .role(ERole.ROLE_MANAGER)
                                        .checkPassword(true)
                                        .user(Users.builder().id(2L).build())
                                        .build())
                        .department(Department.builder().id(3L).build())
                        .address("Hưng Yên")
                        .dob(LocalDate.of(1998,10,20))
                        .gender(EGender.MALE)
                        .note("N/A")
                        .phoneNumber("0962248930")
                        .build(),
                Users.builder()
                        .fullName("khai")
                        .account(
                                Account.builder()
                                        .email("khai123@gmail.com")
                                        .password(passwordEncoder.encode("123456"))
                                        .status(true)
                                        .role(ERole.ROLE_MANAGER)
                                        .checkPassword(true)
                                        .user(Users.builder().id(3L).build())
                                        .build())
                        .department(Department.builder().id(3L).build())
                        .address("Hưng Yên")
                        .dob(LocalDate.of(1998,10,20))
                        .gender(EGender.MALE)
                        .note("N/A")
                        .phoneNumber("0962248930")
                        .build(),
                Users.builder()
                        .fullName("Manh")
                        .account(
                                Account.builder()
                                        .email("manh@gmail.com")
                                        .password(passwordEncoder.encode("123456"))
                                        .status(true)
                                        .role(ERole.ROLE_RECRUITER)
                                        .checkPassword(true)
                                        .user(Users.builder().id(4L).build())
                                        .build())
                        .department(Department.builder().id(3L).build())
                        .address("Hưng Yên")
                        .dob(LocalDate.of(1998,10,20))
                        .gender(EGender.MALE)
                        .note("N/A")
                        .phoneNumber("0962248930")
                        .build(),
                Users.builder()
                        .fullName("Tuan")
                        .account(
                                Account.builder()
                                        .email("tuan@gmail.com")
                                        .password(passwordEncoder.encode("123456"))
                                        .status(true)
                                        .role(ERole.ROLE_RECRUITER)
                                        .checkPassword(true)
                                        .user(Users.builder().id(5L).build())
                                        .build())
                        .department(Department.builder().id(3L).build())
                        .address("Hưng Yên")
                        .dob(LocalDate.of(1998,10,20))
                        .gender(EGender.MALE)
                        .note("N/A")
                        .phoneNumber("0962248930")
                        .build(),
                Users.builder()
                        .fullName("Long")
                        .account(
                                Account.builder()
                                        .email("Long123@gmail.com")
                                        .password(passwordEncoder.encode("123456"))
                                        .status(true)
                                        .role(ERole.ROLE_RECRUITER)
                                        .checkPassword(true)
                                        .user(Users.builder().id(6L).build())
                                        .build())
                        .department(Department.builder().id(3L).build())
                        .address("Hưng Yên")
                        .dob(LocalDate.of(1998,10,20))
                        .gender(EGender.MALE)
                        .note("N/A")
                        .phoneNumber("0962248930")
                        .build(),
                Users.builder()
                        .fullName("David")
                        .account(
                                Account.builder()
                                        .email("david@gmail.com")
                                        .password(passwordEncoder.encode("123456"))
                                        .status(true)
                                        .role(ERole.ROLE_INTERVIEW)
                                        .checkPassword(true)
                                        .user(Users.builder().id(7L).build())
                                        .build())
                        .department(Department.builder().id(3L).build())
                        .address("Hưng Yên")
                        .dob(LocalDate.of(1998,10,20))
                        .gender(EGender.MALE)
                        .note("N/A")
                        .phoneNumber("0962248930")
                        .build(),
                Users.builder()
                        .fullName("Jame")
                        .account(
                                Account.builder()
                                        .email("jame@gmail.com")
                                        .password(passwordEncoder.encode("123456"))
                                        .status(true)
                                        .role(ERole.ROLE_INTERVIEW)
                                        .checkPassword(true)
                                        .user(Users.builder().id(8L).build())
                                        .build())
                        .department(Department.builder().id(3L).build())
                        .address("Hưng Yên")
                        .dob(LocalDate.of(1998,10,20))
                        .gender(EGender.MALE)
                        .note("N/A")
                        .phoneNumber("0962248930")
                        .build(),
                Users.builder()
                        .fullName("Max")
                        .account(
                                Account.builder()
                                        .email("max@gmail.com")
                                        .password(passwordEncoder.encode("123456"))
                                        .status(true)
                                        .role(ERole.ROLE_INTERVIEW)
                                        .checkPassword(true)
                                        .user(Users.builder().id(9L).build())
                                        .build())
                        .department(Department.builder().id(3L).build())
                        .address("Hưng Yên")
                        .dob(LocalDate.of(1998,10,20))
                        .gender(EGender.MALE)
                        .note("N/A")
                        .phoneNumber("0962248930")
                        .build(),
                Users.builder()
                        .fullName("Leo")
                        .account(
                                Account.builder()
                                        .email("Leoo@gmail.com")
                                        .password(passwordEncoder.encode("123456"))
                                        .status(true)
                                        .role(ERole.ROLE_INTERVIEW)
                                        .checkPassword(true)
                                        .user(Users.builder().id(10L).build())
                                        .build())
                        .department(Department.builder().id(3L).build())
                        .address("Hưng Yên")
                        .dob(LocalDate.of(1998,10,20))
                        .gender(EGender.MALE)
                        .note("N/A")
                        .phoneNumber("0962248930")
                        .build(),
                Users.builder()
                        .fullName("Ronaldo")
                        .account(
                                Account.builder()
                                        .email("donalro@gmail.com")
                                        .password(passwordEncoder.encode("123456"))
                                        .status(true)
                                        .role(ERole.ROLE_INTERVIEW)
                                        .checkPassword(true)
                                        .user(Users.builder().id(11L).build())
                                        .build())
                        .department(Department.builder().id(3L).build())
                        .address("Hưng Yên")
                        .dob(LocalDate.of(1998,10,20))
                        .gender(EGender.MALE)
                        .note("N/A")
                        .phoneNumber("0962248930")
                        .build()
        );

//        skillRepository.saveAll(skills);
//        benefitsRepository.saveAll(benefits);
//        levelRepository.saveAll(levels);
//        departmentRepository.saveAll(departments);
//        userRepository.saveAll(users);
    }
}

package fa.training.interviewmanagement.entities;

import fa.training.interviewmanagement.enums.EGender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    private LocalDate dob;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Nationalized
    private String address;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    private String note;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDate createDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private Department department;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Account account;

    @OneToMany(mappedBy = "recruiter",fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Candidate> candidate;

    @OneToMany(mappedBy = "updateBy", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Candidate> candidateUpdate;

    @OneToMany(mappedBy = "createBy", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Job> jobs;

    @OneToMany(mappedBy = "recruiter",fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<InterviewSchedule> interviewScheduleRecruiter;

    @OneToMany(mappedBy = "interviewer")
    @ToString.Exclude
    private List<InterviewerSchedule> schedules;

    @OneToMany(mappedBy = "recruiter" ,fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Offer> offerRecruiter;

    @OneToMany(mappedBy = "manager" ,fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Offer> offerManager;


}

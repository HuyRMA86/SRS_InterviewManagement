package fa.training.interviewmanagement.entities;

import fa.training.interviewmanagement.enums.EGender;
import fa.training.interviewmanagement.enums.EHighestLevel;
import fa.training.interviewmanagement.enums.EPosition;
import fa.training.interviewmanagement.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Candidate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
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

    @Nationalized
    private String note;
    private String email;
    private String cv;

    @Enumerated(EnumType.STRING)
    private EPosition position;
    @Enumerated(EnumType.STRING)
    private EStatus status;

    @Column(name = "Year_of_Experience")
    private Long yearOfExperience;

    @Enumerated(EnumType.STRING)
    private EHighestLevel highestLevel;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Users recruiter;

    @ManyToOne
    @JoinColumn(name = "update_by")
    private Users updateBy;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDate createDate;

    @CreationTimestamp
    @Column(name = "update_date")
    private LocalDate updateDate;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<SkillCandidate> skillCandidates;

    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL)
    @ToString.Exclude
    private ResultInterview resultInterview;
}

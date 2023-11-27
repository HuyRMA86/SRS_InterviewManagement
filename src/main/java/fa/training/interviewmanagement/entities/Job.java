package fa.training.interviewmanagement.entities;

import fa.training.interviewmanagement.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Job  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "salary_from")
    private BigDecimal salaryFrom;
    @Column(name = "salary_to")
    private BigDecimal salaryTo;
    @Nationalized
    @Column(name = "working_address")
    private String workingAddress;
    @Nationalized
    private String description;

    @Enumerated(EnumType.STRING)
    private EStatus status;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDate createDate;

    @ManyToOne
    @JoinColumn(name = "create_by")
    private Users createBy;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<SkillJob> skillJobs;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<BenefitsJob> benefitsJobs;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;


}

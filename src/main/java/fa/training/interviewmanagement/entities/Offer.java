package fa.training.interviewmanagement.entities;

import fa.training.interviewmanagement.enums.EContractType;
import fa.training.interviewmanagement.enums.EPosition;
import fa.training.interviewmanagement.enums.EStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offer  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EPosition position;


    @Enumerated(EnumType.STRING)
    private EContractType contractType;

    @Enumerated(EnumType.STRING)
    private EStatus status;

    @OneToOne
    @JoinColumn(name = "result_interview_id")
    private ResultInterview resultInterview;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Users recruiter;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Users manager;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "basic_salary")
    private BigDecimal basicSalary;

    @Column(name = "contract_from")
    private LocalDate contractFrom;

    @Column(name = "contract_to")
    private LocalDate contractTo;

    @Nationalized
    private String notes;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private Department department;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDate createDate;

    @CreationTimestamp
    @Column(name = "update_date")
    private LocalDate updateDate;

}

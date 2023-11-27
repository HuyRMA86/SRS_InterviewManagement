package fa.training.interviewmanagement.entities;

import fa.training.interviewmanagement.enums.EResult;
import fa.training.interviewmanagement.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewSchedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interview_schedule_id")
    private Long id;

    private String title;
    @Enumerated(EnumType.STRING)
    private EStatus status;

    private boolean location;

    @Nationalized
    private String meeting;

    private LocalDateTime schedule;

    @OneToMany(mappedBy = "schedule",cascade = CascadeType.ALL)
    private List<InterviewerSchedule> interviewer;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Users recruiter;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDate createDate;

    @CreationTimestamp
    @Column(name = "update_date")
    private LocalDate updateDate;

    @OneToOne(mappedBy = "interviewSchedule",cascade = CascadeType.ALL)
    private ResultInterview resultInterviews;
}

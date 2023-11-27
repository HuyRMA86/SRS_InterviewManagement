package fa.training.interviewmanagement.entities;

import fa.training.interviewmanagement.enums.EResult;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "result_interview", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultInterview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private EResult result;

    private String note;

    @OneToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "interview_schedule_id")
    @ToString.Exclude
    private InterviewSchedule interviewSchedule;

    @OneToOne(mappedBy = "resultInterview" ,cascade = CascadeType.ALL)
    @ToString.Exclude
    private Offer offer;
}

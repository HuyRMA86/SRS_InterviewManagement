package fa.training.interviewmanagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(KeySkillJob.class)
public class SkillJob implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "job_id")
    @ToString.Exclude
    private Job job;

    @Id
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
}

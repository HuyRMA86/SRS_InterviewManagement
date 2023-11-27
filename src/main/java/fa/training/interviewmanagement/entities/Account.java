package fa.training.interviewmanagement.entities;

import fa.training.interviewmanagement.enums.ERole;
import fa.training.interviewmanagement.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;
    private String email;
    private String password;

    private Boolean checkPassword;

    private Boolean status;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", unique = true)
    private Users user;

}

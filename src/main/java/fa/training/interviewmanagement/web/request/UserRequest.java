package fa.training.interviewmanagement.web.request;

import fa.training.interviewmanagement.enums.EGender;
import fa.training.interviewmanagement.enums.ERole;
import fa.training.interviewmanagement.validator.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private Long id;

    @NotBlank(message = "{check.notnull}")
    @Length(max = 50, message = "{fullName.length}")
    private String fullName;

    @NotBlank(message = "{check.notnull}")
    @Email(message = "{email.valid}")
    private String email;

    @Length(max = 250, message = "{workingAddress.length}")
    private String address;

    @NotNull(message = "{check.notnull}")
    private EGender gender;

    @Past(message = "{dob.valid}")
    private LocalDate dob;

    @NotBlank(message = "{check.notnull}")
    @Phone(message = "{phone.valid}")
    private String phoneNumber;

    @NotNull(message = "{check.notnull}")
    private ERole role;

    @Length(max = 1000, message = "{description.length}")
    private String note;

    @NotNull(message = "{check.notnull}")
    private Long department;

    private Boolean status;
    private Boolean checkPassword;
}

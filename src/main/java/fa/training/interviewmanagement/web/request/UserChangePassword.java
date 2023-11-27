package fa.training.interviewmanagement.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChangePassword {

    @NotBlank(message = "{check.notnull}")
    private String newPassword;
    @NotBlank(message = "{check.notnull}")
    private String newPasswordRepeat;
    private String email;
}

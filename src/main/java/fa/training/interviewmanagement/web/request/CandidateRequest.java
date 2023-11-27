package fa.training.interviewmanagement.web.request;

import fa.training.interviewmanagement.enums.EGender;
import fa.training.interviewmanagement.enums.EHighestLevel;
import fa.training.interviewmanagement.enums.EPosition;
import fa.training.interviewmanagement.enums.EStatus;
import fa.training.interviewmanagement.validator.Phone;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateRequest {
    private Long id;
    @NotEmpty
    private String fullName;
    @NotEmpty
    @Email
    private String email;
    @Past
    private LocalDate dob;
    private String address;
    @Phone
    @NotEmpty
    private String phoneNumber;
    @NotNull
    private EGender gender;
    private MultipartFile cv;
    @NotNull
    private EPosition position;
    private List<Long> skills;
    @Positive
    private Long recruiterId;
    @NotNull
    private EStatus status;
    @PositiveOrZero
    private Long yearOfExperience;
    @NotNull
    private EHighestLevel highestLevel;
    private String note;
}

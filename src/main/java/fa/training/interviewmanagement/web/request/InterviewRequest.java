package fa.training.interviewmanagement.web.request;

import fa.training.interviewmanagement.enums.EResult;
import fa.training.interviewmanagement.enums.EStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewRequest {
    private Long id;
    @Length(max = 2000)
    @NotEmpty(message = "Title must not be empty")
    private String title;
    @NotNull(message = "Candidate must not be null")
    private Long candidateId;

    @FutureOrPresent(message = "Schedule must be a date in the present or in the future")
    @NotNull(message = "Schedule must not be empty")
    private LocalDateTime schedule;
    @NotNull(message = "Interview must not be null")
    private List<Long> interviewId;
    private boolean location;
    @NotNull(message = "Recruiter must not be null")
    private Long recruiterId;
    @Length(max = 500)
    @NotEmpty(message = "Meeting must not be empty")
    private String meeting;

    @Length(max = 2000)
    private String note;
    private EStatus status;
    private EResult result;
}

package fa.training.interviewmanagement.web.request;

import fa.training.interviewmanagement.enums.EStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequest {

    private Long id;

    @NotBlank(message = "{check.notnull}")
    @Length(max = 100, message = "{title.length}")
    private String title;

    @FutureOrPresent(message = "{date.valid}")
    @NotNull(message = "{check.notnull}")
    private LocalDate startDate;

    @FutureOrPresent(message = "{date.valid}")
    @NotNull(message = "{check.notnull}")
    private LocalDate endDate;

    @Positive(message = "{salaryFrom.valid}")
    private BigDecimal salaryFrom;

    @Positive(message = "{salaryTo.valid}")
    private BigDecimal salaryTo;

    @Length(max = 250, message = "{workingAddress.length}")
    private String workingAddress;

    @Length(max = 1000, message = "{description.length}")
    private String description;

    @Size(min = 1, max = 4, message = "{skills.valid}")
    @NotNull(message = "{check.notnull}")
    private List<Long> skills;

    @Size(min = 1, max = 6, message = "{benefits.valid}")
    @NotNull(message = "{check.notnull}")
    private List<Long> benefits;

    @NotNull(message = "{check.notnull}")
    private Long level;

    private EStatus status;

}

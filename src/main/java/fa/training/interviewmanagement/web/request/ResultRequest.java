package fa.training.interviewmanagement.web.request;

import fa.training.interviewmanagement.enums.EResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultRequest {
    private Long interviewScheduleId;
    public EResult result;
    public String notes;
}

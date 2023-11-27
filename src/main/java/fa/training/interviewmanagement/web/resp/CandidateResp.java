package fa.training.interviewmanagement.web.resp;

import fa.training.interviewmanagement.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateResp {
    private EStatus eStatus;
    private String notes;
    private List<String> interviews;
}

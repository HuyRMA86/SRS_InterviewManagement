package fa.training.interviewmanagement.web.request;

import fa.training.interviewmanagement.enums.EStatus;
import lombok.Data;

import java.util.List;

@Data
public class OfferSearch {
    private String param;
    private EStatus status;
    private String department;
    private Integer pageNumber;
    private final Integer pageSize = 5;
    private List<Integer> pageMaxNumber;
    private String message;
}

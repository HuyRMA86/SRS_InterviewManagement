package fa.training.interviewmanagement.web.request;

import fa.training.interviewmanagement.enums.EStatus;
import lombok.Data;

import java.util.List;

@Data

public class CandidateSearch {

    private String nameKeyword;

    private EStatus status;

    private Integer pageNumber;

    private final Integer pageSize = 5;

    private List<Integer> pageMaxNumbers;

    private String message;
}

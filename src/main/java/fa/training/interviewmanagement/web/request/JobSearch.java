package fa.training.interviewmanagement.web.request;

import fa.training.interviewmanagement.enums.EStatus;
import lombok.Data;

import java.util.List;


@Data
public class JobSearch {
    private String nameSearch;

    private EStatus status;

    private Integer pageIndex;

    private List<Integer> pageNumbers;

    private String message;
}

package fa.training.interviewmanagement.web.request;

import fa.training.interviewmanagement.enums.ERole;
import lombok.Data;

import java.util.List;

@Data
public class UserSearch {
    private String nameSearch;

    private ERole role;

    private Integer pageIndex;

    private List<Integer> pageNumbers;

    private String message;
}

package fa.training.interviewmanagement.utils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public interface MessageList {

    String LOGIN_FAIL = "Invalid username/password. Please try again";
    String LOGIN_NO_ACTIVATED = "Account not activated";
    // Candidate
    String CREATE_CANDIDATE_FAIL = "Failed to created candidate";
    String CREATE_CANDIDATE_SUCCESS = "Successfully created candidate";
    String EDIT_CANDIDATE_SUCCESS = "Failed to updated candidate";
    String EDIT_CANDIDATE_FAIL = "Successfully updated candidate";

    String DELETE_CANDIDATE_FAIL = "Successfully deleted candidate";
    String DELETE_CANDIDATE_SUCCESS = "Failed to delete this candidate";
    //Job
    String CREATE_JOB_FAIL = "Failed to created job";
    String CREATE_JOB_SUCCESS = "Successfully created job";
    String EDIT_JOB_SUCCESS = "Failed to updated job";
    String EDIT_JOB_FAIL = "Successfully updated job";

    String DELETE_JOB_FAIL = "Successfully deleted job";
    String DELETE_JOB_SUCCESS = "Failed to delete this job";

    // User
    String CREATE_USER_FAIL = "Failed to created user";
    String CREATE_USER_SUCCESS = "Successfully created user";
    String EDIT_USER_SUCCESS = "Failed to updated user";
    String EDIT_USER_FAIL = "Successfully updated user";

    String DELETE_USER_FAIL = "Successfully deleted user";
    String DELETE_USER_SUCCESS = "Failed to delete this user";

    //interview
    String CREATE_INTERVIEW_FAIL = "Failed to created interview schedule";
    String CREATE_INTERVIEW_SUCCESS = "Successfully created interview schedule";
    String EDIT_INTERVIEW_SUCCESS = "Failed to updated interview schedule";
    String EDIT_INTERVIEW_FAIL = "Successfully updated interview schedule";

    String DELETE_INTERVIEW_FAIL = "Successfully deleted interview schedule";
    String DELETE_INTERVIEW_SUCCESS = "Failed to delete this interview schedule";

    String DONT_FOUND_INTERVIEW = "No interview schedule found";

    // offer
    String CREATE_OFFER_FAIL = "Failed to created offer";
    String CREATE_OFFER_SUCCESS = "Successfully created offer";
    String EDIT_OFFER_SUCCESS = "Failed to updated offer";
    String EDIT_OFFER_FAIL = "Successfully updated offer";

    String DELETE_OFFER_FAIL = "Successfully deleted offer";
    String DELETE_OFFER_SUCCESS = "Failed to delete this offer";
}

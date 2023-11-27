package fa.training.interviewmanagement.service;

import fa.training.interviewmanagement.entities.ResultInterview;

import java.util.Optional;

public interface ResultInterviewService {
    Optional<ResultInterview> findResultInterviewByCandidate_Id(Long id);
}

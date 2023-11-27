package fa.training.interviewmanagement.service;

import fa.training.interviewmanagement.entities.Candidate;
import fa.training.interviewmanagement.web.request.CandidateSearch;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CandidateService {

    Page<Candidate> findAllCandidate(CandidateSearch candidateSearch);

    Optional<Candidate> findCandidateById(Long id);

    void deleteCandidate(Long id);

    Candidate saveCandidate(Candidate candidate);

    List<Candidate> findAll();
}

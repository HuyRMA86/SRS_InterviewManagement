package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.entities.Candidate;
import fa.training.interviewmanagement.repository.CandidateRepository;
import fa.training.interviewmanagement.service.CandidateService;
import fa.training.interviewmanagement.web.request.CandidateSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class    CandidateServiceImpl implements CandidateService {
    final private CandidateRepository candidateRepository;

    @Override
    public Page<Candidate> findAllCandidate(CandidateSearch candidateSearch) {
        Pageable pageable = PageRequest.of(candidateSearch.getPageNumber() - 1, candidateSearch.getPageSize());
        if (candidateSearch.getStatus() == null) {
            return candidateRepository.findAll(candidateSearch.getNameKeyword(), pageable);
        }
        return candidateRepository.findAll(candidateSearch.getNameKeyword(), candidateSearch.getStatus(), pageable);
    }

    @Override
    public List<Candidate> findAll() {
        List<Candidate> candidates = candidateRepository.findAll();
        return candidates;
    }

    @Override
    public Optional<Candidate> findCandidateById(Long id) {
        return candidateRepository.findById(id);
    }

    @Override
    public void deleteCandidate(Long id) {
        if(findCandidateById(id) != null){
            candidateRepository.deleteById(id);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    @Transactional
    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }
}

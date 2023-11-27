package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.entities.ResultInterview;
import fa.training.interviewmanagement.repository.ResultInterviewRepository;
import fa.training.interviewmanagement.service.ResultInterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ResultInterviewServiceImpl implements ResultInterviewService {
    final private ResultInterviewRepository resultInterviewRepository;
    @Override
    public Optional<ResultInterview> findResultInterviewByCandidate_Id(Long id) {
        return resultInterviewRepository.findByCandidate_Id(id);
    }
}

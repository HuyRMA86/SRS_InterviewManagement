package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.repository.SkillCandidateRepository;
import fa.training.interviewmanagement.service.SkillCandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class SkillCandidateServiceImpl implements SkillCandidateService {
    private final SkillCandidateRepository skillCandidateRepository;
    @Override
    @Transactional
    public void deleteByCandidateId(Long id) {
        skillCandidateRepository.deleteByCandidate_Id(id);
    }
}

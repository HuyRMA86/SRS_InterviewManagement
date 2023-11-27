package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entities.BenefitsJob;
import fa.training.interviewmanagement.entities.KeyBenefitsJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenefitsJobRepository extends JpaRepository<BenefitsJob, KeyBenefitsJob> {
    BenefitsJob findByJob_IdAndBenefits_Id(Long jobId, Long benefitId);
}

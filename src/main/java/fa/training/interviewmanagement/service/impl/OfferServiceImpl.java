package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.entities.Candidate;
import fa.training.interviewmanagement.entities.Offer;
import fa.training.interviewmanagement.enums.EStatus;
import fa.training.interviewmanagement.repository.OfferRepository;
import fa.training.interviewmanagement.service.CandidateService;
import fa.training.interviewmanagement.service.OfferService;
import fa.training.interviewmanagement.web.request.OfferSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    final private OfferRepository offerRepository;

    private final CandidateService candidateService;
    @Override
    public Page<Offer> findAllOffer(OfferSearch offerSearch) {
        Pageable pageable = PageRequest.of(offerSearch.getPageNumber() - 1, offerSearch.getPageSize());
        if (offerSearch.getStatus() == null) {
            return offerRepository.findAll(
                    offerSearch.getParam(),
                    offerSearch.getDepartment(),
                    pageable
            );
        }
        return offerRepository.findAll(
                offerSearch.getParam(),
                offerSearch.getDepartment(),
                offerSearch.getStatus(),
                pageable
        );
    }

    @Override
    @Transactional
    public void deleteOffer(Long id) {
//        Offer offer = offerRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Offer don't found"));
        System.out.println("xoa");
        offerRepository.deleteByOfferId(id);
    }

    @Override
    public List<Offer> findAllOfferByDate(LocalDate fromDate, LocalDate toDate) {
        return offerRepository.findOfferByDate(fromDate,toDate);
    }

    @Override
    @Transactional
    public Offer saveOffer(Offer offer) {
        Offer newOffer = offerRepository.save(offer);
        Candidate candidate = newOffer.getResultInterview().getCandidate();
        candidate.setStatus(EStatus.WAITING_FOR_RESPONSE);
        candidateService.saveCandidate(candidate);
        return newOffer;
    }

    @Override
    public Optional<Offer> findOfferById(Long id) {
        return offerRepository.findById(id);
    }
}

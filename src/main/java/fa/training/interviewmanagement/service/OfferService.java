package fa.training.interviewmanagement.service;

import fa.training.interviewmanagement.entities.Offer;
import fa.training.interviewmanagement.web.request.OfferSearch;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OfferService {
    Page<Offer> findAllOffer(OfferSearch offerSearch);
    void deleteOffer(Long id);
    List<Offer> findAllOfferByDate(LocalDate fromDate, LocalDate toDate);

    Offer saveOffer(Offer offer);

    Optional<Offer> findOfferById(Long id);

}

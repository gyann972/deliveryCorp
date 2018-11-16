package yannis.technical.challenge.deliveryCorp.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import yannis.technical.challenge.deliveryCorp.model.Offer;

/**
 * @author YaYa
 *
 */
public interface OfferRepository extends JpaRepository<Offer, Long>  {

}

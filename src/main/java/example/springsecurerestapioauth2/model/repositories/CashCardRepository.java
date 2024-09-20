package example.springsecurerestapioauth2.model.repositories;

import example.springsecurerestapioauth2.model.CashCard;
import org.springframework.data.repository.CrudRepository;

public interface CashCardRepository extends CrudRepository<CashCard, Long> {
    Iterable<CashCard> findByOwner(String cardNumber);
}

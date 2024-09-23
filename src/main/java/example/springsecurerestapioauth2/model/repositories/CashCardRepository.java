package example.springsecurerestapioauth2.model.repositories;

import example.springsecurerestapioauth2.model.CashCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public interface CashCardRepository extends CrudRepository<CashCard, Long> {
    Iterable<CashCard> findByOwner(String owner);

    default Iterable<CashCard> findAll() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String owner = authentication.getName();
        return findByOwner(owner);
    }
}

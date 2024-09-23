package example.springsecurerestapioauth2.model.repositories;

import example.springsecurerestapioauth2.model.CashCard;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

public interface CashCardRepository extends CrudRepository<CashCard, Long> {
    Iterable<CashCard> findByOwner(String owner);

    @NonNull
    @Query("select * from cash_card cc where cc.owner = :#{authentication.name}")
    Iterable<CashCard> findAll();

//    @NonNull
//    default Iterable<CashCard> findAll() {
//        throw new UnsupportedOperationException("unsupported, please use findByOwner instead");
//    }
}

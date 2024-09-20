package example.springsecurerestapioauth2.controller;

import example.springsecurerestapioauth2.annotation.CurrentOwner;
import example.springsecurerestapioauth2.model.CashCard;
import example.springsecurerestapioauth2.model.repositories.CashCardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    private final CashCardRepository cashCards;

    public CashCardController(CashCardRepository cashCards) {
        this.cashCards = cashCards;
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        return this.cashCards.findById(requestedId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    private ResponseEntity<CashCard> createCashCard(
            @RequestBody CashCard newCashCardRequest,
            UriComponentsBuilder ucb,
            @CurrentOwner String owner
    ) {
        CashCard cashCard = new CashCard(newCashCardRequest.amount(), owner);
        CashCard savedCashCard = cashCards.save(cashCard);
        URI locationOfNewCashCard = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCashCard.id())
                .toUri();
        return ResponseEntity.created(locationOfNewCashCard).body(savedCashCard);
    }

    // Maybe use Authentication or @CurrentSecurityContext
    @GetMapping
    public ResponseEntity<Iterable<CashCard>> findAll(@CurrentOwner String owner) {
        var result = cashCards.findByOwner(owner);
        return ResponseEntity.ok(result);
    }
}

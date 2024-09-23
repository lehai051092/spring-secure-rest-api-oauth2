package example.springsecurerestapioauth2.controller;

import example.springsecurerestapioauth2.annotation.CurrentOwner;
import example.springsecurerestapioauth2.model.CashCard;
import example.springsecurerestapioauth2.model.repositories.CashCardRepository;
import example.springsecurerestapioauth2.model.requests.CashCardRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
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

    @PostAuthorize("returnObject.body.owner == authentication.name")
    @GetMapping("/{requestedId}")
    public ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        return this.cashCards.findById(requestedId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CashCard> createCashCard(@RequestBody CashCardRequest cashCardRequest, UriComponentsBuilder ucb, @CurrentOwner String owner) {
        CashCard cashCard = new CashCard(cashCardRequest.amount(), owner);
        CashCard savedCashCard = this.cashCards.save(cashCard);
        URI locationOfNewCashCard = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCashCard.id())
                .toUri();
        return ResponseEntity.created(locationOfNewCashCard).body(savedCashCard);
    }

    // Maybe use Authentication or @CurrentSecurityContext or @CurrentOwner
    @GetMapping
    public ResponseEntity<Iterable<CashCard>> findAll() {
        return ResponseEntity.ok(cashCards.findAll());
    }
}

package com.apischanskyi.blackjack.game;

import com.apischanskyi.blackjack.entity.Card;
import com.apischanskyi.blackjack.game.card.Deck;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.apischanskyi.blackjack.entity.CardLog.PlayerRole;
import static com.apischanskyi.blackjack.entity.Round.RoundState;

@Component("table")
@Scope(value = "prototype")
public class Table {

    @JsonIgnore
    private final Map<PlayerRole, LinkedHashSet<Card>> roundCards = new HashMap<PlayerRole, LinkedHashSet<Card>>() {{
        put(PlayerRole.DEALER, new LinkedHashSet<>());
        put(PlayerRole.PLAYER, new LinkedHashSet<>());
    }};

    @Autowired(required = true)
    @JsonIgnore
    private Deck deck;

    private Long roundId;

    private Long bet;

    private RoundState state;

    public Table(Long roundId, Long bet) {
        this.roundId = roundId;
        this.bet = bet;
    }

    public void addPlayerCards(Card ... cards) {
        getPlayerCards().addAll(Arrays.asList(cards));
    }

    public void addDealerCards(Card ... cards) {
        getDealerCards().addAll(Arrays.asList(cards));
    }

    public Long getRoundId() {
        return roundId;
    }

    public Long getBet() {
        return bet;
    }

    public Map<PlayerRole, LinkedHashSet<Card>> getRoundCards() {
        return roundCards;
    }

    public Set<Card> getPlayerCards() {
        return roundCards.get(PlayerRole.PLAYER);
    }

    public Set<Card> getDealerCards() {
        return roundCards.get(PlayerRole.DEALER);
    }

    public RoundState getState() {
        return state;
    }

    public Table hideDealerCard() {
        Table hiddenDeal = new Table(roundId, bet);
        hiddenDeal.addPlayerCards(getPlayerCards().toArray(new Card[getPlayerCards().size()]));
        hiddenDeal.addDealerCards(getDealerCards().iterator().next(), Card.getHiddenCard());
        hiddenDeal.state = state;
        hiddenDeal.roundId = roundId;
        hiddenDeal.bet = bet;
        return hiddenDeal;
    }

    public void setRoundState(RoundState state) {
        this.state = state;
    }

    public Deck getDeck() {
        return deck;
    }

}

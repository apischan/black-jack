package com.apischanskyi.blackjack.game;

import com.apischanskyi.blackjack.entity.Card;
import com.apischanskyi.blackjack.entity.CardLog;
import com.apischanskyi.blackjack.entity.Round;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

import static com.apischanskyi.blackjack.entity.CardLog.PlayerRole;
import static com.apischanskyi.blackjack.entity.Round.RoundState;

public class Deal {

    private Long roundId;

    private Long bet;

    @JsonIgnore
    private final Map<PlayerRole, LinkedList<Card>> roundCards = new HashMap<PlayerRole, LinkedList<Card>>() {{
        put(PlayerRole.DEALER, new LinkedList<>());
        put(PlayerRole.PLAYER, new LinkedList<>());
    }};

    private RoundState state;

    public Deal(Long roundId, Long bet) {
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

    public Map<PlayerRole, LinkedList<Card>> getRoundCards() {
        return roundCards;
    }

    public List<Card> getPlayerCards() {
        return roundCards.get(PlayerRole.PLAYER);
    }

    public List<Card> getDealerCards() {
        return roundCards.get(PlayerRole.DEALER);
    }

    public RoundState getState() {
        return state;
    }

    public Deal hideDealerCard() {
        Deal hiddenDeal = new Deal(roundId, bet);
        hiddenDeal.addPlayerCards(getPlayerCards().toArray(new Card[getPlayerCards().size()]));
        hiddenDeal.addDealerCards(getDealerCards().get(0), Card.getHiddenCard());
        hiddenDeal.state = state;
        hiddenDeal.roundId = roundId;
        hiddenDeal.bet = bet;
        return hiddenDeal;
    }

    public void setRoundState(RoundState state) {
        this.state = state;
    }
}

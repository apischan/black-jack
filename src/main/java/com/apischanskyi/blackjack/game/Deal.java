package com.apischanskyi.blackjack.game;

import com.apischanskyi.blackjack.entity.Card;
import com.apischanskyi.blackjack.entity.Round;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Deal {

    private Long roundId;

    private Long bet;

    private final List<Card> playerCards = new ArrayList<>();

    private final List<Card> dealerCards = new ArrayList<>();

    private Round.RoundState state;

    public Deal(Long roundId, Long bet) {
        this.roundId = roundId;
        this.bet = bet;
    }

    public void addPlayerCards(Card ... cards) {
        playerCards.addAll(Arrays.asList(cards));
    }

    public void addDealerCards(Card ... cards) {
        dealerCards.addAll(Arrays.asList(cards));
    }

    public Long getRoundId() {
        return roundId;
    }

    public Long getBet() {
        return bet;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }

    public List<Card> getDealerCards() {
        return dealerCards;
    }

    public Round.RoundState getState() {
        return state;
    }

    public Deal hideDealerCard() {
        Deal hiddenDeal = new Deal(roundId, bet);
        hiddenDeal.addPlayerCards(playerCards.toArray(new Card[playerCards.size()]));
        hiddenDeal.addDealerCards(dealerCards.get(0), Card.getHiddenCard());
        hiddenDeal.state = state;
        hiddenDeal.roundId = roundId;
        hiddenDeal.bet = bet;
        return hiddenDeal;
    }

    public void setRoundState(Round.RoundState state) {
        this.state = state;
    }
}

package com.apischanskyi.blackjack.game.logic;

import com.apischanskyi.blackjack.game.card.Deck;
import com.apischanskyi.blackjack.game.Deal;

public class GameState {

    private Deal deal;

    private Deck deck;

    public GameState(Deal deal) {
        this.deal = deal;
        this.deck = new Deck();
    }

    public Deal getDeal() {
        return deal;
    }

    public void deal() {
        deal.addPlayerCards(deck.dealToPlayer());
        deal.addDealerCards(deck.dealToPlayer());
    }

    public void hitPlayer() {
        deal.addPlayerCards(deck.giveOneCard());
    }

    public void hitDealer() {
        deal.addDealerCards(deck.giveOneCard());
    }
}

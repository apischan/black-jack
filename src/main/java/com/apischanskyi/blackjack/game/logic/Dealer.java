package com.apischanskyi.blackjack.game.logic;

import com.apischanskyi.blackjack.entity.Card;
import com.apischanskyi.blackjack.game.Table;
import com.apischanskyi.blackjack.game.card.Deck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class Dealer {

    private Table table;

    public Dealer(Table table) {
        this.table = table;
    }

    public void deal() {
        Deck deck = table.getDeck();

        Card[] playerCards = deck.dealToPlayer();
        Card[] dealerCards = deck.dealToPlayer();

        table.addPlayerCards(playerCards);
        table.addDealerCards(dealerCards);
    }

    public void hitPlayer() {
        Deck deck = table.getDeck();
        table.addPlayerCards(deck.giveOneCard());
    }

    public void hitDealer() {
        Deck deck = table.getDeck();
        table.addDealerCards(deck.giveOneCard());
    }

}

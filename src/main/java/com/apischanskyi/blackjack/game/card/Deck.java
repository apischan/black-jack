package com.apischanskyi.blackjack.game.card;

import com.apischanskyi.blackjack.entity.Card;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.apischanskyi.blackjack.entity.Card.*;

@Component
@Scope(value = "prototype")
public class Deck {

    private Stack<Card> deckHolder = new Stack<>();

//    public Deck() {}

    @PostConstruct
    public void init() {
        System.out.println("HELLO!!!!!!!!!!!!!!!");
        for (Rank rank : Rank.values()) {
            if (rank != Rank.HIDDEN) {
                for (Suit suit : Suit.values()) {
                    if (suit != Suit.HIDDEN) {
                        deckHolder.push(new Card(suit, rank));
                    }
                }
            }
        }
        Collections.shuffle(deckHolder);
    }

    /**
     * Give one card from deck
     *
     * @return one card from deck
     */
    public Card giveOneCard() {
        return deckHolder.pop();
    }

    /**
     * Initially give 2 cards to player
     *
     * @return give initial amount of cards to player
     */
    public Card[] dealToPlayer() {
        return new Card[] { giveOneCard(), giveOneCard() };
    }
}

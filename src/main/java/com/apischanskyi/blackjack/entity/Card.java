package com.apischanskyi.blackjack.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
public class Card implements Serializable {

    @Column(name = "suit")
    @Enumerated(EnumType.STRING)
    private Suit suit;

    @Column(name = "rank")
    @Enumerated(EnumType.STRING)
    private Rank rank;

    public Card() {
    }

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public static enum Suit {
        SPADES, CLUBS, HEARTS, DIAMONDS, HIDDEN
    }

    public static enum Rank {
        ACE(11, 1),
        KING(10, 10),
        QUEEN(10, 10),
        JACK(10, 10),
        TEN(10, 10),
        NINE(9, 9),
        EIGHT(8, 8),
        SEVEN(7, 7),
        SIX(6, 6),
        FIVE(5, 5),
        FOUR(4, 4),
        THREE(3, 3),
        TWO(2, 2),
        HIDDEN(0, 0);

        int cost;
        int alterCost;

        Rank(int cost, int alterCost) {
            this.cost = cost;
            this.alterCost = alterCost;
        }

        public int getCost() {
            return cost;
        }

        public int getAlterCost() {
            return alterCost;
        }
    }

    /**
     * Produce card with HIDDEN {@link Rank} and {@link Suit}
     *
     * @return card with hidden both {@link Rank} and {@link Suit}
     */
    public static Card getHiddenCard() {
        return new Card(Suit.HIDDEN, Rank.HIDDEN);
    }

    @Override
    public String toString() {
        return "Card{" +
                "suit=" + suit +
                ", rank=" + rank +
                '}';
    }
}

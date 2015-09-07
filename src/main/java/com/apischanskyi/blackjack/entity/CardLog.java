package com.apischanskyi.blackjack.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "card_log")
public class CardLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_log_id_seq")
    @SequenceGenerator(name = "card_log_id_seq", sequenceName = "card_log_id_seq", allocationSize = 1)
    @Column(name = "id", unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlayerRole playerRole;

    @Embedded
    private Card card;

    @ManyToOne
    private Round round;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public enum PlayerRole {
        PLAYER,
        DEALER
    }
}

package com.apischanskyi.blackjack.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "round")
public class Round implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "round_id_seq")
    @SequenceGenerator(name = "round_id_seq", sequenceName = "round_id_seq", allocationSize = 1)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RoundState status;

    @Column(name = "bet")
    private Long bet;

    @Column(name = "round_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date roundDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "round")
    public Set<CardLog> cardLogs;

    public Round() {}

    public Round(User user, Long bet) {
        this.user = user;
        this.bet = bet;
        this.status = RoundState.READY;
        this.roundDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RoundState getStatus() {
        return status;
    }

    public void setStatus(RoundState status) {
        this.status = status;
    }

    public Long getBet() {
        return bet;
    }

    public void setBet(Long bet) {
        this.bet = bet;
    }

    public Set<CardLog> getCardLogs() {
        return cardLogs;
    }

    public void setCardLogs(Set<CardLog> cardLogs) {
        this.cardLogs = cardLogs;
    }

    public Date getRoundDate() {
        return roundDate;
    }

    public void setRoundDate(Date roundDate) {
        this.roundDate = roundDate;
    }

    public enum RoundState {
        READY(false),
        IN_PROGRESS(false),
        CANCELLED(true),
        WIN(true),
        LOOSE(true),
        BOOST(true),
        BLACK_JACK(true),
        PUSH(true);

        boolean isTerminalState;

        RoundState(boolean isTerminalState) {
            this.isTerminalState = isTerminalState;
        }

        public boolean isTerminalState() {
            return isTerminalState;
        }
    }

}

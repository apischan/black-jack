package com.apischanskyi.blackjack.service.declaration;

import com.apischanskyi.blackjack.game.logic.GameState;
import com.apischanskyi.blackjack.exceptions.BlackJackException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

public interface BetService {

    /**
     * Place a bet. Creates new round
     *
     * @param playerId instance ID of player
     * @param amount amount of bet
     * @return inst ID of new round
     * @throws BlackJackException in case of null amount of bet and insufficient balance
     */
    Long bet(long playerId, Long amount);

    /**
     * Make the payment by results of hte round
     *
     * @param gameState contains bet and state of round
     * @param userId instance id of user
     */
    void pay(GameState gameState, long userId);

    /**
     * Cancels existing bet
     *
     * @param roundId instance ID of round
     */
    void cancelBet(Long roundId);
}

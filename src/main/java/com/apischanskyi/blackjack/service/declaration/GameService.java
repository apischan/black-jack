package com.apischanskyi.blackjack.service.declaration;

import com.apischanskyi.blackjack.game.logic.GameState;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public interface GameService {

    /**
     * Performs the DEAL action
     *
     * @param playerId instance ID of player
     * @param roundId instance ID of round
     * @return state of game after dealing
     */
    GameState deal(long playerId, long roundId);

    /**
     * Performs HIT action
     *
     * @param gameState state of game
     * @param playerId instance ID of player
     * @param roundId instance ID of round
     * @return changed state of game after hitting
     */
    GameState hit(GameState gameState, long playerId, long roundId);

    /**
     * Perform the STAND action
     *
     * @param gameState state of game
     * @param playerId instance ID of player
     * @param roundId instance ID of round
     * @return changed state of game after standing
     */
    GameState stand(GameState gameState, long playerId, long roundId);
}

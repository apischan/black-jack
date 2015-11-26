package com.apischanskyi.blackjack.service.declaration;

import com.apischanskyi.blackjack.game.Table;

public interface GameService {

    /**
     * Performs the DEAL action
     *
     * @param playerId instance ID of player
     * @param roundId instance ID of round
     * @return state of game after dealing
     */
    Table deal(long playerId, long roundId);

    /**
     * Performs HIT action
     *
     * @param table state of game
     * @param playerId instance ID of player
     * @param roundId instance ID of round
     * @return changed state of game after hitting
     */
    Table hit(Table table, long playerId, long roundId);

    /**
     * Perform the STAND action
     *
     * @param table state of game
     * @param playerId instance ID of player
     * @param roundId instance ID of round
     * @return changed state of game after standing
     */
    Table stand(Table table, long playerId, long roundId);
}

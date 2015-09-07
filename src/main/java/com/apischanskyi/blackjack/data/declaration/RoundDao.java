package com.apischanskyi.blackjack.data.declaration;

import com.apischanskyi.blackjack.entity.Round;

import java.util.List;

public interface RoundDao {

    Long newRound(Round round);

    Round getRound(long roundId);

    void changeRoundState(Round round, Round.RoundState roundState);

    List<Round> getUserRounds(long playerId, Round.RoundState... states);
}

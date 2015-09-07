package com.apischanskyi.blackjack.service;

import com.apischanskyi.blackjack.exceptions.BlackJackExceptionHelper;
import com.apischanskyi.blackjack.game.logic.GameState;
import com.apischanskyi.blackjack.service.declaration.GameService;
import com.apischanskyi.blackjack.data.declaration.RoundDao;
import com.apischanskyi.blackjack.entity.Round;
import com.apischanskyi.blackjack.game.Deal;
import com.apischanskyi.blackjack.game.logic.GameLogic;
import com.apischanskyi.blackjack.service.declaration.BetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.apischanskyi.blackjack.entity.Round.RoundState;

public class GameServiceImpl implements GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    @Autowired
    private RoundDao roundDao;

    @Autowired
    private GameLogic gameLogic;

    @Autowired
    private BetService betService;

    /**
     * {@inheritDoc}
     */
    public GameState deal(long playerId, long roundId) {
        Round round = roundDao.getRound(roundId);
        if (round.getStatus() != RoundState.READY) {
            logger.info("Game status is: {}", round.getStatus());
            throw BlackJackExceptionHelper.newBlackJackException(BlackJackExceptionHelper.ErrorCode.DEAL_HAS_BEEN_PERFORMED);
        }
        GameState gameState = gameLogic.deal(roundId, round.getBet());
        Deal deal = gameState.getDeal();
        RoundState roundState;
        if (gameLogic.isBlackJackCombination(deal.getPlayerCards())) {
            roundState = RoundState.BLACK_JACK;
        } else {
            roundState = RoundState.IN_PROGRESS;
        }
        applyRoundState(gameState, round, roundState, playerId);
        return gameState;
    }

    /**
     * {@inheritDoc}
     */
    public GameState hit(GameState gameState, long playerId, long roundId) {
        Round round = roundDao.getRound(roundId);
        if (round.getStatus() != RoundState.IN_PROGRESS) {
            logger.info("Game status is: {}", round.getStatus());
            throw BlackJackExceptionHelper.newBlackJackException(BlackJackExceptionHelper.ErrorCode.HIT_IS_NOT_ALLOWED_HERE);
        }
        gameState.hitPlayer();
        Deal deal = gameState.getDeal();
        if (gameLogic.isBoosted(deal.getPlayerCards())) {
            applyRoundState(gameState, round, RoundState.BOOST, playerId);
        }
        return gameState;
    }

    /**
     * {@inheritDoc}
     */
    public GameState stand(GameState gameState, long playerId, long roundId) {
        Round round = roundDao.getRound(roundId);
        gameLogic.dealerPlay(gameState);
        RoundState roundResult = gameLogic.judge(gameState);
        applyRoundState(gameState, round, roundResult, playerId);
        return gameState;
    }

    private void applyRoundState(GameState gameState, Round round, RoundState roundState, long playerId) {
        roundDao.changeRoundState(round, roundState);
        gameState.getDeal().setRoundState(roundState);
        if (roundState.isTerminalState()) {
            betService.pay(gameState, playerId);
        }
    }
}

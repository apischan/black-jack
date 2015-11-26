package com.apischanskyi.blackjack.service;

import com.apischanskyi.blackjack.exceptions.BlackJackExceptionHelper;
import com.apischanskyi.blackjack.game.Table;
import com.apischanskyi.blackjack.game.TableContainer;
import com.apischanskyi.blackjack.game.logic.Dealer;
import com.apischanskyi.blackjack.service.declaration.GameService;
import com.apischanskyi.blackjack.data.declaration.RoundDao;
import com.apischanskyi.blackjack.entity.Round;
import com.apischanskyi.blackjack.game.logic.GameLogic;
import com.apischanskyi.blackjack.service.declaration.BetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.apischanskyi.blackjack.entity.Round.RoundState;
import static com.apischanskyi.blackjack.exceptions.BlackJackExceptionHelper.ErrorCode;

@Service
@Transactional
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
    public Table deal(long playerId, long roundId) {
        logger.info("BetService: {}", betService);
        Round round = roundDao.getRound(roundId);
        if (round.getStatus() != RoundState.READY) {
            logger.info("Game status is: {}", round.getStatus());
            throw BlackJackExceptionHelper.newBlackJackException(ErrorCode.DEAL_HAS_BEEN_PERFORMED);
        }
        Table table = gameLogic.deal(playerId);
        RoundState roundState = gameLogic.isBlackJackCombination(table.getPlayerCards()) ?
                RoundState.BLACK_JACK : RoundState.IN_PROGRESS;

        applyRoundState(table, round, roundState, playerId);
        return table;
    }

    /**
     * {@inheritDoc}
     */
    public Table hit(Table table, long playerId, long roundId) {
        Round round = roundDao.getRound(roundId);
        logger.info("Cards during the round: [{}]", round.getCardLogs().size());
        if (round.getStatus() != RoundState.IN_PROGRESS) {
            logger.info("Game status is: {}", round.getStatus());
            throw BlackJackExceptionHelper.newBlackJackException(ErrorCode.HIT_IS_NOT_ALLOWED_HERE);
        }
        Dealer dealer = new Dealer(table);
        dealer.hitPlayer();
        if (gameLogic.isBoosted(table.getPlayerCards())) {
            applyRoundState(table, round, RoundState.BOOST, playerId);
        }
        return table;
    }

    /**
     * {@inheritDoc}
     */
    public Table stand(Table table, long playerId, long roundId) {
        Round round = roundDao.getRound(roundId);
        gameLogic.dealerPlay(table);
        RoundState roundResult = gameLogic.judge(table);
        applyRoundState(table, round, roundResult, playerId);
        return table;
    }

    private void applyRoundState(Table table, Round round, RoundState roundState, long playerId) {
        roundDao.changeRoundState(round, roundState);
        table.setRoundState(roundState);
        if (roundState.isTerminalState()) {
            betService.pay(table, playerId);
        }
    }
}

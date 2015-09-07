package com.apischanskyi.blackjack.service;

import com.apischanskyi.blackjack.exceptions.BlackJackExceptionHelper;
import com.apischanskyi.blackjack.data.declaration.RoundDao;
import com.apischanskyi.blackjack.data.declaration.UserDao;
import com.apischanskyi.blackjack.entity.Round;
import com.apischanskyi.blackjack.entity.Round.RoundState;
import com.apischanskyi.blackjack.entity.User;
import com.apischanskyi.blackjack.game.Deal;
import com.apischanskyi.blackjack.game.logic.GameState;
import com.apischanskyi.blackjack.service.declaration.BetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class BetServiceImpl implements BetService {
    private static final Logger logger = LoggerFactory.getLogger(BetServiceImpl.class);

    @Autowired
    private RoundDao roundDao;

    @Autowired
    private UserDao userDao;

    /**
     * {@inheritDoc}
     */
    public Long bet(long playerId, Long amount) {
        if (amount == null || amount <= 0) {
            throw BlackJackExceptionHelper.newBlackJackException(BlackJackExceptionHelper.ErrorCode.NO_BET_FOUND);
        }
        User user = userDao.getUserById(playerId);
        if (isBetExists(playerId)) {
            throw BlackJackExceptionHelper.newBlackJackException(BlackJackExceptionHelper.ErrorCode.YOU_ALREADY_HAVE_BET);
        }
        if (user.getBalance() < amount) {
            throw BlackJackExceptionHelper.newBlackJackException(BlackJackExceptionHelper.ErrorCode.INSUFFICIENT_BALANCE);
        }
        user.setBalance(user.getBalance() - amount);
        userDao.update(user);
        return roundDao.newRound(new Round(user, amount));
    }

    /**
     * {@inheritDoc}
     */
    public void pay(GameState gameState, long playerId) {
        Deal deal = gameState.getDeal();
        logger.info("Round state is: {} and bet is: {}", deal.getState(), deal.getBet());
        Long amountToPay = PaymentSolver.calcAmountToPay(deal.getState(), deal.getBet());
        User user = userDao.getUserById(playerId);
        logger.info("Payment: {}", amountToPay);
        user.setBalance(user.getBalance() + amountToPay);
        userDao.update(user);
    }

    /**
     * {@inheritDoc}
     */
    public void cancelBet(Long roundId) {
        Round round = roundDao.getRound(roundId);
        if (round.getStatus() == RoundState.READY) {
            roundDao.changeRoundState(round, RoundState.CANCELLED);
        } else {
            throw BlackJackExceptionHelper.newBlackJackException(BlackJackExceptionHelper.ErrorCode.UNABLE_TO_CANCEL_BET);
        }
    }

    private boolean isBetExists(long playerId) {
        List<Round> userRounds = roundDao.getUserRounds(playerId, RoundState.READY, RoundState.IN_PROGRESS);
        logger.info("Number of rounds {}", userRounds.size());
        return !userRounds.isEmpty();
    }

    // as we are storing money in coins there would be never losing of accuracy
    private enum PaymentSolver {
        BLACK_JACK((state) -> state == RoundState.BLACK_JACK, (bet) -> new Double(bet + bet * 1.5).longValue()),
        WIN((state) -> state == RoundState.WIN, (bet) -> bet + bet),
        BOOSTED((state) -> state == RoundState.BOOST, (bet) -> 0L),
        LOOSE((state) -> state == RoundState.LOOSE, (bet) -> 0L),
        PUSH((state) -> state == RoundState.PUSH, (bet) -> bet);

        private Predicate<RoundState> predicate;
        private Function<Long, Long> formula;

        PaymentSolver(Predicate<RoundState> predicate, Function<Long, Long> formula) {
            this.predicate = predicate;
            this.formula = formula;
        }

        public static Long calcAmountToPay(RoundState roundState, Long bet) {
            for (PaymentSolver solver : values()) {
                if (solver.predicate.test(roundState)) {
                    return solver.formula.apply(bet);
                }
            }
            throw BlackJackExceptionHelper.newBlackJackException(BlackJackExceptionHelper.ErrorCode.INCOMPATIBLE_GAME_STATE);
        }

    }
}

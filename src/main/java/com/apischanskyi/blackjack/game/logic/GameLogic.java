package com.apischanskyi.blackjack.game.logic;

import com.apischanskyi.blackjack.entity.Card;
import com.apischanskyi.blackjack.exceptions.BlackJackExceptionHelper;
import com.apischanskyi.blackjack.game.Table;
import com.apischanskyi.blackjack.game.TableContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import static com.apischanskyi.blackjack.entity.Card.Rank;
import static com.apischanskyi.blackjack.entity.Round.RoundState;
import static com.apischanskyi.blackjack.exceptions.BlackJackExceptionHelper.ErrorCode;

@Component
public class GameLogic {

    private static final Logger logger = LoggerFactory.getLogger(GameLogic.class);

    @Autowired
    private TableContainer tableContainer;

    public static final int BLACK_JACK = 21;

    private static final Comparator<Card> cardComparator = (card, card1) -> card1.getRank() == Rank.ACE ? -1 : 1;

    /**
     * Check Black-Jack combination
     *
     * @param cards set of player cards
     * @return true if player reach Black-Jack combination and false otherwise
     */
    public boolean isBlackJackCombination(Collection<Card> cards) {
        return calculatePoints(cards) == BLACK_JACK;
    }

    /**
     * Checks whether player boosted
     *
     * @param cards set of player cards
     * @return true if player boosted and false otherwise
     */
    public boolean isBoosted(Collection<Card> cards) {
        return calculatePoints(cards) > BLACK_JACK;
    }

    /**
     * Performs dealers actions
     *
     * @param playerId instance ID of player
     */
    public Table dealerPlay(long playerId) {
        Table table = tableContainer.getTable(playerId);
        int playerPoints = calculatePoints(table.getPlayerCards());
        int dealerPoints = calculatePoints(table.getDealerCards());
        Dealer dealer = new Dealer(table);
        while ((dealerNeedMoreCards(dealerPoints) && dealerShouldRisk(playerPoints))
                || playerHaveMore(dealerPoints, playerPoints)) {
            logger.info("Dealer points: {}; Player points: {}", dealerPoints, playerPoints);
            dealer.hitDealer();
            dealerPoints = calculatePoints(table.getDealerCards());
        }
        return table;
    }

    /**
     * Determine and set the result of round
     *
     * @param table state of game
     */
    public RoundState judge(Table table) {
        int playerPoints = calculatePoints(table.getPlayerCards());
        int dealerPoints = calculatePoints(table.getDealerCards());
        logger.info("Dealer points: {}; Player points: {}", dealerPoints, playerPoints);
        return GameJudge.test(playerPoints, dealerPoints);
    }

    /**
     * Calculates the amount of points for given combination of cards
     *
     * @param cards set of cards
     * @return amount of points for given combination
     */
    private int calculatePoints(Collection<Card> cards) {
        return cards.stream().sorted(cardComparator)
                .collect(CardConsumer::new, CardConsumer::accept, (cons1, cons2)-> {}).sum;
    }

    /**
     * Performs the deal
     *
     * @param playerId instance ID of round
     * @return new {@link Table} with dealt cards
     */
    public Table deal(long playerId) {
        Table table = tableContainer.getTable(playerId);
        Dealer dealer = new Dealer(table);
        dealer.deal();
        return table;
    }

    public Table hit(long playerId) {
        Table table = tableContainer.getTable(playerId);
        Dealer dealer = new Dealer(table);
        dealer.hitPlayer();
        return table;
    }

    private boolean dealerShouldRisk(int playerPoints) {
        return playerPoints >= 17;
    }

    private boolean dealerNeedMoreCards(int dealerPoints) {
        return dealerPoints <= 17;
    }

    private boolean playerHaveMore(int dealerPoints, int playerPoints) {
        return dealerPoints < playerPoints;
    }

    /**
     * Performs judgment
     */
    private enum GameJudge {
        PUSH(Integer::equals, RoundState.PUSH),
        WIN((playerPoints, dealerPoints) -> playerPoints > dealerPoints || dealerPoints > BLACK_JACK, RoundState.WIN),
        LOOSE((playerPoints, dealerPoints) -> playerPoints < dealerPoints && dealerPoints <= BLACK_JACK, RoundState.LOOSE);

        BiPredicate<Integer, Integer> predicate;
        RoundState roundState;

        GameJudge(BiPredicate<Integer, Integer> predicate, RoundState roundState) {
            this.predicate = predicate;
            this.roundState = roundState;
        }

        private static RoundState test(int playerPoints, int dealerPoints) {
            for (GameJudge judge : values()) {
                if (judge.predicate.test(playerPoints, dealerPoints)) {
                    return judge.roundState;
                }
            }
            throw BlackJackExceptionHelper.newBlackJackException(ErrorCode.INCOMPATIBLE_GAME_STATE);
        }

    }

    private class CardConsumer implements Consumer<Card> {

        int sum = 0;

        @Override
        public void accept(Card card) {
            Rank rank = card.getRank();
            if (rank == Rank.ACE && sum > 10) {
                sum += rank.getAlterCost();
            } else {
                sum += rank.getCost();
            }
        }
    }

}

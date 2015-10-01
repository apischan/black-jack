package com.apischanskyi.blackjack.service;

import com.apischanskyi.blackjack.data.declaration.CardLogDao;
import com.apischanskyi.blackjack.entity.Card;
import com.apischanskyi.blackjack.entity.CardLog;
import com.apischanskyi.blackjack.entity.Round;
import com.apischanskyi.blackjack.game.Deal;
import com.apischanskyi.blackjack.service.declaration.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.apischanskyi.blackjack.entity.CardLog.PlayerRole;

@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Autowired
    private CardLogDao cardLogDao;

    @Override
    public void logCards(Round round, Deal deal) {
        logger.info("Logging the cards...");
        Map<PlayerRole, LinkedHashSet<Card>> roundCards = deal.getRoundCards();
        for (PlayerRole playerRole : roundCards.keySet()) {
            addCardLogs(round, roundCards.get(playerRole), playerRole);
        }
    }

    private void addCardLogs(Round round, Set<Card> roundCards, PlayerRole playerRole) {
        Set<Card> newCards = getNewCards(round.getCardLogs(), roundCards);
        for (Card card : newCards) {
            CardLog cardLog = new CardLog(playerRole, card, round);
            cardLogDao.save(cardLog);
            round.getCardLogs().add(cardLog);
        }
    }

    /**
     * Filter only new cards
     *
     * @param cardLogs instances of {@link Card} that contains logged cards
     * @param roundCards set of current cards of one of players
     * @return cards that given to player in current hit/deal
     */
    private Set<Card> getNewCards(Set<CardLog> cardLogs, Set<Card> roundCards) {
        Set<Card> existingCards = cardLogs.stream()
                .map(CardLog::getCard)
                .collect(Collectors.toSet());
        Set<Card> result = new HashSet<>(roundCards);
        result.removeAll(existingCards);
        return result;
    }
}

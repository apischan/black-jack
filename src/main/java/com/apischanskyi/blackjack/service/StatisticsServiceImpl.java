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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        Map<PlayerRole, LinkedList<Card>> roundCards = deal.getRoundCards();
        for (PlayerRole playerRole : roundCards.keySet()) {
            addCardLogs(round, roundCards.get(playerRole), playerRole);
        }
    }

    private void addCardLogs(Round round, List<Card> cards, PlayerRole playerRole) {
        for (Card card : cards) {
            CardLog cardLog = new CardLog(playerRole, card, round);
            cardLogDao.save(cardLog);
            logger.info("Card log ID=[{}]", cardLog.getId());
            round.getCardLogs().add(cardLog);
        }
    }
}

package com.apischanskyi.blackjack;

import com.apischanskyi.blackjack.exceptions.BlackJackExceptionHelper;
import com.apischanskyi.blackjack.game.Table;
import com.apischanskyi.blackjack.service.declaration.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.apischanskyi.blackjack.Constants.SiteConstants;

@RestController
@RequestMapping(value = SiteConstants.GAME_CONTROLLER_ROOT
        , produces = MediaType.APPLICATION_JSON_VALUE
        , consumes = MediaType.APPLICATION_JSON_VALUE)
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private static final String ROUND_STATE = "roundState";

    @Autowired
    private GameService gameService;

    @RequestMapping(value = SiteConstants.DEAL, method = RequestMethod.POST)
    public Table deal(@PathVariable("playerId") long playerId, @PathVariable("roundId") long gameId,
                     HttpSession session) {
        logger.info("Player id: {}; roundId: {}", playerId, gameId);
        Table table = gameService.deal(playerId, gameId);
        session.setAttribute(ROUND_STATE, table);
        return table.hideDealerCard();
//        System.out.println("World!!");
//        return new Table(0L, 0L);
    }
    @RequestMapping(value = SiteConstants.HIT, method = RequestMethod.POST)
    public Table hit(@PathVariable("playerId") long playerId, @PathVariable("roundId") long gameId,
                    HttpSession session) {
        logger.info("Player id: {}; roundId: {}", playerId, gameId);
        Table table = (Table) session.getAttribute(ROUND_STATE);
        Table newTable = gameService.hit(table, playerId, gameId);
        session.setAttribute(ROUND_STATE, newTable);
        return table.hideDealerCard();
    }

    @RequestMapping(value = SiteConstants.STAND, method = RequestMethod.POST)
    public Table stand(@PathVariable("playerId") long playerId, @PathVariable("roundId") long gameId,
                    HttpSession session) {
        logger.info("Player id: {}; roundId: {}", playerId, gameId);
        Table table = (Table) session.getAttribute(ROUND_STATE);
        Table newState = gameService.stand(table, playerId, gameId);
        session.setAttribute(ROUND_STATE, null);
        return newState;
    }

    @RequestMapping(value = SiteConstants.RESULT, method = RequestMethod.POST)
    public Table result(HttpSession session) {
        Table table = (Table) session.getAttribute(ROUND_STATE);
        if (table == null) {
            throw BlackJackExceptionHelper.newBlackJackException(BlackJackExceptionHelper.ErrorCode.GAME_NOT_FOUND);
        } else {
            session.setAttribute(ROUND_STATE, null);
        }
        return table;
    }

}

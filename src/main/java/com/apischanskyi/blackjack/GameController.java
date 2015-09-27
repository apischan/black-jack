package com.apischanskyi.blackjack;

import com.apischanskyi.blackjack.exceptions.BlackJackExceptionHelper;
import com.apischanskyi.blackjack.game.logic.GameState;
import com.apischanskyi.blackjack.service.declaration.GameService;
import com.apischanskyi.blackjack.game.Deal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public Deal deal(@PathVariable("playerId") long playerId, @PathVariable("roundId") long gameId,
                     HttpSession session) {
        logger.info("Player id: {}; roundId: {}", playerId, gameId);
        GameState gameState = gameService.deal(playerId, gameId);
        session.setAttribute(ROUND_STATE, gameState);
        return gameState.getDeal().hideDealerCard();
    }

    @RequestMapping(value = SiteConstants.HIT, method = RequestMethod.POST)
    public Deal hit(@PathVariable("playerId") long playerId, @PathVariable("roundId") long gameId,
                    HttpSession session) {
        logger.info("Player id: {}; roundId: {}", playerId, gameId);
        GameState gameState = (GameState) session.getAttribute(ROUND_STATE);
        GameState newGameState = gameService.hit(gameState, playerId, gameId);
        session.setAttribute(ROUND_STATE, newGameState);
        return gameState.getDeal().hideDealerCard();
    }

    @RequestMapping(value = SiteConstants.STAND, method = RequestMethod.POST)
    public Deal stand(@PathVariable("playerId") long playerId, @PathVariable("roundId") long gameId,
                    HttpSession session) {
        logger.info("Player id: {}; roundId: {}", playerId, gameId);
        GameState gameState = (GameState) session.getAttribute(ROUND_STATE);
        GameState newState = gameService.stand(gameState, playerId, gameId);
        session.setAttribute(ROUND_STATE, null);
        return newState.getDeal();
    }

    @RequestMapping(value = SiteConstants.RESULT, method = RequestMethod.POST)
    public Deal result(HttpSession session) {
        GameState gameState = (GameState) session.getAttribute(ROUND_STATE);
        if (gameState == null) {
            throw BlackJackExceptionHelper.newBlackJackException(BlackJackExceptionHelper.ErrorCode.GAME_NOT_FOUND);
        } else {
            session.setAttribute(ROUND_STATE, null);
        }
        return gameState.getDeal();
    }

}

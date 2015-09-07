package com.apischanskyi.blackjack;

import com.apischanskyi.blackjack.Constants.SiteConstants;
import com.apischanskyi.blackjack.service.declaration.BetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.apischanskyi.blackjack.Constants.UrlKeyConstants.ROUND_ID;
import static com.apischanskyi.blackjack.Constants.UrlKeyConstants.USER_ID;

@RestController
@RequestMapping(value = SiteConstants.BET_CONTROLLER_ROOT
        , produces = MediaType.APPLICATION_JSON_VALUE
        , consumes = MediaType.APPLICATION_JSON_VALUE)
public class BetController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private BetService betService;

    @RequestMapping(value = SiteConstants.BET, method = RequestMethod.POST)
    public Long bet(@PathVariable(USER_ID) long playerId, @RequestBody Long amount) {
        logger.info("Input parameters: [{}, {}]", playerId, amount);
        return betService.bet(playerId, amount * 100);
    }

    @RequestMapping(value = SiteConstants.CANCEL_BET, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void betCancel(@PathVariable(USER_ID) long playerId,
                          @PathVariable(ROUND_ID) long roundId) {
        betService.cancelBet(roundId);
    }
}

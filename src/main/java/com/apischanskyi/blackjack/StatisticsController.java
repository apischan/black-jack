package com.apischanskyi.blackjack;

import com.apischanskyi.blackjack.service.declaration.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.apischanskyi.blackjack.Constants.SiteConstants;
import static com.apischanskyi.blackjack.Constants.UrlKeyConstants.ROUND_ID;

@RestController
@RequestMapping(value = SiteConstants.GAME_STATISTICS_ROOT
        , produces = MediaType.APPLICATION_JSON_VALUE
        , consumes = MediaType.APPLICATION_JSON_VALUE)
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value = SiteConstants.ROUND_STATISTICS, method = RequestMethod.POST)
    public void betCancel(@PathVariable(ROUND_ID) long roundId) {

    }

}

package com.apischanskyi.blackjack.service.declaration;

import com.apischanskyi.blackjack.entity.Round;
import com.apischanskyi.blackjack.game.Deal;

public interface StatisticsService {

    void logCards(Round round, Deal deal);
    
}

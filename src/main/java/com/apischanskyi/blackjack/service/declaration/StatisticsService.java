package com.apischanskyi.blackjack.service.declaration;

import com.apischanskyi.blackjack.entity.Round;
import com.apischanskyi.blackjack.game.Table;

public interface StatisticsService {

    void logCards(Round round, Table table);
    
}

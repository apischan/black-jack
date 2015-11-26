package com.apischanskyi.blackjack.game;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TableContainer {

    private Map<Long, Table> userTables = new ConcurrentHashMap<>();

    public void addUserTable(Table table, Long userId) {
        userTables.put(userId, table);
    }

    public Table getTable(Long userId) {
        return userTables.get(userId);
    }

}

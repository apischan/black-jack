package com.apischanskyi.blackjack;

import org.hibernate.cfg.Environment;

public interface Constants {

    interface HibernateConstants {
        String HIBERNATE_DRIVER_PROP_NAME       = "hibernate.driver";
        String HIBERNATE_DB_URL_PROP_NAME       = "hibernate.db.url";
        String HIBERNATE_DB_USERNAME_PROP_NAME  = "hibernate.username";
        String HIBERNATE_DB_PASSWORD_PROP_NAME  = "hibernate.password";
        String HIBERNATE_DIALECT_PROP_NAME      = "hibernate.dialect";
        String HIBERNATE_SHOW_SQL_PROP_NAME     = "hibernate.show_sql";
        String HIBERNATE_HBM2DDL_AUTO_PROP_NAME = "hibernate.hbm2ddl.auto";


        String HIBERNATE_DIALECT  = Environment.DIALECT;
        String HIBERNATE_SHOW_SQL = Environment.SHOW_SQL;
    }

    interface UrlKeyConstants {
        String USER_ID = "playerId";
        String ROUND_ID = "roundId";
    }

    interface SiteConstants {
        String GAME_CONTROLLER_ROOT = "/game/{playerId}/{roundId}";
        String DEAL = "/deal";
        String HIT = "/hit";
        String STAND = "/stand";
        String RESULT = "/result";

        String BET_CONTROLLER_ROOT = "/game/{playerId}";
        String BET = "/bet";
        String CANCEL_BET = "/bet/{roundId}/cancel";
    }

}

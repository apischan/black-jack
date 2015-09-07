package com.apischanskyi.blackjack.config;

import com.apischanskyi.blackjack.data.impl.HibernateRoundDao;
import com.apischanskyi.blackjack.data.impl.HibernateUserDao;
import com.apischanskyi.blackjack.service.BetServiceImpl;
import com.apischanskyi.blackjack.service.GameServiceImpl;
import com.apischanskyi.blackjack.service.declaration.GameService;
import com.apischanskyi.blackjack.data.declaration.RoundDao;
import com.apischanskyi.blackjack.data.declaration.UserDao;
import com.apischanskyi.blackjack.service.declaration.BetService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"prod", "dev"})
@Configuration
public class CustomBeansConfig {

    // services
    @Bean
    public GameService gameService() {
        return new GameServiceImpl();
    }

    @Bean
    public BetService betService() {
        return new BetServiceImpl();
    }

    // DAOs
    @Bean
    public UserDao userDao() {
        return new HibernateUserDao();
    }

    @Bean
    public RoundDao roundDao() {
        return new HibernateRoundDao();
    }

}

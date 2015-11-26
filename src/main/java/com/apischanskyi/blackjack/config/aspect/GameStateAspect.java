package com.apischanskyi.blackjack.config.aspect;

import com.apischanskyi.blackjack.game.Table;
import com.apischanskyi.blackjack.game.TableContainer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class GameStateAspect implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Autowired
    private TableContainer tableContainer;

    @Around("execution(* com.apischanskyi.blackjack.BetController.bet(..))")
    public Object createNewObjectsForGame(ProceedingJoinPoint jp) throws Throwable {
        Long playerId = (Long) jp.getArgs()[0];
        Long betAmount = (Long) jp.getArgs()[1];
        Long roundId = (Long) jp.proceed();

        Table table = beanFactory.getBean(Table.class, roundId, betAmount);
        System.out.println(table.getDeck());
        tableContainer.addUserTable(table, playerId);
        System.out.println("Hello!!");
//        System.out.println("Bet: " + table.getBet());
        return roundId;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}

package com.apischanskyi.spring.blackjack.service;

import com.apischanskyi.blackjack.data.declaration.RoundDao;
import com.apischanskyi.blackjack.data.declaration.UserDao;
import com.apischanskyi.blackjack.entity.Round;
import com.apischanskyi.blackjack.entity.Round.RoundState;
import com.apischanskyi.blackjack.entity.User;
import com.apischanskyi.blackjack.exceptions.BlackJackException;
import com.apischanskyi.blackjack.service.BetServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class BetServiceUnitTest {

    @InjectMocks
    BetServiceImpl betService;

    @Mock
    RoundDao roundDaoMock;

    @Mock
    UserDao userDaoMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void bet_betSuccessCase() {
        System.out.println("hello");
        long userId = 1L;
        Long bet = 100L;
        Long expectedRoundId = 2L;
        User user = new User();
        user.setId(userId);
        user.setBalance(1000L);

        doReturn(user).when(userDaoMock).getUserById(userId);
        doNothing().when(userDaoMock).update(user);
        doReturn(expectedRoundId).when(roundDaoMock).newRound(any(Round.class));

        Long actualRoundId = betService.bet(userId, bet);

        assertEquals(actualRoundId, expectedRoundId);
        assertTrue(user.getBalance() == 1000L - bet);
    }

    @Test(expected = BlackJackException.class)
    public void bet_insufficientBet() {
        betService.bet(1L, 0L);
    }

    @Test(expected = BlackJackException.class)
    public void bet_roundAlreadyStarted() {
        long userId = 1L;
        Long bet = 100L;
        User user = new User();
        user.setId(userId);

        doReturn(user).when(userDaoMock).getUserById(userId);
        doReturn(Collections.singletonList(new Round())).when(roundDaoMock)
                .getUserRounds(userId, RoundState.READY, RoundState.IN_PROGRESS);

        betService.bet(userId, bet);
    }

    @Test(expected = BlackJackException.class)
    public void bet_insufficientBalance() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setBalance(10L);
        Long bet = 100L;

        doReturn(user).when(userDaoMock).getUserById(userId);

        betService.bet(userId, bet);
    }

}

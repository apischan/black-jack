package com.apischanskyi.spring.blackjack.service;

import com.apischanskyi.blackjack.data.declaration.RoundDao;
import com.apischanskyi.blackjack.data.declaration.UserDao;
import com.apischanskyi.blackjack.entity.Round.RoundState;
import com.apischanskyi.blackjack.entity.User;
import com.apischanskyi.blackjack.game.Deal;
import com.apischanskyi.blackjack.game.logic.GameState;
import com.apischanskyi.blackjack.service.BetServiceImpl;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(Theories.class)
public class BetServiceTheoriesTest {

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

    @DataPoints
    public static RoundStatePaymentPair[] data() {
        return new RoundStatePaymentPair[] {
                new RoundStatePaymentPair(RoundState.BLACK_JACK, 25L),
                new RoundStatePaymentPair(RoundState.WIN, 20L),
                new RoundStatePaymentPair(RoundState.LOOSE, 0L),
                new RoundStatePaymentPair(RoundState.BOOST, 0L),
                new RoundStatePaymentPair(RoundState.PUSH, 10L)
        };
    }

    @Theory
    public void pay(final RoundStatePaymentPair testData) {
        long userId = 1L;
        long roundId = 1L;
        User user = new User();
        user.setId(userId);
        user.setBalance(1000L);
        Long bet = 10L;

        Deal deal = new Deal(roundId, bet);
        deal.setRoundState(testData.roundState);
        GameState gameState = mock(GameState.class);
        doReturn(deal).when(gameState).getDeal();
        doReturn(user).when(userDaoMock).getUserById(userId);

        betService.pay(gameState, userId);

        assertEquals(new Long(1000L + testData.payment), user.getBalance());
    }

    public static class RoundStatePaymentPair {
        RoundState roundState;
        Long payment;

        public RoundStatePaymentPair(RoundState roundState, Long payment) {
            this.roundState = roundState;
            this.payment = payment;
        }
    }

}

package com.apischanskyi.spring.blackjack.service

import com.apischanskyi.blackjack.data.declaration.RoundDao
import com.apischanskyi.blackjack.entity.Card
import com.apischanskyi.blackjack.entity.Round
import com.apischanskyi.blackjack.entity.Round.RoundState
import com.apischanskyi.blackjack.exceptions.BlackJackException
import com.apischanskyi.blackjack.exceptions.BlackJackExceptionHelper.ErrorCode
import com.apischanskyi.blackjack.game.Deal
import com.apischanskyi.blackjack.game.logic.GameLogic
import com.apischanskyi.blackjack.game.logic.GameState
import com.apischanskyi.blackjack.service.GameServiceImpl
import com.apischanskyi.blackjack.service.declaration.BetService
import com.apischanskyi.blackjack.service.declaration.GameService
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class GameServiceTestCase extends Specification {

    RoundDao roundDaoMock = Mock(RoundDao)
    BetService betServiceMock = Mock(BetService)
    GameLogic gameLogicMock = Spy(GameLogic)
    GameService gameService = new GameServiceImpl(roundDao: roundDaoMock, betService: betServiceMock, gameLogic: gameLogicMock)

    @Shared def blackJackCombination = [
            new Card(Card.Suit.CLUBS, Card.Rank.ACE),
            new Card(Card.Suit.HEARTS, Card.Rank.JACK)]

    @Shared def boostCombination = [
            new Card(Card.Suit.SPADES, Card.Rank.KING),
            new Card(Card.Suit.HEARTS, Card.Rank.TWO),
            new Card(Card.Suit.HEARTS, Card.Rank.JACK)]

    @Shared def nonBlackJackCombination = [
            new Card(Card.Suit.SPADES, Card.Rank.KING),
            new Card(Card.Suit.HEARTS, Card.Rank.EIGHT),
            new Card(Card.Suit.HEARTS, Card.Rank.TWO)]

    @Shared def lowPointsCombination = [
            new Card(Card.Suit.SPADES, Card.Rank.EIGHT),
            new Card(Card.Suit.HEARTS, Card.Rank.EIGHT)]

    @Unroll
    def "deal parametrized test"(def userCardCombination, def expectedGameResult, def timesToPay) {
        given:
        def userId = 1L
        def roundId = 1L
        def bet = 10L
        Round round = new Round(id: roundId, status: RoundState.READY, bet: bet);
        Deal dealMock = Spy(Deal, constructorArgs: [roundId, bet])
        GameState gameStateMock = Mock(GameState, constructorArgs: [dealMock])

        when:
        GameState gameStateResult = gameService.deal(userId, roundId)
        def gameResult = gameStateResult.getDeal().getState()

        then:
        1 * roundDaoMock.getRound(roundId) >> round
        1 * gameLogicMock.deal(userId, bet) >> gameStateMock
        _ * gameStateMock.getDeal() >> dealMock

        _ * dealMock.getDealerCards() >> nonBlackJackCombination
        _ * dealMock.getPlayerCards() >> userCardCombination

        timesToPay * betServiceMock.pay(gameStateMock, userId)

        _ * roundDaoMock.changeRoundState(round, expectedGameResult)
        _ * dealMock.setRoundState(expectedGameResult)

        expect:
        gameResult == expectedGameResult

        where:
        userCardCombination     | expectedGameResult     | timesToPay
        blackJackCombination    | RoundState.BLACK_JACK  | 1
        nonBlackJackCombination | RoundState.IN_PROGRESS | 0
    }

    def "deal with exception"() {
        given:
        def userId = 1L
        def roundId = 1L
        Round round = new Round(status: RoundState.IN_PROGRESS)

        when:
        gameService.deal(userId, roundId)

        then:
        1 * roundDaoMock.getRound(roundId) >> round
        BlackJackException e = thrown()
        e.getErrorCode() == ErrorCode.DEAL_HAS_BEEN_PERFORMED
    }

    def "hit success"() {
        given:
        def userId = 1L
        def roundId = 1L
        def bet = 10L
        Round round = new Round(status: RoundState.IN_PROGRESS)
        Deal dealMock = Mock(Deal, constructorArgs: [roundId, bet])
        GameState gameStateMock = Mock(GameState, constructorArgs: [dealMock])

        when:
        gameService.hit(gameStateMock, userId, roundId)

        then:
        1 * roundDaoMock.getRound(roundId) >> round
        1 * gameStateMock.hitPlayer()
        1 * gameStateMock.getDeal() >> dealMock
        1 * dealMock.getPlayerCards() >> nonBlackJackCombination
    }

    def "hit boosted"() {
        given:
        def userId = 1L
        def roundId = 1L
        def bet = 10L
        Round round = new Round(status: RoundState.IN_PROGRESS)
        Deal dealMock = Mock(Deal, constructorArgs: [roundId, bet])
        GameState gameStateMock = Mock(GameState, constructorArgs: [dealMock])

        when:
        gameService.hit(gameStateMock, userId, roundId)

        then:
        1 * roundDaoMock.getRound(roundId) >> round
        1 * roundDaoMock.changeRoundState(round, RoundState.BOOST)
        1 * gameStateMock.hitPlayer()
        2 * gameStateMock.getDeal() >> dealMock
        1 * dealMock.setRoundState(RoundState.BOOST)
        1 * betServiceMock.pay(gameStateMock, userId)
        1 * dealMock.getPlayerCards() >> boostCombination
    }

    def "hit wrong round state"() {
        given:
        def userId = 1L
        def roundId = 1L
        def bet = 10L
        Round round = new Round(status: RoundState.READY)
        GameState gameState = new GameState(new Deal(roundId, bet))

        when:
        gameService.hit(gameState, userId, roundId)

        then:
        1 * roundDaoMock.getRound(roundId) >> round
        BlackJackException e = thrown()
        e.getErrorCode() == ErrorCode.HIT_IS_NOT_ALLOWED_HERE
    }

    @Unroll
    def "stand success"(def playerCards, def dealerCards, def roundState) {
        given:
        def userId = 1L
        def roundId = 1L
        def bet = 10L
        Round round = new Round(status: RoundState.IN_PROGRESS)
        Deal dealMock = Spy(Deal, constructorArgs: [roundId, bet])
        GameState gameStateMock = Mock(GameState, constructorArgs: [dealMock])
        gameLogicMock.dealerPlay(gameStateMock) >> {}

        when:
        gameService.stand(gameStateMock, userId, roundId)

        then:
        1 * roundDaoMock.getRound(roundId) >> round
        _ * gameStateMock.getDeal() >> dealMock
        1 * dealMock.getPlayerCards() >> playerCards
        1 * dealMock.getDealerCards() >> dealerCards
        1 * roundDaoMock.changeRoundState(round, roundState)
        1 * betServiceMock.pay(gameStateMock, userId)

        expect:
        dealMock.getState() == roundState

        where:
        playerCards             | dealerCards             || roundState
        nonBlackJackCombination | nonBlackJackCombination || RoundState.PUSH
        nonBlackJackCombination | lowPointsCombination    || RoundState.WIN
        lowPointsCombination    | nonBlackJackCombination || RoundState.LOOSE
    }
}

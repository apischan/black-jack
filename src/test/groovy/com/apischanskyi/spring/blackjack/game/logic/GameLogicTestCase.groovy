package com.apischanskyi.spring.blackjack.game.logic

import com.apischanskyi.blackjack.entity.Card
import com.apischanskyi.blackjack.entity.Card.Rank
import com.apischanskyi.blackjack.entity.Card.Suit
import com.apischanskyi.blackjack.entity.Round.RoundState
import com.apischanskyi.blackjack.game.Deal
import com.apischanskyi.blackjack.game.logic.GameLogic
import com.apischanskyi.blackjack.game.logic.Table
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class GameLogicTestCase extends Specification {

    GameLogic gameLogic = new GameLogic()

    @Shared def blackJackCombination = [
            new Card(Suit.CLUBS, Rank.ACE),
            new Card(Suit.HEARTS, Rank.JACK)]

    @Shared def boostCombination = [
            new Card(Suit.SPADES, Rank.KING),
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.HEARTS, Rank.JACK)]

    @Shared def nonBlackJackCombination = [
            new Card(Suit.SPADES, Rank.KING),
            new Card(Suit.HEARTS, Rank.EIGHT),
            new Card(Suit.HEARTS, Rank.TWO)]

    @Shared def lowPointsCombination = [
            new Card(Suit.SPADES, Rank.EIGHT),
            new Card(Suit.HEARTS, Rank.EIGHT)]


    @Unroll
    def "isBlackJackCombination"(def cards, def expectedResult) {
        when:
        boolean result = gameLogic.isBlackJackCombination(cards)

        then:
        result == expectedResult

        where:
        cards                   | expectedResult
        blackJackCombination    | true
        nonBlackJackCombination | false
        lowPointsCombination    | false
        boostCombination        | false
    }

    @Unroll
    def "isBoostedCombination"(def cards, def expectedResult) {
        when:
        boolean result = gameLogic.isBoosted(cards)

        then:
        result == expectedResult

        where:
        cards                   | expectedResult
        blackJackCombination    | false
        nonBlackJackCombination | false
        lowPointsCombination    | false
        boostCombination        | true
    }

    @Unroll
    def "dealerPlay need more cards" (def initDealerCards, def timesToHit) {
        given:
        def roundId = 1L
        def bet = 10L
        Deal dealMock = Spy(Deal, constructorArgs: [roundId, bet])
        Table gameStateMock = Spy(Table, constructorArgs: [dealMock])
        def dealerCards = initDealerCards.collect()

        when:
        gameLogic.dealerPlay(gameStateMock)

        then:
        1 * gameStateMock.getDeal() >> dealMock
        1 * dealMock.getPlayerCards() >> nonBlackJackCombination
        _ * dealMock.getDealerCards() >> dealerCards
        timesToHit * gameStateMock.hitDealer() >> {dealerCards << new Card(Suit.CLUBS, Rank.TWO)}

        where:
        initDealerCards         | timesToHit
        nonBlackJackCombination | 0
        lowPointsCombination    | 2
    }

    def "deal common case" () {
        given:
        def roundId = 1L
        def bet = 10L

        when:
        Table gameState = gameLogic.deal(roundId, bet)
        Deal deal = gameState.getDeal()

        then:
        assert deal.getDealerCards().size() == 2
        assert deal.getPlayerCards().size() == 2
        assert deal.getBet() == bet
    }

    def "deal with hidden card" () {
        given:
        def roundId = 1L
        def bet = 10L

        when:
        Table gameState = gameLogic.deal(roundId, bet)
        Deal deal = gameState.getDeal()
        Card hiddenCard = deal.hideDealerCard().getDealerCards()[1]

        then:
        assert hiddenCard.rank == Rank.HIDDEN
        assert hiddenCard.suit == Suit.HIDDEN
    }

    @Unroll
    def "judge parametrized"(def playerCards, def dealerCards, def expectedRoundState) {
        given:
        def roundId = 1L
        def bet = 10L
        Deal dealMock = Mock(Deal, constructorArgs: [roundId, bet])
        Table gameStateMock = new Table(dealMock)

        when:
        RoundState roundState = gameLogic.judge(gameStateMock)

        then:
        1 * dealMock.getPlayerCards() >> playerCards
        1 * dealMock.getDealerCards() >> dealerCards

        expect:
        expectedRoundState == roundState

        where:
        playerCards             | dealerCards             || expectedRoundState
        nonBlackJackCombination | nonBlackJackCombination || RoundState.PUSH
        lowPointsCombination    | nonBlackJackCombination || RoundState.LOOSE
        nonBlackJackCombination | lowPointsCombination    || RoundState.WIN
    }

}

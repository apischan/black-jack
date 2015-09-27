package com.apischanskyi.blackjack.data.impl;

import com.apischanskyi.blackjack.exceptions.BlackJackExceptionHelper;
import com.apischanskyi.blackjack.data.HibernateUtils;
import com.apischanskyi.blackjack.data.declaration.RoundDao;
import com.apischanskyi.blackjack.entity.Round;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

import static com.apischanskyi.blackjack.entity.Round.RoundState;

@Repository
public class HibernateRoundDao implements RoundDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public Long newRound(Round round) {
        return (Long) hibernateTemplate.save(round);
    }

    public Round getRound(long roundId) {
        Criteria criteria = HibernateUtils.createCriteria(hibernateTemplate, Round.class);
        Round round = (Round) criteria.add(Restrictions.eq("id", roundId)).uniqueResult();
        if (round == null) {
            throw BlackJackExceptionHelper.newBlackJackException(BlackJackExceptionHelper.ErrorCode.GAME_NOT_FOUND);
        }
        return round;
    }

    public void changeRoundState(Round round, RoundState roundState) {
        round.setStatus(roundState);
        hibernateTemplate.save(round);
    }

    public List<Round> getUserRounds(long playerId, RoundState ... states) {
        Criteria criteria = HibernateUtils.createCriteria(hibernateTemplate, Round.class, "round");
        criteria.createAlias("round.user", "user");
//        criteria.createAlias("user.id", "id");
        criteria.add(Restrictions.in("status", Arrays.asList(states)));
        criteria.add(Restrictions.eq("user.id", playerId));
        return criteria.list();
    }

    @Override
    public void update(Round round) {
        hibernateTemplate.update(round);
    }

}

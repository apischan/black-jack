package com.apischanskyi.blackjack.data.impl;

import com.apischanskyi.blackjack.exceptions.BlackJackExceptionHelper;
import com.apischanskyi.blackjack.data.HibernateUtils;
import com.apischanskyi.blackjack.data.declaration.UserDao;
import com.apischanskyi.blackjack.entity.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateUserDao implements UserDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public User getUserById(long playerId) {
        Criteria criteria = HibernateUtils.createCriteria(hibernateTemplate, User.class);
        User user = (User) criteria.add(Restrictions.eq("id", playerId)).uniqueResult();
        if (user == null) {
            throw BlackJackExceptionHelper.newBlackJackException(BlackJackExceptionHelper.ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    public void update(User user) {
        hibernateTemplate.update(user);
    }
}

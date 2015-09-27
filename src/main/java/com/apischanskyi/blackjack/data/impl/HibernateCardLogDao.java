package com.apischanskyi.blackjack.data.impl;

import com.apischanskyi.blackjack.data.declaration.CardLogDao;
import com.apischanskyi.blackjack.entity.CardLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateCardLogDao implements CardLogDao {

    @Autowired
    public HibernateTemplate hibernateTemplate;


    @Override
    public Long save(CardLog cardLog) {
        return (Long) hibernateTemplate.save(cardLog);
    }
}

package com.apischanskyi.blackjack.data;

import org.hibernate.Criteria;
import org.springframework.orm.hibernate4.HibernateTemplate;

public final class HibernateUtils {

    public static Criteria createCriteria(HibernateTemplate hibernateTemplate, Class clazz) {
        return hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(clazz);
    }

    public static Criteria createCriteria(HibernateTemplate hibernateTemplate, Class clazz, String alias) {
        return hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(clazz, alias);
    }
}

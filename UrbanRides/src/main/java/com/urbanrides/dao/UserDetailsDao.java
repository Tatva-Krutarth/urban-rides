package com.urbanrides.dao;

import com.urbanrides.model.UserDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserDetailsDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public void saveUserDetails(UserDetails userDetails) {
        this.hibernateTemplate.save(userDetails);
    }

    @Transactional
    public UserDetails getUserDetailsById(int id) {
        Session session = getCurrentSession();
        String hql = "FROM UserDetails WHERE userDetailsId = :id";
        Query<UserDetails> query = session.createQuery(hql, UserDetails.class);
        query.setParameter("id", id);
        List<UserDetails> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Transactional
    public UserDetails getUserDetailsByUserId(int id) {
        Session session = getCurrentSession();
        String hql = "FROM UserDetails WHERE user.userId = :id";
        Query<UserDetails> query = session.createQuery(hql, UserDetails.class);
        query.setParameter("id", id);
        List<UserDetails> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Transactional
    public void updateUserDetails(UserDetails userDetails) {
        this.hibernateTemplate.update(userDetails);
    }
}

package com.urbanrides.dao;

import com.urbanrides.model.OtpLogs;
import com.urbanrides.model.SupportTypeLogs;
import com.urbanrides.model.User;
import lombok.extern.flogger.Flogger;
import org.codehaus.jackson.JsonToken;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsersDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public int saveUser(User user) {
        Integer id = (Integer) this.hibernateTemplate.save(user);
        return id;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public void updateUser(User user) {
        this.hibernateTemplate.update(user);
    }

    @Transactional
    public User getUserByEmail(String email) {
        Session s = sessionFactory.openSession();
        try {
            String sm = "FROM User WHERE email = :email ";
            Query qq = s.createQuery(sm);
            qq.setParameter("email", email);
            List<User> list = qq.list();
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            } else {
                return null;
            }
        } finally {
            s.close();
        }
    }

    @Transactional
    public User getUserByUserId(int id) {
        Session s = sessionFactory.openSession();
        String queryString = "FROM User WHERE userId = :id ";
        Query<User> query = s.createQuery(queryString, User.class);
        query.setParameter("id", id);
        List<User> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Transactional
    public int totalCount() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(*) FROM User";
        Query<Long> query = session.createQuery(hql, Long.class);
        int count = query.uniqueResult().intValue();
        return count;
    }

    @Transactional
    public List<User> getAllUsers() {
        Session s = getCurrentSession();
        String queryString = "FROM User";
        Query<User> query = s.createQuery(queryString, User.class);
        List<User> results = query.getResultList();
        return results;
    }

    @Transactional
    public List<User> getAllUsersUnblockedUser() {
        Session s = getCurrentSession();
        String queryString = "FROM User where accountStatus != 6 AND accountType != 1";
        Query<User> query = s.createQuery(queryString, User.class);
        List<User> results = query.getResultList();
        return results;
    }

    @Transactional
    public List<User> getRiderUsersUnblockedUser() {
        Session s = getCurrentSession();
        String queryString = "FROM User where accountStatus != 6 AND accountType = 3";
        Query<User> query = s.createQuery(queryString, User.class);
        List<User> results = query.getResultList();
        return results;
    }

    @Transactional
    public List<User> getCaptainUsersUnblockedUser() {
        Session s = getCurrentSession();
        String queryString = "FROM User where accountStatus != 6 AND accountType = 2";
        Query<User> query = s.createQuery(queryString, User.class);
        List<User> results = query.getResultList();
        return results;
    }

    @Transactional
    public List<User> getAdminUsersUnblockedUser() {
        Session s = getCurrentSession();
        String queryString = "FROM User where accountStatus != 6 AND accountType = 1";
        Query<User> query = s.createQuery(queryString, User.class);
        List<User> results = query.getResultList();
        return results;
    }

    @Transactional
    public List<User> getUsersBlockedUser() {
        Session s = getCurrentSession();
        String queryString = "FROM User where accountStatus = 6";
        Query<User> query = s.createQuery(queryString, User.class);
        List<User> results = query.getResultList();
        return results;
    }

    @Transactional
    public List<User> getAllUnverifiedCaptains() {
        Session s = getCurrentSession();
        String queryString = "FROM User where accountStatus != 2 AND accountType = 2";
        Query<User> query = s.createQuery(queryString, User.class);
        List<User> results = query.getResultList();
        return results;
    }
}

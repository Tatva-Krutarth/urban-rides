package com.urbanrides.dao;

import com.urbanrides.model.NotificationLogs;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.query.Query;


@Repository
public class NotificationLogsDao {


    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public int saveNotificationLog(NotificationLogs notificationLogs) {
        Integer id = (Integer) this.hibernateTemplate.save(notificationLogs);
        return id;
    }

    @Transactional
    public List<NotificationLogs> getAllNotificationLogs(int id) {
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
        Session session = getCurrentSession();
        String hql = "FROM NotificationLogs WHERE user.userId = :id AND createdDate >= :fiveDaysAgo ORDER BY createdDate ASC";
        Query<NotificationLogs> query = session.createQuery(hql, NotificationLogs.class);
        query.setParameter("fiveDaysAgo", fiveDaysAgo);
        query.setParameter("id", id);
        List<NotificationLogs> result = query.getResultList();
        return result.isEmpty() ? null : result;
    }

}

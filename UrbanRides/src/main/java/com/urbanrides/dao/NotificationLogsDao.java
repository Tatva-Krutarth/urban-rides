package com.urbanrides.dao;

import com.urbanrides.model.NotificationLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public class NotificationLogsDao {


    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Transactional
    public int saveNotificationLog(NotificationLogs notificationLogs) {
        Integer id = (Integer) this.hibernateTemplate.save(notificationLogs);
        return id;
    }


    @PersistenceContext
    private EntityManager entityManager;

    public List<NotificationLogs> getAllNotificationLogs() {
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);

        TypedQuery<NotificationLogs> query = entityManager.createQuery(
                "SELECT n FROM NotificationLogs n WHERE n.createdDate >= :fiveDaysAgo ORDER BY n.createdDate ASC",
                NotificationLogs.class
        );
        query.setParameter("fiveDaysAgo", fiveDaysAgo);

        List<NotificationLogs> result = query.getResultList();
        return result.isEmpty() ? null : result;
    }

}

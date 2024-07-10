package com.urbanrides.dao;

import com.urbanrides.model.CaptainDetails;
import com.urbanrides.model.Trip;
import com.urbanrides.model.UserDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CaptainDetailsDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void saveCaptainDetails(CaptainDetails captainDetails) {
        this.hibernateTemplate.save(captainDetails);
    }


    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public CaptainDetails getCaptainDetailByUserId(int userId) {
        Session session = entityManager.unwrap(Session.class);
        String hql = "FROM CaptainDetails WHERE user.userId = :userId";
        Query<CaptainDetails> query = session.createQuery(hql, CaptainDetails.class);
        query.setParameter("userId", userId);

        List<CaptainDetails> result = query.getResultList();

        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
    @Transactional
    public void updateCaptainDetail(CaptainDetails captainDetails) {
        this.hibernateTemplate.update(captainDetails);
    }
}



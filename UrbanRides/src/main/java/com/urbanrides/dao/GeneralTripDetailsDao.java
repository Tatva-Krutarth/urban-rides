package com.urbanrides.dao;

import com.urbanrides.model.GeneralTripDetails;
import com.urbanrides.model.Trip;
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
public class GeneralTripDetailsDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public int saveGeneralTripDetails(GeneralTripDetails generalTripDetails) {
        this.hibernateTemplate.save(generalTripDetails);
        return generalTripDetails.getGeneralTripDetailId();
    }

    @Transactional
    public GeneralTripDetails getGeneralTripDetails(Integer id) {
        return this.hibernateTemplate.get(GeneralTripDetails.class, id);
    }

    @Transactional
    public void updateGeneralTripDetails(GeneralTripDetails generalTripDetails) {
        this.hibernateTemplate.update(generalTripDetails);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public GeneralTripDetails getGeneralTripByTripId(int tripId) {
        Session session = entityManager.unwrap(Session.class);
        String hql = "FROM GeneralTripDetails WHERE tripObj.tripId = :tripId";
        Query<GeneralTripDetails> query = session.createQuery(hql, GeneralTripDetails.class);
        query.setParameter("tripId", tripId);

        List<GeneralTripDetails> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}

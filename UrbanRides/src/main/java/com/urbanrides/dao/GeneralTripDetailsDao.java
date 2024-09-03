package com.urbanrides.dao;


import com.urbanrides.model.GeneralTripDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class GeneralTripDetailsDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public int saveGeneralTripDetails(GeneralTripDetails generalTripDetails) {
        this.hibernateTemplate.save(generalTripDetails);
        return generalTripDetails.getGeneralTripDetailId();
    }

    public GeneralTripDetails getGeneralTripDetailsById(int id){
        Session s = this.sessionFactory.getCurrentSession();
        String queryString = "FROM GeneralTripDetails WHERE generalTripDetailId =: id";
        Query<GeneralTripDetails> q = s.createQuery(queryString,GeneralTripDetails.class);
        q.setParameter("id", id);
        List<GeneralTripDetails> list = q.list();
        return list.get(0);
    }

    @Transactional
    public void updateGeneralTripDetails(GeneralTripDetails generalTripDetails) {
        this.hibernateTemplate.update(generalTripDetails);
    }

    @Transactional
    public GeneralTripDetails getGeneralTripByTripId(int tripId) {
        Session session = getCurrentSession();
        String hql = "FROM GeneralTripDetails WHERE tripObj.tripId = :tripId";
        Query<GeneralTripDetails> query = session.createQuery(hql, GeneralTripDetails.class);
        query.setParameter("tripId", tripId);

        List<GeneralTripDetails> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}

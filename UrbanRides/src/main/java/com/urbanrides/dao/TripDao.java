

package com.urbanrides.dao;


import com.urbanrides.model.Trip;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class TripDao{

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public int saveGeneralTrip(Trip generalTrip) {
        this.hibernateTemplate.save(generalTrip);
        return generalTrip.getTripId(); // Assuming GeneralTrip has a getId() method
    }


    @Transactional
    public List<Trip> getTripForPayment(int userId) {
        Session s = sessionFactory.openSession();
        String queryString = "FROM Trip WHERE tripUserId.userId = :userId AND isAccepted = true";
        Query<Trip> query = s.createQuery(queryString, Trip.class);
        query.setParameter("userId", userId);
        List<Trip> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList;
        }
    }
    @Transactional

    public Trip getTipById(Integer id) {
        return this.hibernateTemplate.get(Trip.class, id);
    }
    @Transactional
    public void updateGeneralTrip(com.urbanrides.model.Trip generalTrip) {
        this.hibernateTemplate.update(generalTrip);
    }
}


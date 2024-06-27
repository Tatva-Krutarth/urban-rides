package com.urbanrides.dao;

import com.urbanrides.model.GeneralTripDetails;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public class GeneralTripDetailsDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public int saveGeneralTripDetails(GeneralTripDetails generalTripDetails) {
        this.hibernateTemplate.save(generalTripDetails);
        return generalTripDetails.getTripId();
    }

    @Transactional
    public GeneralTripDetails getGeneralTripDetails(Integer id) {
        return this.hibernateTemplate.get(GeneralTripDetails.class, id);
    }

    @Transactional
    public void updateGeneralTripDetails(GeneralTripDetails generalTripDetails) {
        this.hibernateTemplate.update(generalTripDetails);
    }
}

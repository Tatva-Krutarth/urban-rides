


package com.urbanrides.dao;


import com.urbanrides.model.VehicleType;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class VehicleTypeDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private SessionFactory sessionFactory;


    public VehicleType getVehicaleId(Integer vehicleId) {
        return this.hibernateTemplate.get(VehicleType.class, vehicleId);
    }
}


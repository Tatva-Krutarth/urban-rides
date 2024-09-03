package com.urbanrides.dao;

import com.urbanrides.model.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class ServiceTypeDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Transactional
    public ServiceType getServiceType(Integer id) {
        return this.hibernateTemplate.get(ServiceType.class, id);
    }

}

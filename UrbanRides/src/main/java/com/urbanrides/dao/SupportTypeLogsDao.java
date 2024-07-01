package com.urbanrides.dao;

import com.urbanrides.model.CaptainDetails;
import com.urbanrides.model.SupportTypeLogs;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository

public class SupportTypeLogsDao {


    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void saveSupportLogs(SupportTypeLogs supportTypeLogs) {
        this.hibernateTemplate.save(supportTypeLogs);
    }

}





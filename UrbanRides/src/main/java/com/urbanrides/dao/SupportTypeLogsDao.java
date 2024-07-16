package com.urbanrides.dao;

import com.urbanrides.model.CaptainDetails;
import com.urbanrides.model.SupportTypeLogs;
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

public class SupportTypeLogsDao {


    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void saveSupportLogs(SupportTypeLogs supportTypeLogs) {
        this.hibernateTemplate.save(supportTypeLogs);
    }

    @Transactional
    public SupportTypeLogs getSupportByUserId(int userId) {
        Session s = sessionFactory.openSession();
        String queryString = "FROM SupportTypeLogs WHERE userObj.userId = :userId AND isSolved = true ORDER BY createdDate DESC";
        Query<SupportTypeLogs> query = s.createQuery(queryString, SupportTypeLogs.class);
        query.setParameter("userId", userId);
        List<SupportTypeLogs> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Transactional
    public SupportTypeLogs getSupportPerData(String supportCaseId) {
        Session s = sessionFactory.openSession();
        String queryString = "FROM SupportTypeLogs WHERE supportCaseId = : supportCaseId";
        Query<SupportTypeLogs> query = s.createQuery(queryString, SupportTypeLogs.class);
        query.setParameter("supportCaseId", supportCaseId);
        List<SupportTypeLogs> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Transactional
    public List<SupportTypeLogs> getAllLogsData() {
        Session s = sessionFactory.openSession();
        String queryString = "FROM SupportTypeLogs";
        Query<SupportTypeLogs> query = s.createQuery(queryString, SupportTypeLogs.class);
        List<SupportTypeLogs> results = query.getResultList();
        return results;
    }


}





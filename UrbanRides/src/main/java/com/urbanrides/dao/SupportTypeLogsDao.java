package com.urbanrides.dao;

import com.urbanrides.model.SupportTypeLogs;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
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
    public SupportTypeLogs getSupportBySupportId(int supportLogsId) {
        Session s = getCurrentSession();
        String queryString = "FROM SupportTypeLogs WHERE supportLogsId = :supportLogsId";
        Query<SupportTypeLogs> query = s.createQuery(queryString, SupportTypeLogs.class);
        query.setParameter("supportLogsId", supportLogsId);
        List<SupportTypeLogs> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Transactional
    public SupportTypeLogs checkActiveRequests(int userId) {
        Session s = getCurrentSession();
        String queryString = "FROM SupportTypeLogs WHERE userObj.userId = :userId   and isSolved = false ";
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
    public Page<SupportTypeLogs> getAllLogsData(Pageable pageable) {
        Session s = getCurrentSession();
        String queryString = "FROM SupportTypeLogs WHERE adminObj is null";
        Query<SupportTypeLogs> query = s.createQuery(queryString, SupportTypeLogs.class);
        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult((int) pageable.getOffset());
        List<SupportTypeLogs> results = query.getResultList();
        Query<Long> countQuery = s.createQuery("SELECT COUNT(*) FROM SupportTypeLogs WHERE adminObj is null", Long.class);
        Long count = countQuery.uniqueResult();
        return new PageImpl<>(results, pageable, count);
    }


    @Transactional
    public Page<SupportTypeLogs> getRunningData(Pageable pageable) {
        Session session = getCurrentSession();
        String queryString = "FROM SupportTypeLogs WHERE adminObj is not null AND isSolved is false";
        Query<SupportTypeLogs> query = session.createQuery(queryString, SupportTypeLogs.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<SupportTypeLogs> results = query.getResultList();
        Query<Long> countQuery = session.createQuery("SELECT COUNT(*) FROM SupportTypeLogs WHERE adminObj is not null AND isSolved is false", Long.class);
        Long count = countQuery.uniqueResult();
        return new PageImpl<>(results, pageable, count);
    }


    @Transactional
    public Page<SupportTypeLogs> getCompletedData(Pageable pageable) {
        Session session = getCurrentSession();
        String queryString = "FROM SupportTypeLogs WHERE isSolved = true";
        Query<SupportTypeLogs> query = session.createQuery(queryString, SupportTypeLogs.class);
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int firstResult = pageNumber * pageSize;
        query.setFirstResult(firstResult);
        query.setMaxResults(pageSize);
        List<SupportTypeLogs> results = query.getResultList();
        Query<Long> countQuery = session.createQuery("SELECT COUNT(*) FROM SupportTypeLogs WHERE isSolved = true", Long.class);
        long totalCount = countQuery.getSingleResult();
        return new PageImpl<>(results, pageable, totalCount);
    }

    @Transactional
    public void updateSupportTypeLogs(SupportTypeLogs supportTypeLogs) {
        this.hibernateTemplate.update(supportTypeLogs);
    }

}





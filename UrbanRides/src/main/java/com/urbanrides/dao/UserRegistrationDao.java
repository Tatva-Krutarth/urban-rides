package com.urbanrides.dao;

import com.urbanrides.model.OtpLogs;
import lombok.extern.flogger.Flogger;
import org.codehaus.jackson.JsonToken;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRegistrationDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public int saveUser(OtpLogs otpLogs) {
        Integer id = (Integer) this.hibernateTemplate.save(otpLogs);
        return id;
    }

    @Transactional
    public OtpLogs getOtpLogsByEmail(String email) {
        Session s = sessionFactory.openSession();
        try {
            String sm = "FROM OtpLogs WHERE email = :email ORDER BY createdDate desc, optReqSendTime desc";
            Query qq = s.createQuery(sm);
            qq.setParameter("email", email);
            List<OtpLogs> list = qq.list();
            if(list!= null &&!list.isEmpty()) {
                return list.get(0);
            } else {
                return null; // or you can return a default OtpLogs object
            }
        } finally {
            s.close();
        }
    }

    @Transactional
    public void deleteOtpLogsByEmail(String email) {
        Session session = sessionFactory.openSession();
        String hql = "DELETE FROM OtpLogs  WHERE email = :email";
        Query query = session.createQuery(hql);
        query.setParameter("email", email);
        session.close();
    }

}

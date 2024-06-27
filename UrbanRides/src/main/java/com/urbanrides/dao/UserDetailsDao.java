package com.urbanrides.dao;


import com.urbanrides.model.User;
import com.urbanrides.model.UserDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserDetailsDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void saveUserDetails(UserDetails userDetails) {
        this.hibernateTemplate.save(userDetails);
    }

    @Transactional
    public UserDetails getUserDetailsById(int id) {
        Session s = sessionFactory.openSession();
        try {
            String sm = "FROM UserDetails WHERE userDetailsId = :id ";
            Query qq = s.createQuery(sm);
            qq.setParameter("id", id);
            List<UserDetails> list = qq.list();
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            } else {
                return null; // or you can return a default OtpLogs object
            }
        } finally {
            s.close();
        }
    }


    @Transactional
    public UserDetails getUserDetailsByUserId(int id) {
        Session s = sessionFactory.openSession();
        try {
            String sm = "FROM UserDetails WHERE user.userId = :id ";
            Query qq = s.createQuery(sm);
            qq.setParameter("id", id);
            List<UserDetails> list = qq.list();
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            } else {
                return null; // or you can return a default OtpLogs object
            }
        } finally {
            s.close();
        }
    }


    //update
    @Transactional
    public void updateUserDetails(UserDetails userDetails) {
        this.hibernateTemplate.update(userDetails);
    }

}

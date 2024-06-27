package com.urbanrides.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.transaction.Transactional;

public class AllDaoReference {
//    @Autowired
//    private HibernateTemplate hibernateTemplate;
//    @Autowired
//    private SessionFactory sessionFactory;
//    @Transactional

//    //save
//    @Transactional
//    public int saveUser(asp_net_users asp_net_users) {
//        Integer id = (Integer) this.hibernateTemplate.save(asp_net_users);
//        return id;
//    }
//
//
//    //update
//    @Transactional
//    public void updateUser(asp_net_users asp_net_users) {
//        this.hibernateTemplate.update(asp_net_users);
//    }
//
//    //delete one row at a time
//    @Transactional
//    public void deleteUser(Integer id) {
//        asp_net_users user = this.hibernateTemplate.get(asp_net_users.class, id);
//        this.hibernateTemplate.delete(user);
//    }
//
//delete
//@Transactional
//public void deleteOtpLogsByEmail(String email) {
//    String hql = "DELETE FROM OtpLogs WHERE email = :email";
//    Query query = sessionFactory.getCurrentSession().createQuery(hql);
//    query.setParameter("email", email);
//    query.executeUpdate();
//}
//    //get single
//    public asp_net_users getUserById(Integer id) {
//        return this.hibernateTemplate.get(asp_net_users.class, id);
//    }
//
//    //get multiple
//    @SuppressWarnings("unchecked")
//    public List<asp_net_users> getAllUsers() {
//        return this.hibernateTemplate.find("from asp_net_users");
//    }

}

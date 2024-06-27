package com.urbanrides.dao;

import com.urbanrides.model.PackageTrip;
import com.urbanrides.model.Trip;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class PackageTripDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private SessionFactory sessionFactory;

//    @Transactional
//    public int savePackageTrip(PackageTrip packageTrip) {
//        this.hibernateTemplate.save(packageTrip);
//        return packageTrip.getPackageTripId();
//    }
    //save
    @Transactional
    public void savePackageTrip(PackageTrip packageTrip) {
        this.hibernateTemplate.save(packageTrip);

    }

    @Transactional
    public PackageTrip getPackageTrip(Integer id) {
        return this.hibernateTemplate.get(PackageTrip.class, id);
    }

    @Transactional
    public void updatePackageTrip(PackageTrip packageTrip) {

        this.hibernateTemplate.update(packageTrip);
    }
}


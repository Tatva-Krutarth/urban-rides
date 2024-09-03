package com.urbanrides.dao;

import com.urbanrides.model.PackageTrip;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class PackageTripDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void savePackageTrip(PackageTrip packageTrip) {
        this.hibernateTemplate.save(packageTrip);
    }

    @Transactional
    public void updatePackageTrip(PackageTrip packageTrip) {
        this.hibernateTemplate.update(packageTrip);
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public PackageTrip getPackageTripDataByTripId(int tripId) {
        Session session = getCurrentSession();
        String hql = "FROM PackageTrip WHERE tripId.tripId = :tripId";
        Query<PackageTrip> query = session.createQuery(hql, PackageTrip.class);
        query.setParameter("tripId", tripId);
        List<PackageTrip> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

}




package com.urbanrides.dao;


import com.urbanrides.dtos.AdminRidesFilterData;
import com.urbanrides.model.NotificationLogs;
import com.urbanrides.model.Trip;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class TripDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public int saveGeneralTrip(Trip generalTrip) {
        this.hibernateTemplate.save(generalTrip);
        return generalTrip.getTripId(); // Assuming GeneralTrip has a getId() method
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public List<Trip> getTripForPayment(int userId) {
        Session s = sessionFactory.openSession();
        String queryString = "FROM Trip WHERE tripUserId.userId = :userId AND isAccepted = true AND paymentMethod IS NOT NULL";
        Query<Trip> query = s.createQuery(queryString, Trip.class);
        query.setParameter("userId", userId);
        List<Trip> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList;
        }
    }

    @Transactional
    public List<Trip> getTripForPaymentForCaptain(int userId) {
        Session s = sessionFactory.openSession();
        String queryString = "FROM Trip WHERE captainUserObj.userId = :userId AND isAccepted = true AND paymentMethod IS NOT NULL";
        Query<Trip> query = s.createQuery(queryString, Trip.class);
        query.setParameter("userId", userId);
        List<Trip> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList;
        }
    }


    @Transactional
    public Trip getTipById(int tripId) {
        Session session = getCurrentSession();
        String hql = "FROM Trip WHERE tripId = :tripId";
        Query<Trip> query = session.createQuery(hql, Trip.class);
        query.setParameter("tripId", tripId);

        List<Trip> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }


    @Transactional
    public void updateTrip(Trip tripId) {
        this.hibernateTemplate.update(tripId);
    }


    @Transactional
    public List<Trip> getAllTrip(int userId) {
        Session session = getCurrentSession();
        String hql = "FROM Trip WHERE tripUserId.userId = :userId";
        Query<Trip> query = session.createQuery(hql, Trip.class);
        query.setParameter("userId", userId);

        List<Trip> result = query.getResultList();
        return result.isEmpty() ? null : result;
    }

    @Transactional
    public List<Trip> getAllTripOfCaptain(int userId) {
        Session session = getCurrentSession();
        String hql = "FROM Trip WHERE captainUserObj.userId = :userId";
        Query<Trip> query = session.createQuery(hql, Trip.class);
        query.setParameter("userId", userId);

        List<Trip> result = query.getResultList();
        return result.isEmpty() ? null : result;
    }

    @Transactional
    public List<Trip> getAllPackageTrips(int userId) {
        Session session = getCurrentSession();
        String hql = "FROM Trip WHERE ServiceType.serviceTypeId != :serviceTypeId AND paymentMethod = null  And (isAccepted = false OR captainUserObj.userId = :userId)";
        Query<Trip> query = session.createQuery(hql, Trip.class);
        query.setParameter("serviceTypeId", 1);
        query.setParameter("userId", userId);
        List<Trip> result = query.getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }


    @Transactional
    public List<Trip> getAllTripByFilter(AdminRidesFilterData filterData) {
        Session session = getCurrentSession();
        StringBuilder hql = new StringBuilder("FROM Trip t WHERE 1=1");

        // Adding conditions based on filter data
        if (filterData.getTripCode() != null && !filterData.getTripCode().isEmpty()) {
            hql.append(" AND t.tripCode = : tripCode");
        }
        if (filterData.getServiceType() != 0) {
            hql.append(" AND t.ServiceType.serviceTypeId = : serviceType");
        }

        // Adding trip status conditions
        if (filterData.getTripStatus() == 1) {
            hql.append(" AND t.isAccepted = false AND t.reasonForCancellation IS NULL");
        } else if (filterData.getTripStatus() == 2) {
            hql.append(" AND t.isAccepted = false AND t.reasonForCancellation IS NULL AND t.ServiceType.serviceTypeId NOT IN (1)");
        } else if (filterData.getTripStatus() == 3) {
            hql.append(" AND t.isAccepted = false AND t.reasonForCancellation IS NOT NULL");
        } else if (filterData.getTripStatus() == 4) {
            hql.append(" AND t.isAccepted = true AND t.reasonForCancellation IS NULL");
        }else if (filterData.getTripStatus() == 5) {
            hql.append(" AND t.isAccepted = true AND t.paymentMethod IS NOT NULL");
        }
        try {
            Query<Trip> query = session.createQuery(hql.toString(), Trip.class);

            if (filterData.getTripCode() != null && !filterData.getTripCode().isEmpty()) {
                query.setParameter("tripCode", filterData.getTripCode());
            }
            if (filterData.getServiceType() != 0) {
                query.setParameter("serviceType", filterData.getServiceType());
            }

            List<Trip> result = query.getResultList();
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception appropriately
            return new ArrayList<>(); // Return an empty list or handle the exception according to your application's logic
        }
    }

    @Transactional
    public List<Trip> getAllTripOfCaptainDashboard(int vehicledId) {
        Session session = getCurrentSession();
        // Corrected HQL query with parameter placeholder
        String hql = "FROM Trip WHERE isAccepted = false AND ServiceType.serviceTypeId = 1 AND reasonForCancellation IS NULL AND vehicleId.vehicleId = :vehicledId";

        // Create query and set the parameter
        Query<Trip> query = session.createQuery(hql, Trip.class);
        query.setParameter("vehicledId", vehicledId);

        List<Trip> result = query.getResultList();
        return result.isEmpty() ? null : result;
    }


    @Transactional
    public int getCountOfGeneralBooking() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(*) FROM Trip  WHERE ServiceType.serviceTypeId = 1";
        Query<Long> query = session.createQuery(hql, Long.class);
        Long count = query.uniqueResult();
        return count.intValue();
    }

    @Transactional
    public int getCountOfServiceBooking() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(*) FROM Trip  WHERE ServiceType.serviceTypeId IN (2, 3)";
        Query<Long> query = session.createQuery(hql, Long.class);
        Long count = query.uniqueResult();
        return count.intValue();
    }

    @Transactional
    public int getSuccessTripCount() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(*) FROM Trip WHERE isAccepted = TRUE AND paymentMethod IS NOT NULL";
        Query<Long> query = session.createQuery(hql, Long.class);
        Long count = query.uniqueResult();
        return count.intValue();
    }


    @Transactional
    public int getSuccessTripCountPerUser(int userId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(*) FROM Trip WHERE tripUserId.userId = :userId AND isAccepted = TRUE AND paymentMethod IS NOT NULL";
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("userId", userId);
        Long count = query.uniqueResult();
        return count != null ? count.intValue() : 0;
    }

    @Transactional
    public int getFailedTripCount(int userId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(*) FROM Trip WHERE tripUserId.userId = :userId AND isAccepted = TRUE AND reasonForCancellation IS NOT NULL";
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("userId", userId);

        Long count = query.uniqueResult();
        return count.intValue();
    }

    @Transactional
    public int getTotalTripCount(int userId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT COUNT(*) FROM Trip WHERE tripUserId.userId = :userId AND isAccepted = TRUE";
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("userId", userId);

        Long count = query.uniqueResult();
        return count.intValue();
    }
}


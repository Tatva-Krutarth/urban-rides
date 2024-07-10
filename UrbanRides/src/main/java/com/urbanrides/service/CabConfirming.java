package com.urbanrides.service;

import com.urbanrides.dao.TripDao;
import com.urbanrides.dao.UserDetailsDao;
import com.urbanrides.dao.UsersDao;
import com.urbanrides.dao.VehicleTypeDao;
import com.urbanrides.dtos.CaptainAllTripsData;
import com.urbanrides.model.Trip;
import com.urbanrides.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class CabConfirming {
    @Autowired
    UsersDao usersDao;
    @Autowired
    UserDetailsDao userDetailsDao;

    @Autowired
    TripDao tripDao;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    VehicleTypeDao vehicleTypeDao;
    @Autowired
    private HttpSession httpSession;


    public List<CaptainAllTripsData> getTripsData() {


        List<Trip> trips = tripDao.getAllTripOfCaptain();
        List<CaptainAllTripsData> captainDataList = new ArrayList<>();
        for (Trip trip : trips) {
            UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(trip.getTripUserId().getUserId());
            CaptainAllTripsData captainData = new CaptainAllTripsData();
            captainData.setTripId(trip.getTripId());
            captainData.setPassengerName(userDetails.getFirstName() + " , " + userDetails.getLastName());
            captainData.setPickUpLocation(trip.getPickupAddress());
            captainData.setCharges(trip.getCharges());
            captainData.setDropLocation(trip.getDropoffAddress());
            captainDataList.add(captainData);
        }
        return captainDataList;

    }
}

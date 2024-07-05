package com.urbanrides.service;

import com.urbanrides.dao.TripDao;
import com.urbanrides.dao.UserDetailsDao;
import com.urbanrides.dao.UsersDao;
import com.urbanrides.dao.VehicleTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

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


}

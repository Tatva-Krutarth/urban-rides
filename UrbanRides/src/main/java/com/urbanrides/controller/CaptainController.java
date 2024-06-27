package com.urbanrides.controller;


import com.urbanrides.service.CabBookingService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/captain")

public class CaptainController {


    @Autowired
    private CabBookingService cabBookingService;

    @RequestMapping("/captain-dashboard")
    public String captainDashboard() {
        return "captain/captainDashboard";
    }


    @ResponseBody
    @PostMapping("/accept-ride")
    public String acceptRide(@RequestParam("tripId") int tripId) {
        String toasterMsg = cabBookingService.acceptRide(tripId);
        System.out.println(toasterMsg);
        return "Done";
    }

    @ResponseBody
    @PostMapping("/captain-otp-submit")
    public String captainOtpSubmit(@RequestParam("otp") int otp) {
        String toasterMsg = cabBookingService.captainOtp(otp);
        System.out.println(toasterMsg);
        return "Done";
    }

}

package com.urbanrides.controller;


import com.urbanrides.dtos.CaptainPersonalDetailsDto;
import com.urbanrides.dtos.RiderPersonalDetailsDto;
import com.urbanrides.service.CabBookingService;
import com.urbanrides.service.LoginServices;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Controller
@RequestMapping("/captain")

public class CaptainController {


    @Autowired
    private CabBookingService cabBookingService;
    @Autowired
    private LoginServices loginServices;


    @RequestMapping("/captain-personal-details")
    public String riderPersonalDetails() {
        System.out.println("inside the demo in captain side");
        return "captain/captainPersonalDetails";
    }

    @ResponseBody
    @PostMapping("/captain-personal-details-submit")
    public ResponseEntity<String> riderPersonalDetailSubmit(@Valid @RequestBody RiderPersonalDetailsDto riderPersonalDetailsDto, HttpServletRequest request) {
        String toasterMsg = loginServices.captainPersonalDetailSubmit(riderPersonalDetailsDto, request);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }


    @RequestMapping("/captain-document-details")
    public String captainPersonalDetails(HttpServletRequest req, Model m) {

        String captainName = loginServices.getCapatainName(req);
        m.addAttribute("captainName", captainName);
        return "captain/captainDocumentDetails";
    }

    @RequestMapping(value = "/captain-document-details-submit", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@Valid @ModelAttribute("captainPersonalDetailsDto") CaptainPersonalDetailsDto captainPersonalDetailsDto, HttpServletRequest request, HttpSession session, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws MethodArgumentNotValidException {
        String toasterMsg = loginServices.saveCaptainPersonalDetails(captainPersonalDetailsDto, session, request);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    //    @RequestMapping("/captain-support")
//    public String captainSupport() {
//        return "login/captainSupport";
//    }
    @RequestMapping("/captain-waiting-page")
    public String captainWaitingPage() {
        return "captain/captainWaitingPage";
    }

    @RequestMapping("/captain-dashboard")
    public String captainDashboard() {
        return "captain/catainDashboard";
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

    @RequestMapping("/captain-earnings")
    public String captainEarning() {
        return "captain/captainEarnings";
    }

    @RequestMapping("/captain-help")
    public String captainSupport() {
        return "captain/captainHelp";
    }

    @RequestMapping("/captain-manage-account")
    public String captainManageAccount() {
        return "captain/captainManageAccount";
    }

    @RequestMapping("/captain-my-trip")
    public String captainMyRides() {
        return "captain/captainMyTrips";
    }

    @RequestMapping("/captain-notifications")
    public String captainNotification() {
        return "captain/captainNotification";
    }

    @RequestMapping("/captain-package-rides")
    public String captainPackageRides() {
        return "captain/captainPackageRides";
    }
    @RequestMapping("/captain-logout")
    public String riderLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "landingPage/landingPage";
    }

}

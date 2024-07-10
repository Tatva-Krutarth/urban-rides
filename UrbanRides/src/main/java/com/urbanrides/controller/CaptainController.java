package com.urbanrides.controller;


import com.urbanrides.dtos.*;
import com.urbanrides.service.CabBookingService;
import com.urbanrides.service.CabConfirming;
import com.urbanrides.service.CaptainOtherService;
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
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/captain")

public class CaptainController {


    @Autowired
    private CabBookingService cabBookingService;
    @Autowired
    private LoginServices loginServices;
    @Autowired
    private CabConfirming cabConfirming;
    @Autowired
    private CaptainOtherService captainOtherService;


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
    @RequestMapping("/get-all-trips-data")
    public List<CaptainAllTripsData> getWalletData() {
        List<CaptainAllTripsData> listOfTrips = cabConfirming.getTripsData();
        return listOfTrips;
    }

    @ResponseBody
    @PostMapping("/accept-ride")
    public RiderInfoDto acceptRide(@RequestParam("tripId") int tripId) {
        RiderInfoDto riderInfoDto = cabBookingService.acceptRideCaptainSide(tripId);
        cabBookingService.acceptRideRiderSide(tripId);
        return riderInfoDto;
    }

    @ResponseBody
    @PostMapping("/captain-otp-submit")
    public ResponseEntity<String> captainOtpSubmit(@RequestBody Map<String, Integer> payload) {
        try {
            int otp = payload.get("otp");
            int tripId = payload.get("tripId");
            String toasterMsg = cabBookingService.captainOtp(otp, tripId);
            System.out.println(toasterMsg);
            return ResponseEntity.ok("Done");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing OTP: " + e.getMessage());
        }
    }


    @RequestMapping("/captain-earnings")
    public String captainEarning(Model model) {


        double walletAMount = captainOtherService.getAmount();
        model.addAttribute("walletAmount", walletAMount);
        return "captain/captainEarnings";
    }


    @ResponseBody
    @RequestMapping("/captain-transaction-details")
    public List<RiderWalletDataDto> getWalletDataCaptain() {
        List<RiderWalletDataDto> listOfWalletData = captainOtherService.getPaymentData();
        return listOfWalletData;
    }

    @PostMapping("/captain-update-amount")
    public ResponseEntity<?> updateWalletAmount(@RequestParam double amount) {
        return captainOtherService.validateAndDepositAmount(amount);
    }

    @RequestMapping("/captain-help")
    public String captainSupport() {
        return "captain/captainHelp";
    }

    @RequestMapping("/captain-manage-account")
    public String captainManageAccount() {

        return "captain/captainManageAccount";
    }

    @ResponseBody
    @RequestMapping("/captain-usermanagement-details")
    public UserManagementDataDto userManagementDetails(HttpServletRequest req) {
        UserManagementDataDto userManagementDataDto = captainOtherService.getUserManagementDetails();
        return userManagementDataDto;
    }

    @ResponseBody
    @PostMapping("/update-personal-details")
    public ResponseEntity<String> riderPersonalDetailSubmit(@Valid @RequestBody RiderUMPersonalDetailDto riderUMPersonalDetailDto, HttpServletRequest request) {
        try {
            captainOtherService.riderPersonalDetailSubmit(riderUMPersonalDetailDto, request);
            return new ResponseEntity<>("Personal details updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update personal details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/update-login-details")
    public ResponseEntity<String> updateLoginDetails(@Valid @RequestBody RiderUMLoginDetails riderUMLoginDetails, HttpServletRequest request) {
        try {
            String result = captainOtherService.sendPassToService(riderUMLoginDetails, request);
            if (result != null) {
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Login details updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update login details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-profile-photo")
    public ResponseEntity<String> updateProfilePhoto(@Valid @ModelAttribute RiderUMUpdateProfileLogo riderUMUpdateProfileLogo, HttpSession session, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
        try {
            String newProfilePhotoPath = captainOtherService.updateProfilePic(riderUMUpdateProfileLogo, session);
            String relativePath = "/UrbanRides/resources/uploads/riderDocuments" + newProfilePhotoPath;
            return new ResponseEntity<>(relativePath, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update profile photo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/captain-get-support")
    public ResponseEntity<String> saveGetSupport(@Valid @ModelAttribute RiderGetSupportDto riderGetSupportDto, HttpSession session, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
        try {
            captainOtherService.getSupportSaveToLogs(riderGetSupportDto, session);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to submit the complain: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/captain-my-trip")
    public String captainMyRides() {
        return "captain/captainMyTrips";
    }


    @RequestMapping("/captain-notifications")
    public String riderNotifications(Model model) {
        List<NotificationDataDto> notiList = captainOtherService.getNotificationData();

        if (notiList.isEmpty()) {
            model.addAttribute("noNotifications", "No new notifications in the last 5 days.");
        } else {
            model.addAttribute("notificationDataDto", notiList);
        }

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

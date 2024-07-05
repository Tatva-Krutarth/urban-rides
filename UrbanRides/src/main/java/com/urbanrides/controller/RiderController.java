package com.urbanrides.controller;


import com.urbanrides.dtos.*;
import com.urbanrides.model.SupportTypeLogs;
import com.urbanrides.service.CabBookingService;
import com.urbanrides.service.LoginServices;
import com.urbanrides.service.RiderOtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/rider")

public class RiderController {


    @Autowired
    private CabBookingService cabBookingService;
    @Autowired
    private ConversionService conversionService;

    @Autowired
    private RiderOtherService riderOtherService;
    @Autowired
    private LoginServices loginServices;

    @RequestMapping("/rider-personal-details")
    public String riderPersonalDetails() {
        System.out.println("inside the demo");
        return "rider/riderPersonalDetails";
    }

    @ResponseBody
    @PostMapping("/rider-personal-details-submit")
    public ResponseEntity<String> riderPersonalDetailSubmit(@Valid @RequestBody RiderPersonalDetailsDto riderPersonalDetailsDto, HttpServletRequest request) {
        String toasterMsg = loginServices.riderPersonalDetailSubmit(riderPersonalDetailsDto, request);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @RequestMapping("/rider-dashboard")
    public String riderDashboard() {
        return "rider/riderDashboard";
    }


    @ResponseBody
    @PostMapping("/rider-normal-ride-submit")
    public ResponseEntity<String> riderNormalRideSubmit(@Valid @RequestBody RiderNormalRideDto riderNormalRideDto) {
        String toasterMsg = cabBookingService.generalRide(riderNormalRideDto );
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/cancel-ride-submit")
    public ResponseEntity<String> riderNormalRideSubmit(@RequestParam("cancelation-reason") String cancellationReason, @RequestParam("trip-id") int tripId) {
        System.out.println("here we go again");
        String toasterMsg = cabBookingService.cancelRide(cancellationReason, tripId);

        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @PostMapping("/rider-reach-info")
    @ResponseBody
    public int riderReachInfo(@RequestBody RiderReachInfo riderReachInfo) {
        int generalTripDetailId = cabBookingService.saveGeneralTripInfo(riderReachInfo);
        return generalTripDetailId;
    }

    @RequestMapping(path = "/ride-start-info", method = RequestMethod.POST)
    @ResponseBody
    public void riderStartInfo(@RequestParam("tripId") int tripId) {
        cabBookingService.saveRiderStartInfo(tripId);
    }


    @RequestMapping(path = "/ride-end-info", method = RequestMethod.POST)
    @ResponseBody
    public RattingModalDataDto rideEndInfo(@RequestParam("tripId") int tripId) {
        RattingModalDataDto rattingModalDataDto = cabBookingService.saveRideEndInfo(tripId);
        return rattingModalDataDto;
    }


    @PostMapping("/ride-ratting-submit")
    @ResponseBody
    public String rideRattingSubmit(@RequestBody RiderRattingConclude riderRattingConclude) {
        cabBookingService.saveRattingInfo(riderRattingConclude);
        return "Done";
    }


    @ResponseBody
    @PostMapping("/package-ride-submit")
    public ResponseEntity<String> packageRideSubmit(@Valid @RequestBody PackageServiceDto packageServiceDto) {
        String toasterMsg = cabBookingService.savePackageTripDetails(packageServiceDto);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @RequestMapping("/rider-notifications")
    public String riderNotifications(Model model) {
        List<NotificationDataDto> notiList = riderOtherService.getNotificationData();

        if (notiList.isEmpty()) {
            model.addAttribute("noNotifications", "No new notifications in the last 5 days.");
        } else {
            model.addAttribute("notificationDataDto", notiList);
        }

        return "rider/riderNotifications";
    }

    @PostMapping("/rider-get-support")
    public ResponseEntity<String> saveGetSupport(@Valid @ModelAttribute RiderGetSupportDto riderGetSupportDto, HttpSession session, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
        try {
            riderOtherService.getSupportSaveToLogs(riderGetSupportDto, session);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to submit the complain: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/rider-my-trip")
    public String riderMyTrip(HttpServletRequest request, Model model) {
        List<RiderMyTripDataDto> listOfRiderTripDetails = riderOtherService.getTripDetails(request);
        model.addAttribute("tripDetails", listOfRiderTripDetails);
        return "rider/riderMyTrips";
    }

    @RequestMapping("/rider-wallet")
    public String riderWallet(Model model, HttpServletRequest req) {


        double walletAMount = riderOtherService.getAmount(req);
        model.addAttribute("walletAmount", walletAMount);
        return "rider/riderWallet";
    }


    @ResponseBody
    @RequestMapping("/rider-transaction-details")
    public List<RiderWalletDataDto> getWalletData(HttpServletRequest req) {
        List<RiderWalletDataDto> listOfWalletData = riderOtherService.getPaymentData(req);
        return listOfWalletData;
    }

    @PostMapping("/rider-update-amount")
    public ResponseEntity<?> updateWalletAmount(@RequestParam double amount) {
        return riderOtherService.validateAndDepositAmount(amount);
    }

    @RequestMapping("/rider-help")
    public String riderHelp() {
        return "rider/riderHelp";
    }

    @RequestMapping("/rider-manage-account")
    public String riderManageAccount() {
        return "rider/riderManageAccount";
    }

    @ResponseBody
    @RequestMapping("/rider-usermanagement-details")
    public UserManagementDataDto userManagementDetails(HttpServletRequest req) {
        UserManagementDataDto userManagementDataDto = riderOtherService.getUserManagementDetails(req);
        return userManagementDataDto;
    }

    @ResponseBody
    @PostMapping("/update-personal-details")
    public ResponseEntity<String> riderPersonalDetailSubmit(@Valid @RequestBody RiderUMPersonalDetailDto riderUMPersonalDetailDto, HttpServletRequest request) {
        try {
            riderOtherService.riderPersonalDetailSubmit(riderUMPersonalDetailDto, request);
            return new ResponseEntity<>("Personal details updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update personal details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/update-login-details")
    public ResponseEntity<String> updateLoginDetails(@Valid @RequestBody RiderUMLoginDetails riderUMLoginDetails, HttpServletRequest request) {
        try {
            String result = riderOtherService.sendPassToService(riderUMLoginDetails, request);
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
            String newProfilePhotoPath = riderOtherService.updateProfilePic(riderUMUpdateProfileLogo, session);
            String relativePath = "/UrbanRides/resources/uploads/riderDocuments" + newProfilePhotoPath;
            return new ResponseEntity<>(relativePath, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update profile photo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/rider-logout")
    public String riderLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "landingPage/landingPage";
    }

}

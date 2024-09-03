package com.urbanrides.controller;


import com.urbanrides.dtos.*;
import com.urbanrides.exceptions.CustomExceptions;
import com.urbanrides.exceptions.CustomValidationException;
import com.urbanrides.exceptions.InsufficientFundsException;
import com.urbanrides.service.CabBookingService;
import com.urbanrides.service.LoginServices;
import com.urbanrides.service.RiderOtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rider")
public class RiderController {

    @Autowired
    private CabBookingService cabBookingService;

    @Autowired
    private RiderOtherService riderOtherService;

    @Autowired
    private LoginServices loginServices;

    @RequestMapping("/rider-personal-details")
    public String riderPersonalDetails() {
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
        try {
            String toasterMsg = cabBookingService.generalRide(riderNormalRideDto);
            return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
        } catch (CustomExceptions e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/cancel-ride-submit")
    public ResponseEntity<String> riderNormalRideSubmit(@RequestParam("cancelation-reason") String cancellationReason, @RequestParam("trip-id") int tripId) {
        String toasterMsg = cabBookingService.cancelRide(cancellationReason, tripId);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @PostMapping("/rider-reach-info")
    @ResponseBody
    public int riderReachInfo(@RequestBody RiderReachInfo riderReachInfo) {
        int generalTripDetailId = cabBookingService.saveGeneralTripInfo(riderReachInfo);
        return generalTripDetailId;
    }

    @RequestMapping("/blocked")
    public String captainBlock() {
        return "rider/riderBlockedPage";
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
    public ResponseEntity<Map<String, String>> rideRattingSubmit(@Valid @RequestBody RiderRattingConclude riderRattingConclude, BindingResult bindingResult) throws MethodArgumentNotValidException {
        Map<String, String> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            Method method = ReflectionUtils.findMethod(getClass(), "rideRattingSubmit", RiderRattingConclude.class, BindingResult.class, HttpSession.class);
            MethodParameter methodParameter = new MethodParameter(method, 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        try {
            cabBookingService.saveRattingInfo(riderRattingConclude);
            response.put("message", "Rating submitted successfully.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InsufficientFundsException e) {
            response.put("message", "Insufficient funds: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.put("message", "An unexpected error occurred: ");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-captain-details")
    @ResponseBody
    public ResponseEntity<?> getCaptainDetails() {
        try {
            List<RiderCaptainDetailsOnMapDto> captainDetails = cabBookingService.getFreeCaptain();
            return new ResponseEntity<>(captainDetails, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/package-ride-submit")
    public ResponseEntity<Map<String, String>> packageRideSubmit(@Valid @RequestBody PackageServiceDto packageServiceDto) {
        Map<String, String> response = new HashMap<>();
        try {
            cabBookingService.savePackageTripDetails(packageServiceDto);
            response.put("message", "Package trip details saved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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

    @ResponseBody
    @PostMapping("/rider-get-support")
    public ResponseEntity<Map<String, String>> saveGetSupport(@Valid @ModelAttribute RiderGetSupportDto riderGetSupportDto, BindingResult bindingResult, HttpSession session) throws MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            Method method = ReflectionUtils.findMethod(getClass(), "saveGetSupport", RiderGetSupportDto.class, BindingResult.class, HttpSession.class);
            MethodParameter methodParameter = new MethodParameter(method, 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        try {
            riderOtherService.getSupportSaveToLogs(riderGetSupportDto, session);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Support request submitted successfully.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to submit the complaint: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalStateException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/rider-my-trip")
    public String riderMyTrip() {
        return "rider/riderMyTrips";
    }

    @ResponseBody
    @RequestMapping("/rider-my-trip-details")
    public List<RiderMyTripDataDto> getRiderMyTrip() {
        List<RiderMyTripDataDto> listOfRiderTripDetails = riderOtherService.getTripDetails();
        return listOfRiderTripDetails;
    }

    @RequestMapping("/rider-wallet")
    public String riderWallet(Model model) {
        double walletAMount = riderOtherService.getAmount();
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
    public ResponseEntity<Map<String, String>> updateWalletAmount(@RequestParam double amount) {
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
        UserManagementDataDto userManagementDataDto = riderOtherService.getUserManagementDetails();
        return userManagementDataDto;
    }

    @ResponseBody
    @PostMapping("/update-personal-details")
    public ResponseEntity<Map<String, String>> riderPersonalDetailSubmit(@Valid @RequestBody RiderUMPersonalDetailDto riderUMPersonalDetailDto, HttpServletRequest request) {
        return riderOtherService.riderPersonalDetailSubmit(riderUMPersonalDetailDto);
    }

    @ResponseBody
    @PostMapping("/update-login-details")
    public ResponseEntity<Map<String, String>> updateLoginDetails(@Valid @RequestBody RiderUMLoginDetails riderUMLoginDetails, HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            String result = riderOtherService.sendPassToService(riderUMLoginDetails, request);
            if (result != null) {
                response.put("message", result);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            response.put("message", "Login details updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Failed to update login details: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/update-profile-photo")
    public ResponseEntity<Map<String, String>> updateProfilePhoto(@Valid @ModelAttribute RiderUMUpdateProfileLogo riderUMUpdateProfileLogo, HttpSession session, BindingResult bindingResult) throws MethodArgumentNotValidException {
        Map<String, String> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            response.put("error", "Only JPG and PNG files are allowed of size less than 1 mb");
            throw new CustomValidationException(response);
        }
        try {
            response = riderOtherService.updateProfilePic(riderUMUpdateProfileLogo, session);
            if (response.containsKey("error")) {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            response.put("error", "Failed to update profile photo: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/search-support-request", method = RequestMethod.GET)
    public ResponseEntity<?> getSupportRequestById(@RequestParam String id) {
        SupportRequestDataDto supportRequestData = riderOtherService.findSupportRequestById(id);

        if (supportRequestData != null) {
            return ResponseEntity.ok(supportRequestData);
        } else {
            return ResponseEntity.notFound().build();
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

package com.urbanrides.controller;


import com.urbanrides.dtos.*;
import com.urbanrides.exceptions.CustomExceptions;
import com.urbanrides.service.CabBookingService;
import com.urbanrides.service.CabConfirming;
import com.urbanrides.service.CaptainOtherService;
import com.urbanrides.service.LoginServices;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
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
        return "captain/captainPersonalDetails";
    }

    @RequestMapping("/captain-document-details-reupload")
    public String captainPersonalDetailsReupload(HttpServletRequest req, Model m) {
        String captainName = loginServices.getCapatainName(req);
        m.addAttribute("captainName", captainName);
        return "captain/captainPersonalDetailsReupload";
    }

    @ResponseBody
    @GetMapping("/get-captain-document-reupload-details")
    public ResponseEntity<?> getDocReuploadData() {
        try {
            // Replace this with actual service call to get approval data
            CaptainReuploadDataRendering captainReuploadDataRendering = loginServices.getCaptainReuploadData();
            return ResponseEntity.ok(captainReuploadDataRendering);

        } catch (Exception e) {
            // Log the exception and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch document approval data.");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/captain-reupload-document-details-submit", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> captainReuploadDocumentSubmit(@ModelAttribute("captainReuploadDataDto") CaptainReuploadDataDto captainReuploadDataDto, HttpServletRequest request, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getAllErrors().forEach(error -> errors.put("error", error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        ResponseEntity<Map<String, String>> responseEntity;
        try {
            responseEntity = loginServices.saveCaptainDocumentReupload(captainReuploadDataDto, session, request);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }


    @ResponseBody
    @PostMapping("/captain-personal-details-submit")
    public ResponseEntity<String> captainPersonalDetailSubmit(@Valid @RequestBody RiderPersonalDetailsDto riderPersonalDetailsDto, HttpServletRequest request) {
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
    public ResponseEntity<Map<String, String>> registerUser(@Valid @ModelAttribute("captainPersonalDetailsDto") CaptainPersonalDetailsDto captainPersonalDetailsDto, BindingResult bindingResult, HttpServletRequest request, HttpSession session) throws MethodArgumentNotValidException {
        Map<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            Method method = ReflectionUtils.findMethod(getClass(), "saveGetSupport", CaptainPersonalDetailsDto.class, BindingResult.class, HttpSession.class);
            MethodParameter methodParameter = new MethodParameter(method, 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        try {
            loginServices.saveCaptainPersonalDetails(captainPersonalDetailsDto, session, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", "An error occurred while saving captain personal details: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



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
    public List<CaptainAllTripsData> getAllTripData() {
        List<CaptainAllTripsData> listOfTrips = cabConfirming.getTripsData();
        return listOfTrips;
    }

    @ResponseBody
    @PostMapping("/accept-ride")
    public ResponseEntity<?> acceptRide(@RequestParam("tripId") int tripId) {
        try {
            RiderInfoDto riderInfoDto = cabBookingService.acceptRideCaptainSide(tripId);
            cabBookingService.acceptRideRiderSide(tripId);
            return ResponseEntity.ok(riderInfoDto);
        } catch (CustomExceptions ex) {
            System.out.println("Exception in handler: " + ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }





    @RequestMapping(value = "/save-captain-location", method = RequestMethod.POST)
    public ResponseEntity<String> saveCaptainLocation(@RequestParam("address") String address) {
        try {
            System.out.println("we rare");
            cabBookingService.saveCaptainLocation(address);
            return ResponseEntity.ok("Address saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving address: " + e.getMessage());
        }
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
    public ResponseEntity<Map<String, String>> updateWalletAmount(@RequestParam double amount) {
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
    public ResponseEntity<Map<String, String>> captainPersonalDetails(@Valid @RequestBody RiderUMPersonalDetailDto riderUMPersonalDetailDto, HttpServletRequest request) {
        return captainOtherService.captainPersonalDetailSubmit(riderUMPersonalDetailDto);
    }

    @PostMapping("/accept-package-ride")
    public ResponseEntity<Map<String, String>> acceptPackageRide(@RequestBody AcceptRideRequestDto request) {
        Map<String, String> response = new HashMap<>();

        try {
            captainOtherService.acceptPackageRide(request.getTripId());
            response.put("message", "Ride accepted successfully.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("errors", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (IllegalStateException e) {
            response.put("errors", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (RuntimeException e) {
            response.put("errors", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // added .status()
        }
    }


    @ResponseBody
    @PostMapping("/update-login-details")
    public ResponseEntity<Map<String, String>> updateLoginDetails(@Valid @RequestBody RiderUMLoginDetails riderUMLoginDetails, HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            String result = captainOtherService.sendPassToService(riderUMLoginDetails, request);
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


    @PostMapping("/update-profile-photo")
    public ResponseEntity<Map<String, String>> updateProfilePhoto(@Valid @ModelAttribute RiderUMUpdateProfileLogo riderUMUpdateProfileLogo, HttpSession session, BindingResult bindingResult) {
        Map<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("error", "Only JPG and PNG files are allowed of size less than 1 mb");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            String newProfilePhotoPath = captainOtherService.updateProfilePic(riderUMUpdateProfileLogo, session);
            String relativePath = "/UrbanRides/resources/uploads/riderDocuments" + newProfilePhotoPath;
            response.put("message", relativePath);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            response.put("error", "Failed to update profile photo: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ResponseBody
    @PostMapping("/captain-get-support")
    public ResponseEntity<Map<String, String>> saveGetSupport(@Valid @ModelAttribute RiderGetSupportDto riderGetSupportDto, HttpSession session, BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            Method method = ReflectionUtils.findMethod(getClass(), "saveGetSupport", RiderGetSupportDto.class, BindingResult.class, HttpSession.class);
            MethodParameter methodParameter = new MethodParameter(method, 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        try {
            captainOtherService.getSupportSaveToLogs(riderGetSupportDto, session);
            // Construct success response with a message
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

    @RequestMapping("/captain-my-trip")
    public String captainMyRides() {
        return "captain/captainMyTrips";
    }

    @RequestMapping("/blocked")
    public String captainBlock() {
        return "captain/captainBlockedPage";
    }


    @RequestMapping("/captain-notifications")
    public String captainNotifications(Model model) {
        List<NotificationDataDto> notiList = captainOtherService.getNotificationData();

        if (notiList.isEmpty()) {
            model.addAttribute("noNotifications", "No new notifications in the last 5 days.");
        } else {
            model.addAttribute("notificationDataDto", notiList);
        }

        return "captain/captainNotification";
    }

    @ResponseBody
    @RequestMapping("/captain-my-trip-details")
    public List<RiderMyTripDataDto> getCaptainMyTrip() {
        List<RiderMyTripDataDto> listOfRiderTripDetails = captainOtherService.getTripDetails();
        return listOfRiderTripDetails;
    }

    @ResponseBody
    @RequestMapping("/captain-package-trip-details")
    public List<CaptainPackageTripsDataDto> getCaptainPackageTripDetails() {
        List<CaptainPackageTripsDataDto> captainPackageTripsDataList = captainOtherService.getCaptainPackageTripData();
        return captainPackageTripsDataList;
    }

    //    @PostMapping("/conclude-ride-rent-taxi")
//    public ResponseEntity<Map<String, Object>> concludeRideRentTaxi(@RequestBody ConcludeRideRequestRentTaxDto request) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            captainOtherService.concludeRentTaxi(request);
//            response.put("status", "success");
//            response.put("message", "Rent taxi ride concluded successfully");
//            return ResponseEntity.ok(response);
//        } catch (IllegalArgumentException e) {
//            response.put("status", "error");
//            response.put("message", "Invalid request: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        } catch (IllegalStateException e) {
//            response.put("status", "error");
//            response.put("message", "Application state error: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        } catch (Exception e) {
//            response.put("status", "error");
//            response.put("message", "An error occurred: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
    @PostMapping("/conclude-ride-rent-taxi")
    public ResponseEntity<Map<String, String>> concludeRideRentTaxi(@Valid @RequestBody ConcludeRideRequestRentTaxDto concludeRideRequestRentTaxDto)  {
        Map<String, String> response = new HashMap<>();
//


        try {
            captainOtherService.concludeRentTaxi(concludeRideRequestRentTaxDto);
            response.put("message", "Rent taxi ride concluded successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            response.put("errors", "Invalid request: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            response.put("errors", "Application state error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            response.put("errors", "An error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/conclude-ride-daily-pickup")
    public ResponseEntity<Map<String, String>> concludeRideDailyPickup(@RequestBody ConcludeRideRequestDailyPickUpDto request) {
        Map<String, String> response = new HashMap<>();
        try {
            captainOtherService.concludeDailyPickUp(request);
            response.put("message", "Daily taxi ride concluded successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            response.put("errors", "Invalid request: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            response.put("errors", "Application state error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            response.put("errors", "An error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping("/captain-package-rides")
    public String captainPackageRides() {
        return "captain/captainPackageRides";
    }

    @RequestMapping("/captain-logout")
    public String captainLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        System.out.println("logut is fired");
        if (session != null) {
            session.invalidate();
        }
        return "landingPage/landingPage";
    }

    @RequestMapping(value = "/search-support-request", method = RequestMethod.GET)
    public ResponseEntity<?> getSupportRequestById(@RequestParam String id) {
        SupportRequestDataDto supportRequestData = captainOtherService.findSupportRequestById(id);

        if (supportRequestData != null) {
            return ResponseEntity.ok(supportRequestData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

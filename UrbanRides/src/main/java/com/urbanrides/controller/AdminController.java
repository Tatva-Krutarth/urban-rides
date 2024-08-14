package com.urbanrides.controller;

import com.urbanrides.dtos.*;
import com.urbanrides.exceptions.CustomValidationException;
import com.urbanrides.service.AdminService;
import com.urbanrides.service.LoginServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")


public class AdminController {


    @Autowired
    private AdminService adminService;
    @Autowired
    private LoginServices loginServices;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @ResponseBody
    @PostMapping("/admin-personal-details-submit")
    public ResponseEntity<String> riderPersonalDetailSubmit(@Valid @RequestBody RiderPersonalDetailsDto riderPersonalDetailsDto, HttpServletRequest request) {
        String toasterMsg = loginServices.adminPersonalDetailSubmit(riderPersonalDetailsDto, request);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @RequestMapping("/admin-personal-details")
    public String riderPersonalDetails() {
        System.out.println("inside the demo");
        return "admin/adminPersonalDetails";
    }

    @RequestMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin/adminDashboard";
    }

    @PostMapping("/admin-accept-request")
    public ResponseEntity<String> acceptRequest(@RequestParam int id) {
        boolean success = adminService.acceptRequest(id);
        if (success) {
            return ResponseEntity.ok("Request accepted successfully.");
        } else {
            return ResponseEntity.status(400).body("Request could not be accepted.");
        }
    }

    @PostMapping("/admin-complete-request")
    public ResponseEntity<String> completeRequest(@RequestParam int id) {
        boolean success = adminService.completeRequest(id);
        if (success) {
            return ResponseEntity.ok("Request accepted successfully.");
        } else {
            return ResponseEntity.status(400).body("Request could not be accepted.");
        }
    }

    @ResponseBody
    @RequestMapping("/admin-count-data")
    public AdminCountData getCountData() {
        AdminCountData adminCountData = adminService.getDashCount();
        return adminCountData;
    }

    //    @ResponseBody
//    @RequestMapping("/admin-query-data")
//    public List<AdminQuerries> getDashData() {
//        List<AdminQuerries> adminQuerries = adminService.getSupportData();
//        return adminQuerries;
//    }
    @ResponseBody
    @GetMapping("/query-data")
    public Page<AdminQuerries> getDashData(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return adminService.getSupportData(pageable);
    }

    @ResponseBody
    @GetMapping("/admin-running-querry-data")
    public Page<AdminQuerries> getRunningData(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return adminService.getRunningData(pageable);
    }


    @ResponseBody
    @GetMapping("/admin-completed-querry-data")
    public Page<AdminQuerries> getCompletedData(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return adminService.getCompletedData(pageable);
    }


    @ResponseBody
    @RequestMapping("/admin-user-all-data")
    public List<AdminUserManagementAllDto> userManagementAllData() {
        List<AdminUserManagementAllDto> adminUserRideData = adminService.getAllUsersData();
        return adminUserRideData;
    }

    @ResponseBody
    @RequestMapping("/admin-captain-all-data")
    public List<AdminCaptainApproveDataDto> adminCaptainAllData() {
        List<AdminCaptainApproveDataDto> captainData = adminService.getAllCaptainData();
        return captainData;
    }

    @ResponseBody
    @PostMapping("/admin-approve-captain-docs")
    public ResponseEntity<Map<String, Object>> approveCaptainDocs(@Valid @RequestBody AproveCaptainsDataDto aproveCaptainsDataDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            adminService.adminCaptainApproveDoc(aproveCaptainsDataDto);
            response.put("message", "Captain documents approved successfully.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.put("message", "An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ResponseBody
    @RequestMapping("/admin-user-rider-data")
    public List<AdminUserManagementAllDto> userManagementRiderData() {
        List<AdminUserManagementAllDto> adminUserRideData = adminService.getRiderUsersData();
        return adminUserRideData;
    }

    @ResponseBody
    @RequestMapping("/admin-user-captain-data")
    public List<AdminUserManagementAllDto> userManagementCaptainData() {
        List<AdminUserManagementAllDto> adminUserRideData = adminService.getCaptainUsersData();
        return adminUserRideData;
    }

    @ResponseBody
    @RequestMapping("/admin-user-admin-data")
    public List<AdminUserManagementAllDto> userManagementAdminData() {
        List<AdminUserManagementAllDto> adminUserRideData = adminService.getAdminUsersData();
        return adminUserRideData;
    }

    @ResponseBody
    @RequestMapping("/admin-user-block-data")
    public List<AdminUserManagementAllDto> userManagementBlockData() {
        List<AdminUserManagementAllDto> adminUserRideData = adminService.getBlockUsersData();
        return adminUserRideData;
    }


    @PostMapping("/admin-block-user")
    public ResponseEntity<String> blockUser(@RequestParam int riderUserId) {
        boolean success = adminService.blockUser(riderUserId);
        if (success) {
            return ResponseEntity.ok("User blocked successfully.");
        } else {
            return ResponseEntity.status(400).body("User could not be blocked.");
        }
    }

    @PostMapping("/admin-unblock-user")
    public ResponseEntity<String> unblockUser(@RequestParam int riderUserId) {
        boolean success = adminService.unblockUser(riderUserId);
        if (success) {
            return ResponseEntity.ok("User blocked successfully.");
        } else {
            return ResponseEntity.status(400).body("User could not be blocked.");
        }
    }


    @RequestMapping(value = "/admin-rides-filter-trips", method = RequestMethod.POST)
    public @ResponseBody List<RiderMyTripDataDto> filterTrips(@RequestBody AdminRidesFilterData filterData) {
        List<RiderMyTripDataDto> riderMyTripList = adminService.ridesFilterData(filterData);
        return riderMyTripList;
    }

    @RequestMapping("/admin-my-profile")
    public String adminProfile() {
        return "admin/adminMyProfile";
    }

    @RequestMapping("/ride-management")
    public String riderManagement() {
        return "admin/adminRideManagement";
    }

    @RequestMapping("/user-management")
    public String userManagement() {
        return "admin/adminUserManagement";
    }

    @RequestMapping("/captain-approve")
    public String adminApproveCaptain() {
        return "admin/adminApproveCaptain";
    }

    @RequestMapping("/admin-logout")
    public String adminLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "landingPage/landingPage";
    }

    @RequestMapping("/rider-manage-account")
    public String riderManageAccount() {
        return "rider/riderManageAccount";
    }

    @ResponseBody
    @RequestMapping("/rider-usermanagement-details")
    public UserManagementDataDto userManagementDetails() {
        UserManagementDataDto userManagementDataDto = adminService.getUserManagementDetails();
        return userManagementDataDto;
    }


    @ResponseBody
    @PostMapping("/update-personal-details")
    public ResponseEntity<Map<String, String>> adminPersonalDetailSubmit(@Valid @RequestBody RiderUMPersonalDetailDto riderUMPersonalDetailDto, HttpServletRequest request) {
        return adminService.adminPersonalDetailSubmit(riderUMPersonalDetailDto);
    }

    @ResponseBody
    @PostMapping("/update-login-details")
    public ResponseEntity<Map<String, String>> updateLoginDetails(@Valid @RequestBody RiderUMLoginDetails riderUMLoginDetails) {
        Map<String, String> response = new HashMap<>();
        try {
            String result = adminService.sendPassToService(riderUMLoginDetails);
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
            String newProfilePhotoPath = adminService.updateProfilePic(riderUMUpdateProfileLogo, session);
            String relativePath = "/UrbanRides/resources/uploads/riderDocuments" + newProfilePhotoPath;
            response.put("message", relativePath);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            response.put("error", "Failed to update profile photo: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

package com.urbanrides.controller;

import com.urbanrides.dtos.*;
import com.urbanrides.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")


public class AdminController {


    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin/adminDashboard";
    }

    @ResponseBody
    @RequestMapping("/admin-count-data")
    public AdminCountData getCountData() {
        AdminCountData adminCountData = adminService.getDashCount();
        return adminCountData;
    }

    @ResponseBody
    @RequestMapping("/admin-querry-data")
    public List<AdminQuerries> getDashData() {
        List<AdminQuerries> adminQuerries = adminService.getSupportData();
        return adminQuerries;
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
    public UserManagementDataDto userManagementDetails(HttpServletRequest req) {
        UserManagementDataDto userManagementDataDto = adminService.getUserManagementDetails();
        return userManagementDataDto;
    }

    @ResponseBody
    @PostMapping("/update-personal-details")
    public ResponseEntity<String> riderPersonalDetailSubmit(@Valid @RequestBody RiderUMPersonalDetailDto riderUMPersonalDetailDto, HttpServletRequest request) {
        try {
            adminService.riderPersonalDetailSubmit(riderUMPersonalDetailDto, request);
            return new ResponseEntity<>("Personal details updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update personal details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/update-login-details")
    public ResponseEntity<String> updateLoginDetails(@Valid @RequestBody RiderUMLoginDetails riderUMLoginDetails, HttpServletRequest request) {
        try {
            String result = adminService.sendPassToService(riderUMLoginDetails, request);
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
            String newProfilePhotoPath = adminService.updateProfilePic(riderUMUpdateProfileLogo, session);
            String relativePath = "/UrbanRides/resources/uploads/riderDocuments" + newProfilePhotoPath;
            return new ResponseEntity<>(relativePath, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update profile photo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.urbanrides.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")

public class AdminController {


    @RequestMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin/adminDashboard";
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
}

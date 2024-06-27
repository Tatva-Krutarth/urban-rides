package com.urbanrides.controller;

import com.urbanrides.dtos.*;
import com.urbanrides.exceptions.CustomExceptions;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.urbanrides.service.LoginServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.Map;

@Controller
public class UserLogin {


    //    private static final Logger log = LoggerFactory.getLogger(UserLogin.class);
    //    private static final Logger log = LoggerFactory.getLogger(UserLogin.class);
    @Autowired
    private LoginServices loginServices;

    @RequestMapping("/")
    public String langingPg() {
        System.out.println("Langing-page");
        return "landingPage/landingPage";
    }

    @RequestMapping("/user-registration/{accountType}")
    public String userRegistration(@PathVariable("accountType") int accountType, Model model) {
        System.out.println(accountType);
        model.addAttribute("accountType", accountType);
        return "login/userRegistration";
    }

    @ResponseBody
    @PostMapping("/user-registration-otp")
    public ResponseEntity<String> userRegistrationGetOtp(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        String toasterMsg = loginServices.otpService(userRegistrationDto);
        System.out.println(toasterMsg);
        System.out.println(userRegistrationDto);
        if (toasterMsg.contains("error")) {
            return new ResponseEntity<>(toasterMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/user-registration-submit")
    public ResponseEntity<String> userRegistrationSubmit(@Valid @RequestBody UserRegistrationDto userRegistrationDto, HttpServletRequest request) {
        System.out.println("user-otp");
        System.out.println(userRegistrationDto);
        String toasterMsg = loginServices.submitRegistration(userRegistrationDto, request);
        System.out.println(toasterMsg);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @RequestMapping("/rider-personal-details")
    public String riderPersonalDetails() {
        return "login/riderPersonalDetails";
    }

    @ResponseBody
    @PostMapping("/rider-personal-details-submit")
    public ResponseEntity<String> riderPersonalDetailSubmit(@Valid @RequestBody RiderPersonalDetailsDto riderPersonalDetailsDto, HttpServletRequest request) {
        String toasterMsg = loginServices.riderPersonalDetailSubmit(riderPersonalDetailsDto, request);
        System.out.println(toasterMsg);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @RequestMapping("/user-login")
    public String userLogin() {
        return "login/userLogin";
    }

    @ResponseBody
    @PostMapping("/user-login-submit")
    public ResponseEntity<String> riderLogin(@Valid @RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
        System.out.println("rider-login");
        System.out.println(userLoginDto);
        String toasterMsg = loginServices.riderLoginService(userLoginDto, request);
        System.out.println(toasterMsg);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @RequestMapping("/forget-password")
    public String forgetPassword() {
        return "login/forgetPassword";
    }

    @RequestMapping(path = "/forget-pass-otp", method = RequestMethod.POST)
    @ResponseBody
    public String sendLinkSubmit(@RequestParam("email") String email, Model model) {
        String toasterMsg = loginServices.forgetOtpService(email);
        return toasterMsg;
    }


    @ResponseBody
    @PostMapping("/forget-pass-submit")
    public ResponseEntity<String> forgetPassSubmit(@Valid @RequestBody ForgetPassDto forgetPassDto) {
        String toasterMsg = loginServices.forgetPassSubmit(forgetPassDto);
        System.out.println(toasterMsg);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @RequestMapping("/captain-personal-details")
    public String captainPersonalDetails() {
        return "login/captainPersonalDetails";
    }

    @RequestMapping(value = "/captain-personal-details-submit", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@Valid @ModelAttribute("registrationDataDto") CaptainPersonalDetailsDto captainPersonalDetailsDto, HttpServletRequest request, HttpSession session, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws MethodArgumentNotValidException {
        String toasterMsg = loginServices.saveCaptainPersonalDetails(captainPersonalDetailsDto, session, request);
        System.out.println(toasterMsg);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @RequestMapping("/captain-support")
    public String captainSupport() {
        return "login/captainSupport";
    }

    @RequestMapping("/error-code-404")
    public String errorCode404() {
        return "errorPages/errorCode404";
    }
}



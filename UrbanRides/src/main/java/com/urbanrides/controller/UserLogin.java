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
        model.addAttribute("accountType", accountType);
        return "login/userRegistration";
    }

    @ResponseBody
    @PostMapping("/user-registration-otp")
    public ResponseEntity<String> userRegistrationGetOtp(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        String toasterMsg = loginServices.otpService(userRegistrationDto);
        if (toasterMsg.contains("error")) {
            return new ResponseEntity<>(toasterMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/user-registration-submit")
    public ResponseEntity<String> userRegistrationSubmit(@Valid @RequestBody UserRegistrationDto userRegistrationDto, HttpServletRequest request) {
        String toasterMsg = loginServices.submitRegistration(userRegistrationDto, request);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }


    @RequestMapping("/user-login")
    public String userLogin() {
        return "login/userLogin";
    }

    @ResponseBody
    @PostMapping("/user-login-submit")
    public ResponseEntity<String> riderLogin(@Valid @RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
        String toasterMsg = loginServices.riderLoginService(userLoginDto, request);
        System.out.println("this isthe backend secreate msg");
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



    @RequestMapping("/error-code-404")
    public String errorCode404() {
        return "errorPages/errorCode404";
    }

    @RequestMapping("/no-session")
    public String noSessionFound() {
        return "errorPages/noSessionFound";
    }
}



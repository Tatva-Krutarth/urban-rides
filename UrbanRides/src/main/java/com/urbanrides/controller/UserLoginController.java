package com.urbanrides.controller;

import com.urbanrides.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.urbanrides.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserLoginController {

    //    private static final Logger log = LoggerFactory.getLogger(UserLogin.class);
    //    private static final Logger log = LoggerFactory.getLogger(UserLogin.class);
    @Autowired
    private LoginService loginServices;

    @RequestMapping("/")
    public String langingPg() {
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
        try {
            loginServices.otpService(userRegistrationDto);
            return new ResponseEntity<>("Email Sent Successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ResponseBody
    @PostMapping("/user-registration-submit")
    public ResponseEntity<String> userRegistrationSubmit(@Valid @RequestBody UserRegistrationDto userRegistrationDto, HttpServletRequest request) {
        try {
            loginServices.submitRegistration(userRegistrationDto, request);
            if (userRegistrationDto.getAcccoutTypeId() == 3) {
                return new ResponseEntity<>("Rider Registered", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Captain Registered", HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping("/user-login")
    public String userLogin() {
        return "login/userLogin";
    }

    @ResponseBody
    @PostMapping("/user-login-submit")
    public ResponseEntity<String> riderLogin(@Valid @RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
        String toasterMsg = loginServices.riderLoginService(userLoginDto, request);
        return new ResponseEntity<>(toasterMsg, HttpStatus.OK);
    }

    @RequestMapping("/forget-password")
    public String forgetPassword() {
        return "login/forgetPassword";
    }

    @RequestMapping(path = "/forget-pass-otp", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> sendLinkSubmit(@RequestParam("email") String email) {
        try {
            loginServices.forgetOtpService(email);
            return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ResponseBody
    @PostMapping("/forget-pass-submit")
    public ResponseEntity<String> forgetPassSubmit(@Valid @RequestBody ForgetPassDto forgetPassDto) {
        String toasterMsg = loginServices.forgetPassSubmit(forgetPassDto);
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



package com.urbanrides.service;

//import com.urbanrides.dao.UserDetailsDao;

import com.urbanrides.dao.*;
import com.urbanrides.dtos.*;
import com.urbanrides.helper.*;
import com.urbanrides.model.*;


import com.urbanrides.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
public class LoginServices {

    @Autowired
    UserRegistrationDao userRegistrationDao;
    OtpGenerator otpGenerator;
    @Autowired
    CommonValidation commonValidation;
    @Autowired
    PasswordToHash passwordToHash;

    @Autowired
    UsersDao usersDao;
    @Autowired
    EmailSend emailSend;

    @Autowired
    UserDetailsDao userDetailsDao;
    @Autowired
    private ConversionService conversionService;

    @Autowired
    private DateTimeConverter dateTimeConverter;

    @Autowired
    private CaptainDetailsDao captainDetailsDao;

    public String otpService(UserRegistrationDto userRegistrationDto) {


        //validation
        String error = validateUserRegistration(userRegistrationDto);
        if (error != null) {
            return error;
        }
        //checking id the otp is for first time

        OtpLogs eachLog = userRegistrationDao.getOtpLogsByEmail(userRegistrationDto.getEmail());
        if (eachLog == null) {
            //first time
            System.out.println("First Time");
            OtpLogs otpLogs = setOtpLogData(userRegistrationDto);

            //save
            try {
//                this.emailSend.userRegistrationOtp(userRegistrationDto);
            } catch (Exception e) {
                System.out.println("Email not send");
                return "Email not send";
            }
            userRegistrationDao.saveUser(otpLogs);
            return "Email Send Successfully";
        } else {
            System.out.println("Not first Time");
            LocalDate createdDate = eachLog.getCreatedDate();
            if (createdDate.isEqual(LocalDate.now())) {
                System.out.println(LocalTime.now());
                System.out.println(eachLog.getOptReqSendTime());
                if (Math.abs(ChronoUnit.MINUTES.between(eachLog.getOptReqSendTime(), LocalDateTime.now())) <= 2) {
                    return "Please Wait for 2 minutes before getting new otp";
                } else {
                    System.out.println("Same day after 2 minutes");
                    //delete old Entries;
                    userRegistrationDao.deleteOtpLogsByEmail(userRegistrationDto.getEmail());
                    OtpLogs otpLogs = setOtpLogData(userRegistrationDto);
                    try {
//                        this.emailSend.userRegistrationOtp(userRegistrationDto);
                    } catch (Exception e) {
                        System.out.println("Email not send");
                        return "Email not send";
                    }
                    userRegistrationDao.saveUser(otpLogs);
                    return "Email Send Successfully";
                }
            } else {
                System.out.println("Next day");
                OtpLogs otpLogs = setOtpLogData(userRegistrationDto);
                userRegistrationDao.deleteOtpLogsByEmail(userRegistrationDto.getEmail());
                try {
//                    this.emailSend.userRegistrationOtp(userRegistrationDto);
                } catch (Exception e) {
                    System.out.println("Email not send");
                    return "Email not send";
                }
                userRegistrationDao.saveUser(otpLogs);
                return "Email Send Successfully";
            }
        }
    }

    public OtpLogs setOtpLogData(UserRegistrationDto userRegistrationDto) {
        OtpLogs otpLogs = new OtpLogs();
        otpLogs.setEmail(userRegistrationDto.getEmail());
        otpLogs.setOptReqSendTime(LocalTime.now());
//        otpLogs.setAttempt(1);
        otpLogs.setGeneratedOtp(otpGenerator.generateOTP());
        otpLogs.setOtpPassed(false);
        return otpLogs;
    }

    public OtpLogs setOtpLogDataForgetData(String email) {
        OtpLogs otpLogs = new OtpLogs();
        otpLogs.setEmail(email);
        otpLogs.setOptReqSendTime(LocalTime.now());
//        otpLogs.setAttempt(1);
        otpLogs.setGeneratedOtp(otpGenerator.generateOTP());
        otpLogs.setOtpPassed(false);
        return otpLogs;
    }

    public String validateUserRegistration(UserRegistrationDto userRegistrationDto) {
        if (!commonValidation.isValidEmail(userRegistrationDto.getEmail())) {
            return "Invalid email address";
        }
        if (!commonValidation.isValidPassword(userRegistrationDto.getPassword())) {
            return "Password must contain be between 8 and 13 characters.";
        }
        if (!commonValidation.confirmPassword(userRegistrationDto.getPassword(), userRegistrationDto.getConfPass())) {
            return "Passwords do not match";
        }
        return null; // no errors found
    }

    public String emailValidation(String email) {
        if (!commonValidation.isValidEmail(email)) {
            return "Invalid email address";
        }
        return null; // no errors found
    }


    public String submitRegistration(UserRegistrationDto userRegistrationDto, HttpServletRequest request) {
        //validate data here
        OtpLogs eachLog = userRegistrationDao.getOtpLogsByEmail(userRegistrationDto.getEmail());
        if (eachLog == null) {
            //first time
            System.out.println("First Time");
            //OtpLogs otpLogs = setOtpLogData(userRegistrationDto);
            return "Get Otp first";
        } else {
            System.out.println("Not first Time");
            LocalDate createdDate = eachLog.getCreatedDate();
            if (createdDate.isEqual(LocalDate.now())) {
                System.out.println(LocalTime.now());
                System.out.println(eachLog.getOptReqSendTime());
                if (Math.abs(ChronoUnit.MINUTES.between(eachLog.getOptReqSendTime(), LocalDateTime.now())) <= 2) {

                    if (Integer.parseInt(userRegistrationDto.getOtp()) == eachLog.getGeneratedOtp()) {
                        //save user data here
                        String msgFromSumbitting = submitData(userRegistrationDto, request);
                        return msgFromSumbitting;
                    } else {
                        return "Invalid OTP";
                    }


                } else {
                    System.out.println("Same day after 2 minutes");
                    return "OTP Time is over get a new Otp";
                }
            } else {
                System.out.println("Next day");
                return "OTP Time is over get a new Otp";
            }
        }
    }


    public String submitData(UserRegistrationDto userRegistrationDto, HttpServletRequest request) {

        //custom validation
        String error = validateUserRegistration(userRegistrationDto);
        if (error != null) {
            return error;
        }
        if (!commonValidation.isValidOtp(userRegistrationDto.getOtp())) {
            return "Invalid otp format";
        }
        //checking if the user is already exist
        User user = usersDao.getUserByEmail(userRegistrationDto.getEmail());

        if (user == null) {
            //saving the user
            int userId = setUserData(userRegistrationDto);
            User justRegisteredUser = usersDao.getUserByUserId(userId);
            justRegisteredUser.setAccountStatus(1);
            usersDao.updateUser(justRegisteredUser);
            if (userRegistrationDto.getAcccoutTypeId() == 3) {
                HttpSession session = request.getSession();
                UserSessionObj userSessionObj = new UserSessionObj();
                userSessionObj.setUserId(justRegisteredUser.getUserId());
                userSessionObj.setAccountStatus(1);
                userSessionObj.setAccountTypeId(justRegisteredUser.getAccountType());
                session.setAttribute("riderSessionObj", userSessionObj);
                return "Rider Registered";
            } else {
                HttpSession session = request.getSession();
                UserSessionObj userSessionObj = new UserSessionObj();
                userSessionObj.setUserId(justRegisteredUser.getUserId());
                userSessionObj.setAccountStatus(1);
                userSessionObj.setAccountTypeId(justRegisteredUser.getAccountType());
                session.setAttribute("captainSessionObj", userSessionObj);
                return "Captain Registered";
            }
        } else {
            if (userRegistrationDto.getAcccoutTypeId() == 3) {
                return "Rider already exists";
            } else {
                return "Captain already exists";
            }
        }
    }

    public int setUserData(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setEmail(userRegistrationDto.getEmail());
        user.setAccountType(userRegistrationDto.getAcccoutTypeId());


        String saltString = passwordToHash.generateSalt();
        String hashedPasswordString = passwordToHash.hashPassword(userRegistrationDto.getPassword(), saltString);


        user.setSalt(saltString);
        user.setPasswordHash(hashedPasswordString);
        int userId = usersDao.saveUser(user);
        return userId;
    }


    public String riderPersonalDetailSubmit(RiderPersonalDetailsDto riderPersonalDetailsDto, HttpServletRequest request) {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(riderPersonalDetailsDto.getRiderFirstName());
        userDetails.setLastName(riderPersonalDetailsDto.getRiderLastName());
        userDetails.setPhone(riderPersonalDetailsDto.getPhone());
        userDetails.setAge(riderPersonalDetailsDto.getAge());


        HttpSession session = request.getSession();

        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("riderSessionObj");
        User user = usersDao.getUserByUserId(userSessionObj.getUserId());

        if (user == null) {
            return "User not found";
        }
        //checking if the userdetails are already filled
        user.setAccountStatus(2);


        UserDetails userDetailsChekcing = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());

        if (userDetailsChekcing != null) {
            return "User details already filled, please login";
        }
        userDetails.setUser(user);
        System.out.println(userDetails);
        userSessionObj.setAccountStatus(2);
        session.setAttribute("riderSessionObj", userSessionObj);
        usersDao.updateUser(user);

        userDetailsDao.saveUserDetails(userDetails);
        return "Login successful";
    }

    public String captainPersonalDetailSubmit(RiderPersonalDetailsDto riderPersonalDetailsDto, HttpServletRequest request) {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(riderPersonalDetailsDto.getRiderFirstName());
        userDetails.setLastName(riderPersonalDetailsDto.getRiderLastName());
        userDetails.setPhone(riderPersonalDetailsDto.getPhone());
        userDetails.setAge(riderPersonalDetailsDto.getAge());


        HttpSession session = request.getSession();

        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("captainSessionObj");
        User user = usersDao.getUserByUserId(userSessionObj.getUserId());

        if (user == null) {
            return "User not found";
        }
        //checking if the userdetails are already filled

        UserDetails userDetailsChekcing = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());

        if (userDetailsChekcing != null) {
            return "User details already filled, please login";
        }
        user.setAccountStatus(2);
        userSessionObj.setAccountStatus(2);
        session.setAttribute("captainSessionObj", userSessionObj);

        usersDao.updateUser(user);
        userDetails.setUser(user);
        System.out.println(userDetails);
        userDetailsDao.saveUserDetails(userDetails);
        return "Login successful";
    }

    public String getCapatainName(HttpServletRequest req) {

        HttpSession session = req.getSession();
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("captainSessionObj");
        UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());

        String capatainName = userDetails.getFirstName();
        return capatainName;
    }

    public String riderLoginService(UserLoginDto userLoginDto, HttpServletRequest request) {
        User user = usersDao.getUserByEmail(userLoginDto.getEmail());
        if (user == null) {
            return "User not found";
        } else {
            // Verify the password
            Boolean verified = passwordToHash.checkPassword(userLoginDto.getPassword(), user.getSalt(), user.getPasswordHash());
            if (verified) {


                if (user.getAccountType() == 3) {
                    HttpSession session = request.getSession();
                    UserSessionObj userSessionObj = new UserSessionObj();
                    userSessionObj.setUserId(user.getUserId());
                    userSessionObj.setAccountStatus(user.getAccountStatus());
                    userSessionObj.setAccountTypeId(user.getAccountType());
                    session.setAttribute("riderSessionObj", userSessionObj);
                    return "Login successful + " + user.getAccountType();
                } else {
                    HttpSession session = request.getSession();
                    UserSessionObj userSessionObj = new UserSessionObj();
                    userSessionObj.setUserId(user.getUserId());
                    userSessionObj.setAccountStatus(user.getAccountStatus());
                    userSessionObj.setAccountTypeId(user.getAccountType());
                    session.setAttribute("captainSessionObj", userSessionObj);
                    return "Login successful + " + user.getAccountType();
                }


//                HttpSession session = request.getSession();
//                UserSessionObj userSessionObj = new UserSessionObj();
//                userSessionObj.setUserId(user.getUserId());
//                userSessionObj.setAccountStatus(user.getAccountStatus());
//                userSessionObj.setAccountTypeId(user.getAccountType());
//                session.setAttribute("riderSessionObj", userSessionObj);
//
//                return "Login successful + " + user.getAccountType();
            } else {
                return "Invalid password ";
            }
        }
    }

    public String forgetOtpService(String email) {
        //validation
        String error = emailValidation(email);
        if (error != null) {
            return error;
        }
        //checking if the user is exist or not
        User user = usersDao.getUserByEmail(email);
        if (user == null) {
            return "User Doesn't exist";
        } else {
            // Process the user object
            System.out.println("User exists: ");
        }

        //checking id the otp is for first time
        OtpLogs eachLog = userRegistrationDao.getOtpLogsByEmail(email);
        if (eachLog == null) {
            //first time
            System.out.println("First Time");
            OtpLogs otpLogs = setOtpLogDataForgetData(email);

            //save
            try {
//                this.emailSend.userRegistrationOtp(userRegistrationDto);
            } catch (Exception e) {
                System.out.println("Email not send");
                return "Email not send";
            }
            userRegistrationDao.saveUser(otpLogs);
            return "Email Send Successfully";
        } else {
            System.out.println("Not first Time");
            LocalDate createdDate = eachLog.getCreatedDate();
            if (createdDate.isEqual(LocalDate.now())) {
                System.out.println(LocalTime.now());
                System.out.println(eachLog.getOptReqSendTime());
                if (Math.abs(ChronoUnit.MINUTES.between(eachLog.getOptReqSendTime(), LocalDateTime.now())) <= 2) {
                    return "Please Wait for 2 minutes before getting new otp";
                } else {
                    System.out.println("Same day after 2 minutes");
                    //delete old Entries;
                    userRegistrationDao.deleteOtpLogsByEmail(email);
                    OtpLogs otpLogs = setOtpLogDataForgetData(email);
                    try {
//                        this.emailSend.userRegistrationOtp(userRegistrationDto);
                    } catch (Exception e) {
                        System.out.println("Email not send");
                        return "Email not send";
                    }
                    userRegistrationDao.saveUser(otpLogs);
                    return "Email Send Successfully";
                }
            } else {
                System.out.println("Next day");
                OtpLogs otpLogs = setOtpLogDataForgetData(email);
                userRegistrationDao.deleteOtpLogsByEmail(email);
                try {
//                    this.emailSend.userRegistrationOtp(userRegistrationDto);
                } catch (Exception e) {
                    System.out.println("Email not send");
                    return "Email not send";
                }
                userRegistrationDao.saveUser(otpLogs);
                return "Email Send Successfully";
            }
        }
    }


    public String forgetPassSubmit(ForgetPassDto forgetPassDto) {
        // custom validation
        String error = forgetPassValidation(forgetPassDto);
        if (error != null) {
            return error;
        }
        //validate data here
        OtpLogs eachLog = userRegistrationDao.getOtpLogsByEmail(forgetPassDto.getEmail());
        if (eachLog == null) {
            //first time
            System.out.println("First Time");
            return "Get Otp first";
        } else {
            System.out.println("Not first Time");
            LocalDate createdDate = eachLog.getCreatedDate();
            if (createdDate.isEqual(LocalDate.now())) {
                System.out.println(LocalTime.now());
                System.out.println(eachLog.getOptReqSendTime());
                if (Math.abs(ChronoUnit.MINUTES.between(eachLog.getOptReqSendTime(), LocalDateTime.now())) <= 2) {
                    if (Integer.parseInt(forgetPassDto.getOtp()) == eachLog.getGeneratedOtp()) {
                        //update user data here
                        String msgFromSumbitting = updateData(forgetPassDto);
                        return msgFromSumbitting;
                    } else {
                        return "Invalid OTP";
                    }
                } else {
                    System.out.println("Same day after 2 minutes");
                    return "OTP Time is over get a new Otp";
                }
            } else {
                System.out.println("Next day");
                return "OTP Time is over get a new Otp";
            }
        }
    }

    public String updateData(ForgetPassDto forgetPassDto) {
        //checking if the user is already exist
        User user = usersDao.getUserByEmail(forgetPassDto.getEmail());

        if (user == null) {
            //User Not Found
            return "User Not Found";
        } else {
            //update the user
            updatePassword(user, forgetPassDto);
            return "Password updated successfully";
        }
    }

    public User updatePassword(User user, ForgetPassDto forgetPassDto) {
        String saltString = passwordToHash.generateSalt();
        String hashedPasswordString = passwordToHash.hashPassword(forgetPassDto.getPassword(), saltString);
        user.setSalt(saltString);
        user.setPasswordHash(hashedPasswordString);
        usersDao.updateUser(user);
        return user;
    }

    public String forgetPassValidation(ForgetPassDto forgetPassDto) {
        if (!commonValidation.isValidEmail(forgetPassDto.getEmail())) {
            return "Invalid email address";
        }
        if (!commonValidation.isValidPassword(forgetPassDto.getPassword())) {
            return "Password must contain be between 8 and 13 characters.";
        }
        if (!commonValidation.confirmPassword(forgetPassDto.getPassword(), forgetPassDto.getConfPass())) {
            return "Passwords do not match";
        }
        if (!commonValidation.isValidOtp(forgetPassDto.getOtp())) {
            return "Otp is of 4 Digit.";
        }
        return null; // no errors found
    }


    public String saveCaptainPersonalDetails(CaptainPersonalDetailsDto captainPersonalDetailsDto, HttpSession session, HttpServletRequest request) {
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("captainSessionObj");
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());

        if (captainDetails != null) {
            return "User document are already filled.";
        }

        int i = uploadDocumentCaptain(captainPersonalDetailsDto, session);
        if (i != 4) {
            return "Error While uploading the data";
        }
        //saving the data
        String msg = saveCaptainDetails(captainPersonalDetailsDto, request);
        return msg;
    }

    @Autowired
    VehicleTypeDao vehicleTypeDao;

    public String saveCaptainDetails(CaptainPersonalDetailsDto captainPersonalDetailsDto, HttpServletRequest request) {
        CaptainDetails captainDetails = new CaptainDetails();
        String originalFileName = captainPersonalDetailsDto.getProfilePhoto().getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        HttpSession session = request.getSession();
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("captainSessionObj");
        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
        user.setAccountStatus(3);
        userSessionObj.setAccountStatus(3);
        captainDetails.setProfilePhotoExtension(extension);
        captainDetails.setUser(user);
        VehicleType vehicleType = vehicleTypeDao.getVehicaleId(captainPersonalDetailsDto.getVehicleType());
        captainDetails.setVehicleType(vehicleType);
        captainDetails.setNumberPlate(captainPersonalDetailsDto.getNumberPlate());
        captainDetails.setAdharCard(true);
        captainDetails.setDrivingLicense(true);
        captainDetails.setLicenseExpirationDate(dateTimeConverter.stringToLocalDate(captainPersonalDetailsDto.getLicenseExpiration()));
        captainDetails.setRegistrationCertificate(true);
        captainDetails.setRcExpirationDate(dateTimeConverter.stringToLocalDate(captainPersonalDetailsDto.getLicenseExpiration()));
        captainDetails.setProfilePhoto(true);
        captainDetailsDao.saveCaptainDetails(captainDetails);
        usersDao.updateUser(user);
        session.setAttribute("captainSessionObj", userSessionObj);
        return "Data uploaded successfully";
    }

    public int uploadDocumentCaptain(CaptainPersonalDetailsDto captainPersonalDetailsDto, HttpSession session) {
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("captainSessionObj");

        // -------- File Uploding---------------------------------
        String captainId = String.valueOf(userSessionObj.getUserId()); // assuming this method exists
        String folderName = "captain" + captainId;
        String folderPath = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "captainDocuments" + File.separator + folderName;
        File folder = new File(folderPath);

        if (!folder.exists()) {
            // create the folder if it doesn't exist
            folder.mkdirs();
        }

        int i = 0;
        String fileNameAdhar = "adharcard.pdf";
        String filePathAdhar = folderPath + File.separator + fileNameAdhar;
        System.out.println(filePathAdhar);
        try (FileOutputStream fos = new FileOutputStream(filePathAdhar)) {
            byte[] data = captainPersonalDetailsDto.getAdharCard().getBytes();
            fos.write(data);
            System.out.println("Adharcard uploaded successfully!");
            i++;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Adharcard uploading file: " + e.getMessage());
        }


        String fileNameLicense = "drivingLicense.pdf";
        String filePathLicense = folderPath + File.separator + fileNameLicense;

        try (FileOutputStream fos = new FileOutputStream(filePathLicense)) {
            byte[] data = captainPersonalDetailsDto.getDrivingLicense().getBytes();
            fos.write(data);
            System.out.println("drivingLicense uploaded successfully!");
            i++;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("drivingLicense uploading file: " + e.getMessage());
        }


        String originalFileName = captainPersonalDetailsDto.getProfilePhoto().getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileNameProfile = "profilePhoto" + extension;
        String filePathProfile = folderPath + File.separator + fileNameProfile;

        try (FileOutputStream fos = new FileOutputStream(filePathProfile)) {
            byte[] data = captainPersonalDetailsDto.getProfilePhoto().getBytes();
            fos.write(data);
            System.out.println("profilePhoto uploaded successfully!");
            i++;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("profilePhoto uploading file: " + e.getMessage());
        }

        String fileNameRc = "registrationCertificate.pdf";
        String filePathRc = folderPath + File.separator + fileNameRc;

        try (FileOutputStream fos = new FileOutputStream(filePathRc)) {
            byte[] data = captainPersonalDetailsDto.getRegistrationCertificate().getBytes();
            fos.write(data);
            System.out.println("registrationCertificate uploaded successfully!");
            i++;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("registrationCertificate uploading file: " + e.getMessage());
        }
        return i;
    }





}





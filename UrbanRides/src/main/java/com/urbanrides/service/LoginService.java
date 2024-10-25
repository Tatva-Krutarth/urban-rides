package com.urbanrides.service;


import com.urbanrides.dao.*;
import com.urbanrides.dtos.*;
import com.urbanrides.enums.NotificationTypeEnum;
import com.urbanrides.helper.*;
import com.urbanrides.model.*;


import com.urbanrides.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

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
    DateTimeConverter dateTimeConverter;

    @Autowired
    NotificationLogsDao notificationLogsDao;

    @Autowired
    CaptainDetailsDao captainDetailsDao;

    @Autowired
    HttpSession httpSession;

    @Autowired
    VehicleTypeDao vehicleTypeDao;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public void otpService(UserRegistrationDto userRegistrationDto) {
        String error = validateUserRegistration(userRegistrationDto);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        userRegistrationDto.setEmail(userRegistrationDto.getEmail().toLowerCase());
        OtpLogs existingLog = userRegistrationDao.getOtpLogsByEmail(userRegistrationDto.getEmail());

        if (existingLog == null || !existingLog.getCreatedDate().isEqual(LocalDate.now())) {
            sendAndSaveOtp(userRegistrationDto);
        } else if (ChronoUnit.MINUTES.between(existingLog.getOptReqSendTime(), LocalDateTime.now()) > 2) {
            sendAndSaveOtp(userRegistrationDto);
        } else {
            throw new IllegalArgumentException("Please wait for 2 minutes before requesting a new OTP");
        }
    }

    public void sendAndSaveOtp(UserRegistrationDto userRegistrationDto) {
        OtpLogs otpLogs = setOtpLogData(userRegistrationDto);
        try {
            this.emailSend.userRegistrationOtp(userRegistrationDto, otpLogs.getGeneratedOtp());
        } catch (Exception e) {
            throw new RuntimeException("Email not sent");
        }
        userRegistrationDao.saveUser(otpLogs);
    }


    public OtpLogs setOtpLogData(UserRegistrationDto userRegistrationDto) {
        OtpLogs otpLogs = new OtpLogs();
        otpLogs.setEmail(userRegistrationDto.getEmail());
        otpLogs.setOptReqSendTime(LocalTime.now());
        otpLogs.setGeneratedOtp(otpGenerator.generateOTP());
        return otpLogs;
    }

    public OtpLogs setOtpLogDataForgetData(String email) {
        OtpLogs otpLogs = new OtpLogs();
        otpLogs.setEmail(email);
        otpLogs.setOptReqSendTime(LocalTime.now());
        otpLogs.setGeneratedOtp(otpGenerator.generateOTP());
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
        return null;
    }

    public String emailValidation(String email) {
        if (!commonValidation.isValidEmail(email)) {
            return "Invalid email address";
        }
        return null;
    }

    public void submitRegistration(UserRegistrationDto userRegistrationDto, HttpServletRequest request) {
        userRegistrationDto.setEmail(userRegistrationDto.getEmail().toLowerCase());
        OtpLogs eachLog = userRegistrationDao.getOtpLogsByEmail(userRegistrationDto.getEmail());

        if (eachLog == null) {
            throw new IllegalArgumentException("Get OTP first");
        }

        LocalDate createdDate = eachLog.getCreatedDate();
        if (createdDate.isEqual(LocalDate.now())) {
            if (Math.abs(ChronoUnit.MINUTES.between(eachLog.getOptReqSendTime(), LocalDateTime.now())) <= 2) {
                if (Integer.parseInt(userRegistrationDto.getOtp()) == eachLog.getGeneratedOtp()) {
                    submitData(userRegistrationDto, request);
                } else {
                    throw new IllegalArgumentException("Invalid OTP");
                }
            } else {
                throw new IllegalArgumentException("OTP time is over, get a new OTP");
            }
        } else {
            throw new IllegalArgumentException("OTP time is over, get a new OTP");
        }
    }

    public void submitData(UserRegistrationDto userRegistrationDto, HttpServletRequest request) {
        String error = validateUserRegistration(userRegistrationDto);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        if (!commonValidation.isValidOtp(userRegistrationDto.getOtp())) {
            throw new IllegalArgumentException("Invalid OTP format");
        }

        User user = usersDao.getUserByEmail(userRegistrationDto.getEmail());
        if (user == null) {
            int userId = setUserData(userRegistrationDto);
            User justRegisteredUser = usersDao.getUserByUserId(userId);
            justRegisteredUser.setAccountStatus(1);
            usersDao.updateUser(justRegisteredUser);

            HttpSession session = request.getSession();
            UserSessionObjDto userSessionObj = new UserSessionObjDto();
            userSessionObj.setUserId(justRegisteredUser.getUserId());
            userSessionObj.setAccountStatus(1);
            userSessionObj.setAccountTypeId(justRegisteredUser.getAccountType());

            if (userRegistrationDto.getAcccoutTypeId() == 3) {
                session.setAttribute("riderSessionObj", userSessionObj);
            } else {
                session.setAttribute("captainSessionObj", userSessionObj);
            }
        } else {
            if (user.getAccountType() == 3) {
                throw new IllegalArgumentException("Rider already exists");
            } else {
                throw new IllegalArgumentException("Captain already exists");
            }
        }
    }


    public int setUserData(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        String email = userRegistrationDto.getEmail().toLowerCase();
        user.setEmail(email);
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
        String firstName = capitalizeFirstLetter(riderPersonalDetailsDto.getRiderFirstName());
        String lastName = capitalizeFirstLetter(riderPersonalDetailsDto.getRiderLastName());
        userDetails.setFirstName(firstName);
        userDetails.setLastName(lastName);
        userDetails.setPhone(riderPersonalDetailsDto.getPhone());
        userDetails.setAge(riderPersonalDetailsDto.getAge());
        HttpSession session = request.getSession();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
        if (user == null) {
            return "User not found";
        }
        user.setAccountStatus(5);
        UserDetails userDetailsChecking = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
        if (userDetailsChecking != null) {
            return "User details already filled, please login";
        }
        userDetails.setUser(user);
        userSessionObj.setAccountStatus(5);
        session.setAttribute("riderSessionObj", userSessionObj);
        usersDao.updateUser(user);
        userDetailsDao.saveUserDetails(userDetails);
        return "Login successful";
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public String captainPersonalDetailSubmit(RiderPersonalDetailsDto riderPersonalDetailsDto, HttpServletRequest request) {
        UserDetails userDetails = new UserDetails();
        String firstName = capitalizeFirstLetter(riderPersonalDetailsDto.getRiderFirstName());
        String lastName = capitalizeFirstLetter(riderPersonalDetailsDto.getRiderLastName());
        userDetails.setFirstName(firstName);
        userDetails.setLastName(lastName);
        userDetails.setPhone(riderPersonalDetailsDto.getPhone());
        userDetails.setAge(riderPersonalDetailsDto.getAge());
        HttpSession session = request.getSession();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("captainSessionObj");
        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
        if (user == null) {
            return "User not found";
        }
        UserDetails userDetailsChekcing = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
        if (userDetailsChekcing != null) {
            return "User details already filled, please login";
        }
        user.setAccountStatus(2);
        userSessionObj.setAccountStatus(2);
        session.setAttribute("captainSessionObj", userSessionObj);
        usersDao.updateUser(user);
        userDetails.setUser(user);
        userDetailsDao.saveUserDetails(userDetails);
        return "Login successful";
    }

    public String adminPersonalDetailSubmit(RiderPersonalDetailsDto riderPersonalDetailsDto, HttpServletRequest request) {
        UserDetails userDetails = new UserDetails();
        String firstName = capitalizeFirstLetter(riderPersonalDetailsDto.getRiderFirstName());
        String lastName = capitalizeFirstLetter(riderPersonalDetailsDto.getRiderLastName());
        userDetails.setFirstName(firstName);
        userDetails.setLastName(lastName);
        userDetails.setFirstName(riderPersonalDetailsDto.getRiderFirstName());
        userDetails.setLastName(riderPersonalDetailsDto.getRiderLastName());
        userDetails.setPhone(riderPersonalDetailsDto.getPhone());
        userDetails.setAge(riderPersonalDetailsDto.getAge());
        HttpSession session = request.getSession();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("adminSessionObj");
        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
        if (user == null) {
            return "User not found";
        }
        UserDetails userDetailsChekcing = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
        if (userDetailsChekcing != null) {
            return "User details already filled, please login";
        }
        user.setAccountStatus(2);
        userSessionObj.setAccountStatus(2);
        session.setAttribute("adminSessionObj", userSessionObj);
        usersDao.updateUser(user);
        userDetails.setUser(user);
        userDetailsDao.saveUserDetails(userDetails);
        return "Login successful";
    }

    public String getCapatainName(HttpServletRequest req) {
        HttpSession session = req.getSession();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("captainSessionObj");
        UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
        String capatainName = userDetails.getFirstName() + " , " + userDetails.getLastName();
        return capatainName;
    }

    public String riderLoginService(UserLoginDto userLoginDto, HttpServletRequest request) {
        User user = usersDao.getUserByEmail(userLoginDto.getEmail());
        if (user == null) {
            return "User not found";
        } else {
            Boolean verified = passwordToHash.checkPassword(userLoginDto.getPassword(), user.getSalt(), user.getPasswordHash());
            if (verified) {
                UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(user.getUserId());
                UserSessionObjDto userSessionObj = new UserSessionObjDto();
                if (user.getAccountType() == 3) {
                    HttpSession session = request.getSession();
                    userSessionObj.setUserId(user.getUserId());
                    userSessionObj.setAccountStatus(user.getAccountStatus());
                    userSessionObj.setAccountTypeId(user.getAccountType());
                    if (userDetails != null && userDetails.isProfilePhoto()) {
                        userSessionObj.setProfilePhoto(1);
                        userSessionObj.setProfileLoc("/resources/uploads/riderDocuments/riderProfilePics" + user.getUserId() + "/riderProfile" + user.getUserId() + userDetails.getProfilePhotoExtention());
                    } else {
                        userSessionObj.setProfilePhoto(0);
                    }
                    session.setAttribute("riderSessionObj", userSessionObj);
                    return "Login successful + " + user.getAccountType();
                } else if (user.getAccountType() == 2) {
                    HttpSession session = request.getSession();
                    userSessionObj.setUserId(user.getUserId());
                    userSessionObj.setAccountStatus(user.getAccountStatus());
                    userSessionObj.setAccountTypeId(user.getAccountType());
                    if (userDetails != null && userDetails.isProfilePhoto()) {
                        userSessionObj.setProfilePhoto(1);
                        userSessionObj.setProfileLoc("/resources/uploads/captainDocuments/captain" + user.getUserId() + "/profilePhoto" + userDetails.getProfilePhotoExtention());
                    } else {
                        userSessionObj.setProfilePhoto(0);
                    }
                    session.setAttribute("captainSessionObj", userSessionObj);
                    return "Login successful + " + user.getAccountType();
                } else {
                    HttpSession session = request.getSession();
                    userSessionObj.setUserId(user.getUserId());
                    userSessionObj.setAccountStatus(user.getAccountStatus());
                    userSessionObj.setAccountTypeId(user.getAccountType());
                    if (userDetails != null && userDetails.isProfilePhoto()) {
                        userSessionObj.setProfilePhoto(1);
                        userSessionObj.setProfileLoc("/resources/uploads/adminDocuments/admin" + user.getUserId() + "/profilePhoto" + userDetails.getProfilePhotoExtention());
                    } else {
                        userSessionObj.setProfilePhoto(0);
                    }
                    session.setAttribute("adminSessionObj", userSessionObj);
                    return "Login successful + " + user.getAccountType();
                }
            } else {
                return "Invalid password ";
            }
        }
    }

    public void forgetOtpService(String emaile) {
        String email = emaile.toLowerCase();
        String error = emailValidation(email);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        User user = usersDao.getUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User doesn't exist");
        }

        OtpLogs eachLog = userRegistrationDao.getOtpLogsByEmail(email);
        if (eachLog == null) {
            OtpLogs otpLogs = setOtpLogDataForgetData(email);
            try {
                this.emailSend.userForgetOtp(email, otpLogs.getGeneratedOtp());
            } catch (Exception e) {
                throw new RuntimeException("Email not sent");
            }
            userRegistrationDao.saveUser(otpLogs);
        } else {
            LocalDate createdDate = eachLog.getCreatedDate();
            if (createdDate.isEqual(LocalDate.now())) {
                if (Math.abs(ChronoUnit.MINUTES.between(eachLog.getOptReqSendTime(), LocalDateTime.now())) <= 2) {
                    throw new IllegalArgumentException("Please wait for 2 minutes before getting a new OTP");
                } else {
                    userRegistrationDao.deleteOtpLogsByEmail(email);
                    OtpLogs otpLogs = setOtpLogDataForgetData(email);
                    try {
                        this.emailSend.userForgetOtp(email, otpLogs.getGeneratedOtp());
                    } catch (Exception e) {
                        throw new RuntimeException("Email not sent");
                    }
                    userRegistrationDao.saveUser(otpLogs);
                }
            } else {
                OtpLogs otpLogs = setOtpLogDataForgetData(email);
                userRegistrationDao.deleteOtpLogsByEmail(email);
                try {
                    this.emailSend.userForgetOtp(email, otpLogs.getGeneratedOtp());
                } catch (Exception e) {
                    throw new RuntimeException("Email not sent");
                }
                userRegistrationDao.saveUser(otpLogs);
            }
        }
    }

    public String forgetPassSubmit(ForgetPassDto forgetPassDto) {
        String error = forgetPassValidation(forgetPassDto);
        if (error != null) {
            return error;
        }
        forgetPassDto.setEmail(forgetPassDto.getEmail().toLowerCase());
        OtpLogs eachLog = userRegistrationDao.getOtpLogsByEmail(forgetPassDto.getEmail());
        if (eachLog == null) {
            return "Get Otp first";
        } else {
            LocalDate createdDate = eachLog.getCreatedDate();
            if (createdDate.isEqual(LocalDate.now())) {
                if (Math.abs(ChronoUnit.MINUTES.between(eachLog.getOptReqSendTime(), LocalDateTime.now())) <= 2) {
                    if (Integer.parseInt(forgetPassDto.getOtp()) == eachLog.getGeneratedOtp()) {
                        String msgFromSumbitting = updateData(forgetPassDto);
                        return msgFromSumbitting;
                    } else {
                        return "Invalid OTP";
                    }
                } else {
                    return "OTP Time is over get a new Otp";
                }
            } else {
                return "OTP Time is over get a new Otp";
            }
        }
    }

    public String updateData(ForgetPassDto forgetPassDto) {
        User user = usersDao.getUserByEmail(forgetPassDto.getEmail());
        if (user == null) {
            return "User Not Found";
        } else {
            NotificationLogs notificationLogs = new NotificationLogs();
            notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID4"));
            notificationLogs.setNotificationMsg("The password has been changed using the forgot password option.");
            notificationLogs.setUser(user);
            notificationLogsDao.saveNotificationLog(notificationLogs);
            updatePassword(user, forgetPassDto);
            sendNotiToRider(notificationLogs.getNotificationMsg());
            return "Password updated successfully";
        }
    }

    public void sendNotiToRider(String notiMsg) {
        Map<String, String> notification = new HashMap<>();
        notification.put("message", notiMsg);
        simpMessagingTemplate.convertAndSend("/topic/rider-incoming-notifications", notification);
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
        return null;
    }

    public void saveCaptainPersonalDetails(CaptainPersonalDetailsDto captainPersonalDetailsDto, HttpSession session, HttpServletRequest request) throws Exception {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("captainSessionObj");
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
        if (captainDetails != null) {
            throw new Exception("User documents are already filled.");
        }
        int i = uploadDocumentCaptain(captainPersonalDetailsDto, session);
        if (i != 4) {
            throw new Exception("Error while uploading the data");
        }
        saveCaptainDetails(captainPersonalDetailsDto, request);
    }

    public void saveCaptainDetails(CaptainPersonalDetailsDto captainPersonalDetailsDto, HttpServletRequest request)  {
        CaptainDetails captainDetails = new CaptainDetails();
        String originalFileName = captainPersonalDetailsDto.getProfilePhoto().getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        HttpSession session = request.getSession();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("captainSessionObj");
        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
        UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
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
        captainDetails.setRcExpirationDate(dateTimeConverter.stringToLocalDate(captainPersonalDetailsDto.getRcExpiration()));
        captainDetails.setProfilePhoto(true);
        userDetails.setProfilePhoto(true);
        userDetails.setProfilePhotoExtention(extension);
        captainDetailsDao.saveCaptainDetails(captainDetails);
        userDetailsDao.updateUserDetails(userDetails);
        usersDao.updateUser(user);
        session.setAttribute("captainSessionObj", userSessionObj);
    }


    public int uploadDocumentCaptain(CaptainPersonalDetailsDto captainPersonalDetailsDto, HttpSession session) {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("captainSessionObj");
        String captainId = String.valueOf(userSessionObj.getUserId()); // assuming this method exists
        String folderName = "captain" + captainId;
        String folderPath = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "captainDocuments" + File.separator + folderName;
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        int i = 0;
        String fileNameAdhar = "adharcard.pdf";
        String filePathAdhar = folderPath + File.separator + fileNameAdhar;
        try (FileOutputStream fos = new FileOutputStream(filePathAdhar)) {
            byte[] data = captainPersonalDetailsDto.getAdharCard().getBytes();
            fos.write(data);
            i++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileNameLicense = "drivingLicense.pdf";
        String filePathLicense = folderPath + File.separator + fileNameLicense;
        try (FileOutputStream fos = new FileOutputStream(filePathLicense)) {
            byte[] data = captainPersonalDetailsDto.getDrivingLicense().getBytes();
            fos.write(data);
            i++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String originalFileName = captainPersonalDetailsDto.getProfilePhoto().getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileNameProfile = "profilePhoto" + extension;
        String filePathProfile = folderPath + File.separator + fileNameProfile;
        try (FileOutputStream fos = new FileOutputStream(filePathProfile)) {
            byte[] data = captainPersonalDetailsDto.getProfilePhoto().getBytes();
            fos.write(data);
            i++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileNameRc = "registrationCertificate.pdf";
        String filePathRc = folderPath + File.separator + fileNameRc;
        try (FileOutputStream fos = new FileOutputStream(filePathRc)) {
            byte[] data = captainPersonalDetailsDto.getRegistrationCertificate().getBytes();
            fos.write(data);
            i++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public CaptainReuploadDataRenderingDto getCaptainReuploadData() {
        CaptainReuploadDataRenderingDto captainReuploadDataRendering = new CaptainReuploadDataRenderingDto();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) httpSession.getAttribute("captainSessionObj");
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
        UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(user.getUserId());
        captainReuploadDataRendering.setCaptainId(user.getUserId());
        captainReuploadDataRendering.setCaptainName(userDetails.getFirstName() + " " + userDetails.getLastName());
        String fileLocation = "/UrbanRides/resources/uploads/captainDocuments/captain" + user.getUserId();
        captainReuploadDataRendering.setAdharApprovedApprove(captainDetails.isAdharApproved());
        captainReuploadDataRendering.setDrivingLicenceApprove(captainDetails.isLicenseApproved());
        captainReuploadDataRendering.setRCCertificateApprove(captainDetails.isRcApproved());
        captainReuploadDataRendering.setRCExpirationDateApprove(captainDetails.isRcExpirationDateApproved());
        captainReuploadDataRendering.setDrivingLicenceExpiryDateApprove(captainDetails.isLicenseExpirationDateApproved());
        captainReuploadDataRendering.setNumberPlateApprove(captainDetails.isNumberplateApproved());
        captainReuploadDataRendering.setAdharCard(fileLocation + "/adharcard.pdf");
        captainReuploadDataRendering.setDrivingLicence(fileLocation + "/drivingLicense.pdf");
        captainReuploadDataRendering.setDrivingLicenceExpiryDate(formatDateToString(captainDetails.getLicenseExpirationDate()));
        captainReuploadDataRendering.setRCCertificate(fileLocation + "/registrationCertificate.pdf");
        captainReuploadDataRendering.setRCExpirationDate(formatDateToString(captainDetails.getRcExpirationDate()));
        captainReuploadDataRendering.setNumberPlate(captainDetails.getNumberPlate());
        return captainReuploadDataRendering;
    }

    public static String formatDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public ResponseEntity<Map<String, String>> saveCaptainDocumentReupload(CaptainReuploadDataDto captainReuploadDataDto, HttpSession session, HttpServletRequest request) {
        int expectedUploadCount = 0;
        if (captainReuploadDataDto.getAdharCarde() != null && !captainReuploadDataDto.getAdharCarde().isEmpty()) {
            expectedUploadCount++;
        }
        if (captainReuploadDataDto.getDrivingLicensee() != null && !captainReuploadDataDto.getDrivingLicensee().isEmpty()) {
            expectedUploadCount++;
        }
        if (captainReuploadDataDto.getRegistrationCertificatee() != null && !captainReuploadDataDto.getRegistrationCertificatee().isEmpty()) {
            expectedUploadCount++;
        }
        int actualUploadCount;
        try {
            actualUploadCount = uploadDocumentCaptainReupload(captainReuploadDataDto, session);
        } catch (Exception e) {
            throw new RuntimeException("Error while uploading documents: " + e.getMessage(), e);
        }
        if (actualUploadCount < expectedUploadCount) {
            throw new RuntimeException("Error while uploading some of the documents.");
        }
        return saveCaptainDetailsReupload(captainReuploadDataDto, request);
    }

    public ResponseEntity<Map<String, String>> saveCaptainDetailsReupload(CaptainReuploadDataDto captainReuploadDataDto, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("captainSessionObj");
        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
        user.setAccountStatus(3);
        userSessionObj.setAccountStatus(3);
        if (captainReuploadDataDto.getNumberPlatee() != null && !captainReuploadDataDto.getNumberPlatee().isEmpty()) {
            captainDetails.setNumberPlate(captainReuploadDataDto.getNumberPlatee());
        }
        if (captainReuploadDataDto.getRcExpiratione() != null && !captainReuploadDataDto.getRcExpiratione().isEmpty()) {
            captainDetails.setRcExpirationDate(dateTimeConverter.stringToLocalDate(captainReuploadDataDto.getRcExpiratione()));
        }
        if (captainReuploadDataDto.getLicenseExpiratione() != null && !captainReuploadDataDto.getLicenseExpiratione().isEmpty()) {
            captainDetails.setLicenseExpirationDate(dateTimeConverter.stringToLocalDate(captainReuploadDataDto.getLicenseExpiratione()));
        }
        captainDetailsDao.updateCaptainDetail(captainDetails);
        usersDao.updateUser(user);
        session.setAttribute("captainSessionObj", userSessionObj);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Data uploaded successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public int uploadDocumentCaptainReupload(CaptainReuploadDataDto captainReuploadDataDto, HttpSession session) {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("captainSessionObj");
        String captainId = String.valueOf(userSessionObj.getUserId());
        String folderName = "captain" + captainId;
        String folderPath = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "captainDocuments" + File.separator + folderName;
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        int uploadCount = 0;
        try {
            if (captainReuploadDataDto.getAdharCarde() != null && !captainReuploadDataDto.getAdharCarde().isEmpty()) {
                String fileNameAdhar = "adharcard.pdf";
                String filePathAdhar = folderPath + File.separator + fileNameAdhar;
                try (FileOutputStream fos = new FileOutputStream(filePathAdhar)) {
                    byte[] data = captainReuploadDataDto.getAdharCarde().getBytes();
                    fos.write(data);
                    uploadCount++;
                }
            }
            if (captainReuploadDataDto.getDrivingLicensee() != null && !captainReuploadDataDto.getDrivingLicensee().isEmpty()) {
                String fileNameLicense = "drivingLicense.pdf";
                String filePathLicense = folderPath + File.separator + fileNameLicense;
                try (FileOutputStream fos = new FileOutputStream(filePathLicense)) {
                    byte[] data = captainReuploadDataDto.getDrivingLicensee().getBytes();
                    fos.write(data);
                    uploadCount++;
                }
            }
            if (captainReuploadDataDto.getRegistrationCertificatee() != null && !captainReuploadDataDto.getRegistrationCertificatee().isEmpty()) {
                String fileNameRc = "registrationCertificate.pdf";
                String filePathRc = folderPath + File.separator + fileNameRc;
                try (FileOutputStream fos = new FileOutputStream(filePathRc)) {
                    byte[] data = captainReuploadDataDto.getRegistrationCertificatee().getBytes();
                    fos.write(data);
                    uploadCount++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while uploading documents: " + e.getMessage(), e);
        }

        return uploadCount;
    }

}





package com.urbanrides.service;


import com.urbanrides.dao.*;
import com.urbanrides.dtos.*;
import com.urbanrides.helper.*;
import com.urbanrides.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    CommonValidation commonValidation;

    @Autowired
    EmailSend emailSend;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private TripDao tripDao;

    @Autowired
    private SupportTypeLogsDao supportTypeLogsDao;

    @Autowired
    private UserDetailsDao userdetailsDao;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    PasswordToHash passwordToHash;

    @Autowired
    NotificationLogsDao notificationLogsDao;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserDetailsDao userDetailsDao;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private GeneralTripDetailsDao generalTripDetailsDao;

    @Autowired
    private CaptainDetailsDao captainDetailsDao;

    @Autowired
    private PackageTripDao packageTripDao;

    @Autowired
    DateTimeConverter dateTimeConverter;

    public AdminCountData getDashCount() {
        AdminCountData adminCountData = new AdminCountData();
        adminCountData.setTotalUserCount(usersDao.totalCount());
        adminCountData.setGeneralBooking(tripDao.getCountOfGeneralBooking());
        adminCountData.setServiceBooking(tripDao.getCountOfServiceBooking());
        adminCountData.setTotalSuccessBooking(tripDao.getSuccessTripCount());
        return adminCountData;
    }

    public Page<AdminQuerries> getSupportData(Pageable pageable) {
        Page<SupportTypeLogs> supportTypeLogsPage = supportTypeLogsDao.getAllLogsData(pageable);
        List<AdminQuerries> adminQuerriesList = supportTypeLogsPage.getContent().stream().map(supportTypeLogs -> {
            AdminQuerries adminQuerries = new AdminQuerries();
            adminQuerries.setUserID(supportTypeLogs.getUserObj().getUserId());
            adminQuerries.setSupportId(supportTypeLogs.getSupportLogsId());
            UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(supportTypeLogs.getUserObj().getUserId());
            adminQuerries.setContactDetails(userDetails.getPhone());
            adminQuerries.setSypportType(supportTypeLogs.getSupportType());
            adminQuerries.setAccountType(UserTypeEnum.getValueById(supportTypeLogs.getUserObj().getAccountType()));
            adminQuerries.setMessage(supportTypeLogs.getDescription());
            adminQuerries.setUserName(userDetails.getFirstName() + " , " + userDetails.getLastName());
            LocalDate createdDate = supportTypeLogs.getCreatedDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = createdDate.format(formatter);
            adminQuerries.setCreatedDate(formattedDate);
            return adminQuerries;
        }).collect(Collectors.toList());
        return new PageImpl<>(adminQuerriesList, pageable, supportTypeLogsPage.getTotalElements());
    }

    public Page<AdminQuerries> getRunningData(Pageable pageable) {
        Page<SupportTypeLogs> supportTypeLogsPage = supportTypeLogsDao.getRunningData(pageable);
        List<AdminQuerries> adminQuerriesList = supportTypeLogsPage.getContent().stream().map(supportTypeLogs -> {
            AdminQuerries adminQuerries = new AdminQuerries();
            adminQuerries.setUserID(supportTypeLogs.getUserObj().getUserId());
            adminQuerries.setSupportId(supportTypeLogs.getSupportLogsId());
            UserDetails userDetails = userdetailsDao.getUserDetailsByUserId(supportTypeLogs.getUserObj().getUserId());
            adminQuerries.setContactDetails(userDetails.getPhone());
            adminQuerries.setSypportType(supportTypeLogs.getSupportType());
            adminQuerries.setAccountType(UserTypeEnum.getValueById(supportTypeLogs.getUserObj().getAccountType()));
            adminQuerries.setMessage(supportTypeLogs.getDescription());
            adminQuerries.setUserName(userDetails.getFirstName() + " , " + userDetails.getLastName());
            if (supportTypeLogs.isFile()) {
                adminQuerries.setFileAvailable(1);
            } else {
                adminQuerries.setFileAvailable(2);
            }
            String fileLocation;
            if (supportTypeLogs.getUserObj().getAccountType() == 3) {
                fileLocation = "/UrbanRides/resources/uploads/riderDocuments/riderComplain" + userDetails.getUser().getUserId() + "/complain" + userDetails.getUser().getUserId() + supportTypeLogs.getFileExtention();
            } else {
                fileLocation = "/UrbanRides/resources/uploads/captainDocuments/captainComplain" + userDetails.getUser().getUserId() + "/complain" + userDetails.getUser().getUserId() + supportTypeLogs.getFileExtention();
            }
            adminQuerries.setFileLocaton(fileLocation);
            LocalDate createdDate = supportTypeLogs.getCreatedDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = createdDate.format(formatter);
            adminQuerries.setCreatedDate(formattedDate);
            return adminQuerries;
        }).collect(Collectors.toList());
        return new PageImpl<>(adminQuerriesList, pageable, supportTypeLogsPage.getTotalElements());
    }

    public Page<AdminQuerries> getCompletedData(Pageable pageable) {
        Page<SupportTypeLogs> supportTypeLogsPage = supportTypeLogsDao.getCompletedData(pageable);
        List<AdminQuerries> adminQuerriesList = supportTypeLogsPage.getContent().stream().map(supportTypeLogs -> {
            AdminQuerries adminQuerries = new AdminQuerries();
            adminQuerries.setUserID(supportTypeLogs.getUserObj().getUserId());
            adminQuerries.setSupportId(supportTypeLogs.getSupportLogsId());
            UserDetails userDetails = userdetailsDao.getUserDetailsByUserId(supportTypeLogs.getUserObj().getUserId());
            adminQuerries.setContactDetails(userDetails.getPhone());
            adminQuerries.setSypportType(supportTypeLogs.getSupportType());
            adminQuerries.setAccountType(UserTypeEnum.getValueById(supportTypeLogs.getUserObj().getAccountType()));
            adminQuerries.setMessage(supportTypeLogs.getDescription());
            adminQuerries.setUserName(userDetails.getFirstName() + " , " + userDetails.getLastName());
            UserDetails adminDetails = userdetailsDao.getUserDetailsByUserId(supportTypeLogs.getAdminObj().getUserId());
            adminQuerries.setAdminName(adminDetails.getFirstName() + " , " + adminDetails.getLastName());
            LocalDate createdDate = supportTypeLogs.getCreatedDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = createdDate.format(formatter);
            adminQuerries.setCreatedDate(formattedDate);
            return adminQuerries;
        }).collect(Collectors.toList());
        return new PageImpl<>(adminQuerriesList, pageable, supportTypeLogsPage.getTotalElements());
    }

    public UserManagementDataDto getUserManagementDetails() {
        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("adminSessionObj");
        UserDetails userDetails = userdetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
        UserManagementDataDto userManagementDataDto = new UserManagementDataDto();
        userManagementDataDto.setFirstName(userDetails.getFirstName());
        userManagementDataDto.setLastName(userDetails.getLastName());
        userManagementDataDto.setEmail(userDetails.getUser().getEmail());
        userManagementDataDto.setPhone(userDetails.getPhone());
        if (userDetails.isProfilePhoto()) {
            userManagementDataDto.setProfilePhotoPath("/UrbanRides/resources/uploads/adminDocuments/adminProfilePics" + userSessionObj.getUserId() + "/adminProfile" + userSessionObj.getUserId() + userDetails.getProfilePhotoExtention());
        }
        return userManagementDataDto;
    }


    public ResponseEntity<Map<String, String>> adminPersonalDetailSubmit(RiderUMPersonalDetailDto riderUMPersonalDetailDto) {
        Map<String, String> response = new HashMap<>();
        try {
            UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("adminSessionObj");
            UserDetails userDetails = userdetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
            if (userDetails == null) {
                response.put("message", "User not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            String firstName = capitalizeFirstLetter(riderUMPersonalDetailDto.getFirstName());
            String lastName = capitalizeFirstLetter(riderUMPersonalDetailDto.getLastName());
            userDetails.setFirstName(firstName);
            userDetails.setLastName(lastName);
            userDetails.setPhone(riderUMPersonalDetailDto.getPhone());
            userdetailsDao.updateUserDetails(userDetails);
            response.put("message", "Personal details updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Failed to update personal details: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public String sendPassToService(RiderUMLoginDetails riderUMLoginDetails) throws Exception {
        String error = passwordValidation(riderUMLoginDetails);
        if (error != null) {
            return error;
        }
        updatePassword(riderUMLoginDetails);
        return null;
    }

    public String passwordValidation(RiderUMLoginDetails riderUMLoginDetails) {
        if (!commonValidation.isValidPassword(riderUMLoginDetails.getNewPassword())) {
            return "Password must be between 8 and 13 characters.";
        }
        if (!commonValidation.confirmPassword(riderUMLoginDetails.getNewPassword(), riderUMLoginDetails.getConfirmPassword())) {
            return "Passwords do not match.";
        }
        return null;
    }

    public void updatePassword(RiderUMLoginDetails riderUMLoginDetails) throws Exception {
        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("adminSessionObj");
        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
        if (user == null) {
            throw new Exception("User not found");
        }
        Boolean verified = passwordToHash.checkPassword(riderUMLoginDetails.getCurrentPassword(), user.getSalt(), user.getPasswordHash());
        if (verified) {
            updateToDB(user, riderUMLoginDetails);
            emailSend.loginCredentialUpdated(user.getEmail());
        } else {
            throw new Exception("Incorrect current password");
        }
    }

    public User updateToDB(User user, RiderUMLoginDetails riderUMLoginDetails) throws Exception {
        try {
            String saltString = passwordToHash.generateSalt();
            String hashedPasswordString = passwordToHash.hashPassword(riderUMLoginDetails.getNewPassword(), saltString);
            user.setSalt(saltString);
            user.setPasswordHash(hashedPasswordString);
            usersDao.updateUser(user);

            NotificationLogs notificationLogs = new NotificationLogs();
            notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID4"));
            notificationLogs.setNotificationMsg("The password has been changed from the user's My Profile section.");
            notificationLogs.setUser(user);
            notificationLogsDao.saveNotificationLog(notificationLogs);
            sendNotiToRider(notificationLogs.getNotificationMsg());
            return user;
        } catch (Exception e) {
            throw new Exception("Error updating password: " + e.getMessage());
        }
    }

    public void sendNotiToRider(String notiMsg) {
        Map<String, String> notification = new HashMap<>();
        notification.put("message", notiMsg);
        simpMessagingTemplate.convertAndSend("/topic/rider-incoming-notifications", notification);
    }

    public String updateProfilePic(RiderUMUpdateProfileLogo riderUMUpdateProfileLogo, HttpSession session) throws IOException {
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("adminSessionObj");
        if (userSessionObj == null) {
            throw new IOException("User session not found.");
        }
        UserDetails userDetails = userdetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
        if (userDetails == null) {
            throw new IOException("User details not found.");
        }
        String adminId = String.valueOf(userSessionObj.getUserId());
        String folderName = "adminProfilePics" + adminId;
        String folderPath = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "adminDocuments" + File.separator + folderName;
        File folder = new File(folderPath);
        try {
            deleteFilesInFolder(folder);
        } catch (Exception e) {
            throw new IOException("Failed to delete existing files: " + e.getMessage(), e);
        }
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new IOException("Failed to create directory: " + folderPath);
            }
        }
        String fileExtension = getFileExtension(riderUMUpdateProfileLogo.getProfilePhoto().getOriginalFilename());
        if (!isValidFileExtension(fileExtension)) {
            throw new IOException("Invalid file extension: " + fileExtension);
        }
        String fileNameBase = "adminProfile";
        String fileName = fileNameBase + adminId + fileExtension;
        String fileProfile = folderPath + File.separator + fileName;
        try (FileOutputStream fos = new FileOutputStream(fileProfile)) {
            byte[] data = riderUMUpdateProfileLogo.getProfilePhoto().getBytes();
            fos.write(data);
        } catch (IOException e) {
            throw new IOException("Error uploading profile photo: " + e.getMessage(), e);
        }
        userDetails.setProfilePhoto(true);
        userDetails.setProfilePhotoExtention(fileExtension);
        try {
            userdetailsDao.updateUserDetails(userDetails);
        } catch (Exception e) {
            throw new IOException("Failed to update user details in the database: " + e.getMessage(), e);
        }
        userSessionObj.setProfileLoc("/resources/uploads/adminDocuments/adminProfilePics" + userSessionObj.getUserId() + "/adminProfile" + userSessionObj.getUserId() + fileExtension);
        session.setAttribute("adminSessionObj", userSessionObj);
        String relativePath = getRelativePath(fileProfile, session);
        return relativePath;
    }

    private void deleteFilesInFolder(File folder) {
        if (folder == null || !folder.exists() || !folder.isDirectory()) {
            return;
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                return fileName.substring(dotIndex).toLowerCase();
            }
        }
        return "";
    }

    private boolean isValidFileExtension(String extension) {
        return extension.equals(".png") || extension.equals(".jpg");
    }

    private String getRelativePath(String fullPath, HttpSession session) {
        String rootDir = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "adminDocuments" + File.separator;
        int index = fullPath.indexOf(rootDir);
        if (index != -1) {
            String relativePath = fullPath.substring(index + rootDir.length());
            relativePath = relativePath.replace(File.separator, "/");
            if (!relativePath.startsWith("/")) {
                relativePath = "/" + relativePath;
            }
            return relativePath;
        }
        return fullPath;
    }

    public boolean acceptRequest(int id) {
        SupportTypeLogs supportTypeLogs = supportTypeLogsDao.getSupportBySupportId(id);
        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("adminSessionObj");
        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
        supportTypeLogs.setAdminObj(user);
        supportTypeLogsDao.updateSupportTypeLogs(supportTypeLogs);
        emailSend.acceptSupportRequest(supportTypeLogs);
        return true;
    }

    public boolean completeRequest(int id) {
        SupportTypeLogs supportTypeLogs = supportTypeLogsDao.getSupportBySupportId(id);
        supportTypeLogs.setSolved(true);
        supportTypeLogsDao.updateSupportTypeLogs(supportTypeLogs);
        emailSend.concludeSupportRequest(supportTypeLogs);
        return true;
    }

    public List<AdminUserManagementAllDto> getAllUsersData() {
        List<AdminUserManagementAllDto> userlist = new ArrayList<>();
        List<User> userList = usersDao.getAllUsersUnblockedUser();
        for (User user : userList) {
            AdminUserManagementAllDto allDataObj = new AdminUserManagementAllDto();
            allDataObj.setAccountType(user.getAccountType());
            allDataObj.setEmail(user.getEmail());
            allDataObj.setRiderUserId(user.getUserId());
            allDataObj.setStatus(user.getAccountStatus());
            UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(user.getUserId());
            allDataObj.setUserName(userDetails.getFirstName() + " , " + userDetails.getLastName());
            allDataObj.setPhone(userDetails.getPhone());
            int sucessTripCount = tripDao.getSuccessTripCountPerUser(user.getUserId());
            int failedTripCount = tripDao.getFailedTripCount(user.getUserId());
            int allTripCount = tripDao.getTotalTripCount(user.getUserId());
            allDataObj.setTotalSuccestrip(sucessTripCount);
            allDataObj.setTotalFailedTrip(failedTripCount);
            allDataObj.setTotalNumberofRides(allTripCount);
            userlist.add(allDataObj);
        }
        return userlist;
    }

    public List<AdminUserManagementAllDto> getRiderUsersData() {
        List<AdminUserManagementAllDto> userlist = new ArrayList<>();
        List<User> userList = usersDao.getRiderUsersUnblockedUser();
        for (User user : userList) {
            AdminUserManagementAllDto allDataObj = new AdminUserManagementAllDto();
            allDataObj.setAccountType(user.getAccountType());
            allDataObj.setEmail(user.getEmail());
            allDataObj.setRiderUserId(user.getUserId());
            allDataObj.setStatus(user.getAccountStatus());
            UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(user.getUserId());
            allDataObj.setUserName(userDetails.getFirstName() + " , " + userDetails.getLastName());
            allDataObj.setPhone(userDetails.getPhone());
            int sucessTripCount = tripDao.getSuccessTripCountPerUser(user.getUserId());
            int failedTripCount = tripDao.getFailedTripCount(user.getUserId());
            int allTripCount = tripDao.getTotalTripCount(user.getUserId());
            allDataObj.setTotalSuccestrip(sucessTripCount);
            allDataObj.setTotalFailedTrip(failedTripCount);
            allDataObj.setTotalNumberofRides(allTripCount);
            userlist.add(allDataObj);
        }
        return userlist;
    }

    public List<AdminUserManagementAllDto> getCaptainUsersData() {
        List<AdminUserManagementAllDto> userlist = new ArrayList<>();
        List<User> userList = usersDao.getCaptainUsersUnblockedUser();
        for (User user : userList) {
            AdminUserManagementAllDto allDataObj = new AdminUserManagementAllDto();
            allDataObj.setAccountType(user.getAccountType());
            allDataObj.setEmail(user.getEmail());
            allDataObj.setRiderUserId(user.getUserId());
            allDataObj.setStatus(user.getAccountStatus());
            UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(user.getUserId());
            allDataObj.setUserName(userDetails.getFirstName() + " , " + userDetails.getLastName());
            allDataObj.setPhone(userDetails.getPhone());
            int sucessTripCount = tripDao.getSuccessTripCountPerUser(user.getUserId());
            int failedTripCount = tripDao.getFailedTripCount(user.getUserId());
            int allTripCount = tripDao.getTotalTripCount(user.getUserId());
            allDataObj.setTotalSuccestrip(sucessTripCount);
            allDataObj.setTotalFailedTrip(failedTripCount);
            allDataObj.setTotalNumberofRides(allTripCount);
            userlist.add(allDataObj);
        }
        return userlist;
    }

    public List<AdminUserManagementAllDto> getAdminUsersData() {
        List<AdminUserManagementAllDto> userlist = new ArrayList<>();
        List<User> userList = usersDao.getAdminUsersUnblockedUser();
        for (User user : userList) {
            AdminUserManagementAllDto allDataObj = new AdminUserManagementAllDto();
            allDataObj.setAccountType(user.getAccountType());
            allDataObj.setEmail(user.getEmail());
            allDataObj.setRiderUserId(user.getUserId());
            allDataObj.setStatus(user.getAccountStatus());
            UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(user.getUserId());
            allDataObj.setUserName(userDetails.getFirstName() + " , " + userDetails.getLastName());
            allDataObj.setPhone(userDetails.getPhone());
            int sucessTripCount = tripDao.getSuccessTripCountPerUser(user.getUserId());
            int failedTripCount = tripDao.getFailedTripCount(user.getUserId());
            int allTripCount = tripDao.getTotalTripCount(user.getUserId());
            allDataObj.setTotalSuccestrip(sucessTripCount);
            allDataObj.setTotalFailedTrip(failedTripCount);
            allDataObj.setTotalNumberofRides(allTripCount);
            userlist.add(allDataObj);
        }
        return userlist;
    }

    public List<AdminUserManagementAllDto> getBlockUsersData() {
        List<AdminUserManagementAllDto> userlist = new ArrayList<>();
        List<User> userList = usersDao.getUsersBlockedUser();
        for (User user : userList) {
            AdminUserManagementAllDto allDataObj = new AdminUserManagementAllDto();
            allDataObj.setAccountType(user.getAccountType());
            allDataObj.setEmail(user.getEmail());
            allDataObj.setRiderUserId(user.getUserId());
            allDataObj.setStatus(user.getAccountStatus());
            UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(user.getUserId());
            allDataObj.setUserName(userDetails.getFirstName() + " , " + userDetails.getLastName());
            allDataObj.setPhone(userDetails.getPhone());
            int sucessTripCount = tripDao.getSuccessTripCountPerUser(user.getUserId());
            int failedTripCount = tripDao.getFailedTripCount(user.getUserId());
            int allTripCount = tripDao.getTotalTripCount(user.getUserId());
            allDataObj.setTotalSuccestrip(sucessTripCount);
            allDataObj.setTotalFailedTrip(failedTripCount);
            allDataObj.setTotalNumberofRides(allTripCount);
            userlist.add(allDataObj);
        }
        return userlist;
    }

    public boolean unblockUser(int userId) {
        User user = usersDao.getUserByUserId(userId);
        if (user != null) {
            user.setAccountStatus(5);
            usersDao.updateUser(user);
            try {
                emailSend.unblockUserMail(user.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public boolean blockUser(int userId) {
        User user = usersDao.getUserByUserId(userId);
        if (user == null) {
            return false;
        }
        NotificationLogs notificationLogs = new NotificationLogs();
        notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID15"));
        notificationLogs.setNotificationMsg("Your account has been blocked by admin due to violating the terms and conditions.");
        notificationLogs.setUser(user);
        notificationLogsDao.saveNotificationLog(notificationLogs);
        user.setAccountStatus(6);
        if (user.getAccountType() == 2) {
            sendNotiToCaptain(notificationLogs.getNotificationMsg());
            UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");
            if (userSessionObj != null && userSessionObj.getUserId() == userId) {
                HttpSession session = httpServletRequest.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
            }
        } else {
            sendNotiToRider(notificationLogs.getNotificationMsg());
            UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("riderSessionObj");
            if (userSessionObj != null && userSessionObj.getUserId() == userId) {
                HttpSession session = httpServletRequest.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
            }
        }
        try {
            emailSend.blockUserMail(user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        usersDao.updateUser(user);
        return true;
    }

    private String formatLocalDateTime(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.format(formatter);
    }

    public List<RiderMyTripDataDto> ridesFilterData(AdminRidesFilterData filterData) {
        List<RiderMyTripDataDto> riderMyTripList = new ArrayList<>();

        List<Trip> tripList = tripDao.getAllTripByFilter(filterData);
        if (tripList == null) {
            return riderMyTripList;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm d'th' MMMM");
        for (Trip perTrip : tripList) {
            RiderMyTripDataDto riderDataList = new RiderMyTripDataDto();
            riderDataList.setServiceTypeId(perTrip.getServiceType().getServiceTypeId());
            riderDataList.setVehicleTypeId(perTrip.getVehicleId().getVehicleId());
            riderDataList.setPickUpLocation(perTrip.getPickupAddress());
            riderDataList.setDropOffLocation(perTrip.getDropoffAddress());
            riderDataList.setTripDate(formatLocalDateTime(perTrip.getCreatedDate(), formatter));
            riderDataList.setDistance(perTrip.getDistance());
            riderDataList.setCharges(perTrip.getCharges());
            riderDataList.setTripId(perTrip.getTripCode());
            UserDetails riderDetails = userDetailsDao.getUserDetailsByUserId(perTrip.getTripUserId().getUserId());
            riderDataList.setRiderName(riderDetails.getFirstName() + " " + riderDetails.getLastName());
            try {
                riderDataList.setDuration(perTrip.getEstimatedTime().toString());
            } catch (NullPointerException e) {
                riderDataList.setDuration(null);
            }
            if (perTrip.isAccepted()) {
                riderDataList.setIsAccepted(1);
                riderDataList.setIsCancelationDetails(2);
                riderDataList.setIsCaptainDetails(1);
                UserDetails captainDetails = userdetailsDao.getUserDetailsByUserId(perTrip.getCaptainUserObj().getUserId());
                riderDataList.setCaptainName(captainDetails.getFirstName() + " , " + captainDetails.getLastName());
                String captainProfilePath = "/UrbanRides/resources/uploads/captainDocuments/captain" + perTrip.getCaptainUserObj().getUserId() + "/profilePhoto.png";
                riderDataList.setCaptainProfilePath(captainProfilePath);
                GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripByTripId(perTrip.getTripId());
                if (generalTripDetails != null && generalTripDetails.getIsTripCompleted() || perTrip.getPaymentMethod() != null) {
                    riderDataList.setStatus(5); // completed
                    if (filterData.getTripStatus() != 5) {
                        if (filterData.getTripStatus() != 0) {
                            continue;
                        }
                    }
                } else {
                    riderDataList.setStatus(4); // running
                    if (filterData.getTripStatus() != 4) {
                        if (filterData.getTripStatus() != 0) {
                            continue;
                        }
                    }
                }
                PackageTrip packageTrip = packageTripDao.getPackageTripDataByTripId(perTrip.getTripId());
                if (packageTrip != null) {
                    riderDataList.setPickupDate(formatDateToString(packageTrip.getPickupDate()));
                    riderDataList.setDropOffDate(formatDateToString(packageTrip.getDropOffDate()));
                    riderDataList.setPickupTime(dateTimeConverter.convertTo24HourFormat(packageTrip.getPickupTime()));
                    riderDataList.setNumberOfPassengers(packageTrip.getNumPassengers());
                    riderDataList.setNumberOfDays(packageTrip.getNumOfDays());
                    riderDataList.setDailyPickUpDays(convertToDayNames(packageTrip.getDailyPickUp()));
                    riderDataList.setConcludeNotes(packageTrip.getConcludeNotes());
                    riderDataList.setSpecialInstruction(packageTrip.getSpecialInstructions());
                }

            } else {
                PackageTrip packageTrip = packageTripDao.getPackageTripDataByTripId(perTrip.getTripId());
                if (packageTrip != null) {
                    riderDataList.setPickupDate(formatDateToString(packageTrip.getPickupDate()));
                    riderDataList.setDropOffDate(formatDateToString(packageTrip.getDropOffDate()));
                    riderDataList.setPickupTime(dateTimeConverter.convertTo24HourFormat(packageTrip.getPickupTime()));
                    riderDataList.setNumberOfPassengers(packageTrip.getNumPassengers());
                    riderDataList.setNumberOfDays(packageTrip.getNumOfDays());
                    riderDataList.setDailyPickUpDays(convertToDayNames(packageTrip.getDailyPickUp()));
                    riderDataList.setConcludeNotes(packageTrip.getConcludeNotes());
                    riderDataList.setSpecialInstruction(packageTrip.getSpecialInstructions());
                }
                LocalDate today = LocalDate.now();
                riderDataList.setIsAccepted(2);
                riderDataList.setIsCaptainDetails(2);
                riderDataList.setCancellationReason(perTrip.getReasonForCancellation());
                if (perTrip.getReasonForCancellation() != null) {
                    riderDataList.setIsCancelationDetails(1);
                    riderDataList.setStatus(3); // cancelled
                    if (filterData.getTripStatus() != 3) {
                        if (filterData.getTripStatus() != 0) {
                            continue;
                        }
                    }
                } else if (packageTrip != null && !perTrip.isAccepted() && (today.isAfter(packageTrip.getPickupDate()) || today.isEqual(packageTrip.getPickupDate()))) {
                    riderDataList.setIsCancelationDetails(2);
                    riderDataList.setStatus(2); // expired
                    if (filterData.getTripStatus() != 2) {
                        if (filterData.getTripStatus() != 0) {
                            continue;
                        }
                    }
                } else {
                    riderDataList.setIsCancelationDetails(2);
                    riderDataList.setStatus(1); // pending
                    if (filterData.getTripStatus() != 1) {
                        if (filterData.getTripStatus() != 0) {
                            continue;
                        }
                    }
                }
            }
            riderMyTripList.add(riderDataList);
        }
        return riderMyTripList;
    }


    public static String convertToDayNames(String numOfDays) {
        Map<String, String> dayMap = new HashMap<>();
        dayMap.put("1", "Monday");
        dayMap.put("2", "Tuesday");
        dayMap.put("3", "Wednesday");
        dayMap.put("4", "Thursday");
        dayMap.put("5", "Friday");
        dayMap.put("6", "Saturday");
        dayMap.put("7", "Sunday");
        String[] daysArray = numOfDays.split(",");
        String dailyPickUpDays = java.util.Arrays.stream(daysArray).map(dayMap::get).filter(day -> day != null).collect(Collectors.joining(", "));
        return dailyPickUpDays;
    }

    @Transactional
    public List<AdminCaptainApproveDataDto> getAllCaptainData() {
        List<AdminCaptainApproveDataDto> captainData = new ArrayList<>();
        List<CaptainDetails> captainDetails = captainDetailsDao.getAllUnverifiedCaptain();
        for (CaptainDetails perCaptainList : captainDetails) {
            User user = usersDao.getUserByUserId(perCaptainList.getUser().getUserId());
            AdminCaptainApproveDataDto adminCaptainApproveDataDto = new AdminCaptainApproveDataDto();
            UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(user.getUserId());
            adminCaptainApproveDataDto.setCaptainId(user.getUserId());
            adminCaptainApproveDataDto.setCaptainName(userDetails.getFirstName() + " " + userDetails.getLastName());
            adminCaptainApproveDataDto.setPhone(userDetails.getPhone());
            adminCaptainApproveDataDto.setEmail(userDetails.getUser().getEmail());
            adminCaptainApproveDataDto.setCreatedDate(formatDateToString(userDetails.getCreatedDate()));
            adminCaptainApproveDataDto.setStatus(getStatusString(user.getAccountStatus()));
            String fileLocation = "/UrbanRides/resources/uploads/captainDocuments/captain" + user.getUserId();
            adminCaptainApproveDataDto.setAdharApprovedApprove(perCaptainList.isAdharApproved());
            adminCaptainApproveDataDto.setDrivingLicenceApprove(perCaptainList.isLicenseApproved());
            adminCaptainApproveDataDto.setRCCertificateApprove(perCaptainList.isRcApproved());
            adminCaptainApproveDataDto.setRCExpirationDateApprove(perCaptainList.isRcExpirationDateApproved());
            adminCaptainApproveDataDto.setDrivingLicenceExpiryDateApprove(perCaptainList.isLicenseExpirationDateApproved());
            adminCaptainApproveDataDto.setNumberPlateApprove(perCaptainList.isNumberplateApproved());
            adminCaptainApproveDataDto.setAdharCard(fileLocation + "/adharcard.pdf");
            adminCaptainApproveDataDto.setDrivingLicence(fileLocation + "/drivingLicense.pdf");
            adminCaptainApproveDataDto.setDrivingLicenceExpiryDate(formatDateToString(perCaptainList.getLicenseExpirationDate()));
            adminCaptainApproveDataDto.setRCCertificate(fileLocation + "/registrationCertificate.pdf");
            adminCaptainApproveDataDto.setRCExpirationDate(formatDateToString(perCaptainList.getRcExpirationDate()));
            adminCaptainApproveDataDto.setVehicleNumber(perCaptainList.getNumberPlate());
            if (user.getAccountStatus() == 3) {
                adminCaptainApproveDataDto.setStatus("new");
            } else if (user.getAccountStatus() == 5) {
                adminCaptainApproveDataDto.setStatus("re-upload");
            }
            if (user.getAccountStatus() == 3 || user.getAccountStatus() == 5) {
                captainData.add(adminCaptainApproveDataDto);
            }
        }
        return captainData;
    }

    public String getStatusString(int status) {
        switch (status) {
            case 3:
                return "Unverified";
            case 4:
                return "Verification Failed";
            default:
                return "Unknown";
        }
    }

    public static String formatDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public void adminCaptainApproveDoc(AproveCaptainsDataDto aproveCaptainsDataDto) {
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(aproveCaptainsDataDto.getCaptainId());
        if (captainDetails == null) {
            throw new IllegalArgumentException("Captain not found for ID: " + aproveCaptainsDataDto.getCaptainId());
        }
        List<String> verifiedDocs = aproveCaptainsDataDto.getVerifiedDocId() != null ? aproveCaptainsDataDto.getVerifiedDocId() : new ArrayList<>();
        List<String> unverifiedDocs = aproveCaptainsDataDto.getUnverifiedDocId() != null ? aproveCaptainsDataDto.getUnverifiedDocId() : new ArrayList<>();
        if (!captainDetails.isLicenseApproved()) {
            boolean licenseApproved = verifiedDocs.contains("drivingApprove") && !unverifiedDocs.contains("drivingUnapprove");
            captainDetails.setLicenseApproved(licenseApproved);
        }
        if (!captainDetails.isRcApproved()) {
            boolean rcApproved = verifiedDocs.contains("rcApprove") && !unverifiedDocs.contains("rcUnapprove");
            captainDetails.setRcApproved(rcApproved);
        }
        if (!captainDetails.isAdharApproved()) {
            boolean adharApproved = verifiedDocs.contains("adharApprove") && !unverifiedDocs.contains("adharUnapprove");
            captainDetails.setAdharApproved(adharApproved);
        }
        if (!captainDetails.isRcExpirationDateApproved()) {
            boolean rcExpirationDateApproved = verifiedDocs.contains("rcExpiryApprove") && !unverifiedDocs.contains("rcExpiryUnapprove");
            captainDetails.setRcExpirationDateApproved(rcExpirationDateApproved);
        }
        if (!captainDetails.isLicenseExpirationDateApproved()) {
            boolean drivingLicenceExpiryDateApproved = verifiedDocs.contains("drivingLicenceExpiryApprove") && !unverifiedDocs.contains("drivingLicenceExpiryUnapprove");
            captainDetails.setLicenseExpirationDateApproved(drivingLicenceExpiryDateApproved);

        }
        if (!captainDetails.isNumberplateApproved()) {
            boolean numberPlateApprove = verifiedDocs.contains("numberPlateApprove") && !unverifiedDocs.contains("numberPlateUnapprove");
            captainDetails.setNumberplateApproved(numberPlateApprove);
        }
        boolean allDocsApproved = captainDetails.isLicenseApproved() && captainDetails.isRcApproved() && captainDetails.isAdharApproved() && captainDetails.isRcExpirationDateApproved() && captainDetails.isLicenseExpirationDateApproved();
        captainDetails.setDocumentApproved(allDocsApproved);
        User user = usersDao.getUserByUserId(aproveCaptainsDataDto.getCaptainId());
        if (allDocsApproved) {
            user.setAccountStatus(5);
            NotificationLogs notificationLogs = new NotificationLogs();
            notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID12"));
            notificationLogs.setNotificationMsg("Your Document has been verified .");
            notificationLogs.setUser(user);
            sendNotiToCaptain(notificationLogs.getNotificationMsg());
            try {
                emailSend.allDocumentsApproved(user.getEmail());
            } catch (Exception e) {
                System.out.println("email not send");
            }
            notificationLogsDao.saveNotificationLog(notificationLogs);
        } else {
            user.setAccountStatus(4);
            NotificationLogs notificationLogs = new NotificationLogs();
            notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID13"));
            notificationLogs.setNotificationMsg("Some of your documents are not verified. Please re-upload the documents.");
            notificationLogs.setUser(user);
            sendNotiToCaptain(notificationLogs.getNotificationMsg());
            try {
                emailSend.documentsNotApproved(user.getEmail());
            } catch (Exception e) {
                System.out.println("email not send");
            }
            notificationLogsDao.saveNotificationLog(notificationLogs);
        }
        usersDao.updateUser(user);
        captainDetailsDao.updateCaptainDetail(captainDetails);
    }

    public void sendNotiToCaptain(String notiMsg) {
        Map<String, String> notification = new HashMap<>();
        notification.put("message", notiMsg);
        simpMessagingTemplate.convertAndSend("/topic/captain-incoming-notifications", notification);
    }
}


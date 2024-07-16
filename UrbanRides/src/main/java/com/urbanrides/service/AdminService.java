package com.urbanrides.service;


import com.urbanrides.dao.*;
import com.urbanrides.dtos.*;
import com.urbanrides.helper.CommonValidation;
import com.urbanrides.helper.NotificationTypeEnum;
import com.urbanrides.helper.PasswordToHash;
import com.urbanrides.helper.UserTypeEnum;
import com.urbanrides.model.NotificationLogs;
import com.urbanrides.model.SupportTypeLogs;
import com.urbanrides.model.User;
import com.urbanrides.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    CommonValidation commonValidation;

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

    public AdminCountData getDashCount() {
        AdminCountData adminCountData = new AdminCountData();
        adminCountData.setTotalUserCount(usersDao.totalCount());
        adminCountData.setGeneralBooking(tripDao.getCountOfGeneralBooking());
        adminCountData.setServiceBooking(tripDao.getCountOfServiceBooking());
        adminCountData.setTotalSuccessBooking(tripDao.getSuccessTripCount());
        return adminCountData;
    }

    public List<AdminQuerries> getSupportData() {
        List<AdminQuerries> adminQuerriesList = new ArrayList<>();
        List<SupportTypeLogs> supportTypeLogsList = supportTypeLogsDao.getAllLogsData();

        for (SupportTypeLogs supportTypeLogs : supportTypeLogsList) {
            AdminQuerries adminQuerries = new AdminQuerries();
            adminQuerries.setUserID(supportTypeLogs.getUserObj().getUserId());
            adminQuerries.setSupportId(supportTypeLogs.getSupportLogsId());
            UserDetails UserDetails = userdetailsDao.getUserDetailsByUserId(supportTypeLogs.getUserObj().getUserId());
            adminQuerries.setContactDetails(UserDetails.getPhone());
            adminQuerries.setSypportType(supportTypeLogs.getSupportType());
            adminQuerries.setAccountType(UserTypeEnum.getValueById(supportTypeLogs.getUserObj().getAccountType()));
            adminQuerries.setMessage(supportTypeLogs.getDescription());
            LocalDate createdDate = supportTypeLogs.getCreatedDate();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = createdDate.format(formatter);

            adminQuerries.setCreatedDate(formattedDate);
            adminQuerriesList.add(adminQuerries);
        }

        return adminQuerriesList;
    }


//    ----------------get user management detialos -=-------------/

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


    public ResponseEntity<?> riderPersonalDetailSubmit(RiderUMPersonalDetailDto riderUMPersonalDetailDto, HttpServletRequest request) {
        try {
            UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("adminSessionObj");

            UserDetails userDetails = userdetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
            if (userDetails == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            userDetails.setFirstName(riderUMPersonalDetailDto.getFirstName());
            userDetails.setLastName(riderUMPersonalDetailDto.getLastName());
            userDetails.setPhone(riderUMPersonalDetailDto.getPhone());
            userdetailsDao.updateUserDetails(userDetails);
            return new ResponseEntity<>("Personal details updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update personal details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-------------------------------------------------password--------------------------


    public String sendPassToService(RiderUMLoginDetails riderUMLoginDetails, HttpServletRequest req) throws Exception {
        // Custom validation
        String error = passwordValidation(riderUMLoginDetails);
        if (error != null) {
            return error;
        }
        updatePassword(riderUMLoginDetails, req);
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

    public void updatePassword(RiderUMLoginDetails riderUMLoginDetails, HttpServletRequest request) throws Exception {
        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("adminSessionObj");

        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
        if (user == null) {
            throw new Exception("User not found");
        }

        Boolean verified = passwordToHash.checkPassword(riderUMLoginDetails.getCurrentPassword(), user.getSalt(), user.getPasswordHash());
        if (verified) {
            updateToDB(user, riderUMLoginDetails);
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
            return user;
        } catch (Exception e) {
            throw new Exception("Error updating password: " + e.getMessage());
        }
    }


    //    -----------------file-----------------------


    public String updateProfilePic(RiderUMUpdateProfileLogo riderUMUpdateProfileLogo, HttpSession session) throws IOException {
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("adminSessionObj");
        // Get UserDetails object for the current user
        UserDetails userDetails = userdetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());


        String adminId = String.valueOf(userSessionObj.getUserId());
        String folderName = "adminProfilePics" + adminId; // Dynamic folder name
        String folderPath = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "adminDocuments" + File.separator + folderName;
        File folder = new File(folderPath);

        // Delete all existing files in the folder
        deleteFilesInFolder(folder);

        if (!folder.exists()) {
            // Create the folder if it doesn't exist
            folder.mkdirs();
        }

        String fileExtension = getFileExtension(riderUMUpdateProfileLogo.getProfilePhoto().getOriginalFilename()); // Get the actual file extension

        // Check for valid file extensions (.png or .jpg)
        if (!isValidFileExtension(fileExtension)) {
            throw new IOException("Invalid file extension: " + fileExtension);
        }

        String fileNameBase = "adminProfile";
        String fileName = fileNameBase + adminId + fileExtension; // Use riderId in the filename

        String fileProfile = folderPath + File.separator + fileName;
        System.out.println("Saving profile photo to: " + fileProfile);

        try (FileOutputStream fos = new FileOutputStream(fileProfile)) {
            byte[] data = riderUMUpdateProfileLogo.getProfilePhoto().getBytes();
            fos.write(data);
            System.out.println("Profile photo uploaded successfully!");
        } catch (IOException e) {
            System.out.println("Error uploading profile photo: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the controller
        }
        // Set profile photo flag and extension
        userDetails.setProfilePhoto(true);
        userDetails.setProfilePhotoExtention(getFileExtension(riderUMUpdateProfileLogo.getProfilePhoto().getOriginalFilename()));

        // Update user details in the database
        userdetailsDao.updateUserDetails(userDetails);
        userSessionObj.setProfileLoc("/resources/uploads/adminDocuments/adminProfilePics" + userSessionObj.getUserId() + "/adminProfile" + userSessionObj.getUserId() + userDetails.getProfilePhotoExtention());
        session.setAttribute("adminSessionObj", userSessionObj);

        // Get the relative path
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
            // Replace File.separator with "/" for web URL compatibility
            relativePath = relativePath.replace(File.separator, "/");
            // Ensure leading "/" for correct relative path
            if (!relativePath.startsWith("/")) {
                relativePath = "/" + relativePath;
            }
            return relativePath;
        }
        return fullPath; // If rootDir not found, return fullPath as is
    }


}

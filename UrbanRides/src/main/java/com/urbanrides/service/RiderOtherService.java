package com.urbanrides.service;


import com.urbanrides.dao.TripDao;
import com.urbanrides.dao.UserDetailsDao;
import com.urbanrides.dao.UsersDao;
import com.urbanrides.dtos.*;
import com.urbanrides.helper.CommonValidation;
import com.urbanrides.helper.NotificationTypeEnum;
import com.urbanrides.helper.PasswordToHash;
import com.urbanrides.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class RiderOtherService {

    @Autowired
    UsersDao usersDao;
    @Autowired
    private UserDetailsDao userdetailsdao;
    @Autowired
    private com.urbanrides.dao.NotificationLogsDao notificationLogsDao;


    @Autowired
    TripDao tripDao;

    public List<NotificationDataDto> getNotificationData() {
        List<NotificationDataDto> notiDtoList = new ArrayList<>();
        List<NotificationLogs> notificationLogsList = notificationLogsDao.getAllNotificationLogs();

        if (notificationLogsList != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm d'th' MMMM");

            for (NotificationLogs log : notificationLogsList) {
                NotificationDataDto dto = new NotificationDataDto();
                dto.setNotificationType(log.getNotificationType());
                dto.setNotificationMsg(log.getNotificationMsg());
                dto.setCreatedDate(formatLocalDateTime(log.getCreatedDate(), formatter));
                dto.setNotificationTypeId(NotificationTypeEnum.getIdByValue(log.getNotificationType()));
                notiDtoList.add(dto);
            }
        }

        return notiDtoList;
    }

    private String formatLocalDateTime(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.format(formatter);
    }


//    --------wallet--------------

    public double getAmount(HttpServletRequest req) {

//        User user = usersDao.getUserByUserId(1);
        //get session id
        UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(8);
        BigDecimal walletAmount = userDetails.getWallet();
        double amountDouble = walletAmount.setScale(2, RoundingMode.HALF_UP).doubleValue();
        return amountDouble;
    }


    public ResponseEntity<?> validateAndDepositAmount(double amount) {
        try {
            // Validate the amount
            if (amount <= 0 || amount > 5000) {
                return ResponseEntity.badRequest().body("Amount should be between 1 and 5000.");
            }

            // Process the deposit
            double updatedBalance = depositAmount(amount);

            // Return success response with updated balance
            return ResponseEntity.ok(updatedBalance);
        } catch (Exception e) {
            // Handle exceptions and errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update wallet balance.");
        }
    }

    public double depositAmount(double amount) {
        try {
            UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(8);

            BigDecimal currentAmount = userDetails.getWallet();
            BigDecimal newAmount = currentAmount.add(BigDecimal.valueOf(amount));

            userDetails.setWallet(newAmount);
            userdetailsdao.updateUserDetails(userDetails);

            return newAmount.setScale(2, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            // Handle exceptions and errors
            throw new RuntimeException("Failed to deposit amount into wallet.", e);
        }
    }


    //    -----payment data-----------------
    public List<RiderWalletDataDto> getPaymentData(HttpServletRequest req) {
        List<RiderWalletDataDto> walletList = new ArrayList<>();

//session
        List<Trip> tripList = tripDao.getTripForPayment(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm d'th' MMMM");

        for (Trip perTrip : tripList) {
            RiderWalletDataDto riderWalletDataDto = new RiderWalletDataDto();
            riderWalletDataDto.setServiceType(perTrip.getServiceType().getServiceTypeId());
            riderWalletDataDto.setDateAndTime(formatLocalDateTime(perTrip.getCreatedDate(), formatter));

//            UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(perTrip.getCaptainUserObj().getUserId());
            UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(8);
            if ("Pay with cash".equals(perTrip.getPaymentMethod())) {
                riderWalletDataDto.setPaymentMethod(1);
            } else {
                riderWalletDataDto.setPaymentMethod(2);
            }
            riderWalletDataDto.setWalletHeader("Paid to Taxi Driver - " + userDetails.getFirstName() + " " + userDetails.getLastName());
            riderWalletDataDto.setPaidAmount(perTrip.getCharges());
            walletList.add(riderWalletDataDto);
        }
        return walletList;
    }


//    ----------------get user management detialos -=-------------/

    public UserManagementDataDto getUserManagementDetails(HttpServletRequest request) {

//get session
        UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(8);
        UserManagementDataDto userManagementDataDto = new UserManagementDataDto();
        userManagementDataDto.setFirstName(userDetails.getFirstName());
        userManagementDataDto.setLastName(userDetails.getLastName());
        userManagementDataDto.setEmail(userDetails.getUser().getEmail());
        userManagementDataDto.setPhone(userDetails.getPhone());

        if (userDetails.isProfilePhoto()) {

            userManagementDataDto.setProfilePhotoPath("/UrbanRides/resources/uploads/riderDocuments/profilePhoto/" + 1);
//            userManagementDataDto.setProfilePhotoPath("/UrbanRides/resources/uploads/riderDocuments/profilePhoto/" + userId);

        }


        return userManagementDataDto;

    }


    public ResponseEntity<?> riderPersonalDetailSubmit(RiderUMPersonalDetailDto riderUMPersonalDetailDto, HttpServletRequest request) {
        try {
            UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(8);
            if (userDetails == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            userDetails.setFirstName(riderUMPersonalDetailDto.getFirstName());
            userDetails.setLastName(riderUMPersonalDetailDto.getLastName());
            userDetails.setPhone(riderUMPersonalDetailDto.getPhone());
            userdetailsdao.updateUserDetails(userDetails);
            return new ResponseEntity<>("Personal details updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update personal details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-------------------------------------------------password--------------------------
    @Autowired
    PasswordToHash passwordToHash;
    @Autowired
    CommonValidation commonValidation;

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
        User user = usersDao.getUserByUserId(8);
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
            return user;
        } catch (Exception e) {
            throw new Exception("Error updating password: " + e.getMessage());
        }
    }


    //    -----------------file-----------------------
    public String updateProfilePic(RiderUMUpdateProfileLogo riderUMUpdateProfileLogo, HttpSession session) throws IOException {
        String riderId = "2"; // Assuming this method exists to get riderId
        String folderName = "riderProfilePics" + riderId;
        String folderPath = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "riderDocuments" + File.separator + folderName;
        File folder = new File(folderPath);

        if (!folder.exists()) {
            // Create the folder if it doesn't exist
            folder.mkdirs();
        }

        // Dynamically generate the file name based on existing files in the folder
        int i = 1;
        String fileExtension = getFileExtension(riderUMUpdateProfileLogo.getProfilePhoto().getOriginalFilename()); // Get the actual file extension

        // Check for valid file extensions (.png or .jpg)
        if (!isValidFileExtension(fileExtension)) {
            throw new IOException("Invalid file extension: " + fileExtension);
        }

        String fileNameBase = "riderProfile";
        String fileName = fileNameBase + i + fileExtension;

        // Check for existing file names and find the next available file name
        while (new File(folderPath + File.separator + fileName).exists()) {
            i++;
            fileName = fileNameBase + i + fileExtension;
        }

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

        // Get the relative path
        String relativePath = getRelativePath(fileProfile, session);

        return relativePath;
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
        String rootDir = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "riderDocuments" + File.separator;
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

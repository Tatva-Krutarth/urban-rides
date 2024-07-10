package com.urbanrides.service;


import com.urbanrides.dao.*;
import com.urbanrides.dtos.*;
import com.urbanrides.helper.CommonValidation;
import com.urbanrides.helper.NotificationTypeEnum;
import com.urbanrides.helper.PasswordToHash;
import com.urbanrides.helper.SupportType;
import com.urbanrides.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import java.util.UUID;

@Service
public class CaptainOtherService {
    @Autowired
    PasswordToHash passwordToHash;
    @Autowired
    CommonValidation commonValidation;
    @Autowired
    private NotificationLogsDao notificationLogsDao;
    @Autowired
    private UsersDao usersDao;

    @Autowired
    private HttpSession httpSession;
    @Autowired
    private UserDetailsDao userdetailsdao;

    public List<NotificationDataDto> getNotificationData() {
        List<NotificationDataDto> notiDtoList = new ArrayList<>();

        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");

        List<NotificationLogs> notificationLogsList = notificationLogsDao.getAllNotificationLogs(userSessionObj.getUserId());

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


    @Autowired
    private CaptainDetailsDao captainDetailsDao;
//    ----------------get user management detialos -=-------------/

    public UserManagementDataDto getUserManagementDetails() {
        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");

        UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
        UserManagementDataDto userManagementDataDto = new UserManagementDataDto();
        userManagementDataDto.setFirstName(userDetails.getFirstName());
        userManagementDataDto.setLastName(userDetails.getLastName());
        userManagementDataDto.setEmail(userDetails.getUser().getEmail());
        userManagementDataDto.setPhone(userDetails.getPhone());

        if (captainDetails.isProfilePhoto()) {
            userManagementDataDto.setProfilePhotoPath("/UrbanRides/resources/uploads/captainDocuments/captain" + userSessionObj.getUserId() + "/profilePhoto" + captainDetails.getProfilePhotoExtension());
        }
        return userManagementDataDto;

    }


    public ResponseEntity<?> riderPersonalDetailSubmit(RiderUMPersonalDetailDto riderUMPersonalDetailDto, HttpServletRequest request) {
        try {
            UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");

            UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());
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
        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");
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
            return user;
        } catch (Exception e) {
            throw new Exception("Error updating password: " + e.getMessage());
        }
    }


    //    -----------------file-----------------------


    public String updateProfilePic(RiderUMUpdateProfileLogo riderUMUpdateProfileLogo, HttpSession session) throws IOException {
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("captainSessionObj");        // Get UserDetails object for the current user
        UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());


        String captainId = String.valueOf(userSessionObj.getUserId());
        String folderName = "captain" + captainId; // Dynamic folder name
        String folderPath = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "captainDocuments" + File.separator + folderName;
        File folder = new File(folderPath);


        if (!folder.exists()) {
            // Create the folder if it doesn't exist
            folder.mkdirs();
        }

        String fileExtension = getFileExtension(riderUMUpdateProfileLogo.getProfilePhoto().getOriginalFilename()); // Get the actual file extension

        // Check for valid file extensions (.png or .jpg)
        if (!isValidFileExtension(fileExtension)) {
            throw new IOException("Invalid file extension: " + fileExtension);
        }

        String fileNameBase = "profilePhoto";
        String fileName = fileNameBase + fileExtension; // Use riderId in the filename

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
        userdetailsdao.updateUserDetails(userDetails);


        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
        captainDetails.setProfilePhotoExtension(getFileExtension(riderUMUpdateProfileLogo.getProfilePhoto().getOriginalFilename()));
        captainDetailsDao.updateCaptainDetail(captainDetails);
        userSessionObj.setProfileLoc("/UrbanRides/resources/uploads/captainDocuments/captain" + userSessionObj.getUserId() + "/profilePhoto/" + userDetails.getProfilePhotoExtention());

        session.setAttribute("captainSessionObj", userSessionObj);

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
        String rootDir = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "captainDocuments" + File.separator;
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


    @Autowired
    private SupportTypeLogsDao supportTypeLogsDao;


    //    ------------------captain help------------------------
    public void getSupportSaveToLogs(RiderGetSupportDto riderGetSupportDto, HttpSession session) throws IOException {
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("captainSessionObj");
        SupportTypeLogs supportTypeLogs = new SupportTypeLogs();
        supportTypeLogs.setSupportType(SupportType.getValueById(riderGetSupportDto.getSupportType()));
        supportTypeLogs.setDescription(riderGetSupportDto.getDescription());
        MultipartFile complainFile = riderGetSupportDto.getUploadFile();

        if (complainFile == null || complainFile.isEmpty()) {
            supportTypeLogs.setFile(false);
        } else {
            supportTypeLogs.setFile(true);

            // Define the base directory path
            String baseDir = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "captainDocuments" + File.separator;

            // Define the user-specific directory path
            String userDir = baseDir + "captainComplain" + userSessionObj.getUserId() + File.separator;

            // Create user-specific directory if it does not exist
            File userDirectory = new File(userDir);
            if (!userDirectory.exists()) {
                userDirectory.mkdirs();
            }

            // Define the file name
            String fileExtension = getFileExtension(complainFile.getOriginalFilename());
            String fileName = "complain" + userSessionObj.getUserId() + fileExtension;
            String filePath = userDir + fileName;

            // Save the file
            complainFile.transferTo(new File(filePath));

            supportTypeLogs.setFileName(fileName);
            supportTypeLogs.setFileExtention(fileExtension); // Assuming SupportTypeLogs has a field for file extension
        }

        // Get user object from session or database
        User user = (User) session.getAttribute("user"); // Assuming user is stored in session
        if (user == null) {
            user = usersDao.getUserByUserId(userSessionObj.getUserId()); // Default user, replace with actual logic
        }
        supportTypeLogs.setUserObj(user);

        // Generate support case ID
        supportTypeLogs.setSupportCaseId(generateSupportCaseId());

        // Save to database
        supportTypeLogsDao.saveSupportLogs(supportTypeLogs);
    }

    @Autowired
    private TripDao tripDao;

    private String generateSupportCaseId() {
        // Implement your logic to generate a unique support case ID
        return UUID.randomUUID().toString();
    }

    //    ----------------------------------------------------------payment data-----------------

    public double getAmount() {
        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");

        UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());
        BigDecimal walletAmount = userDetails.getWallet();
        double amountDouble = walletAmount.setScale(2, RoundingMode.HALF_UP).doubleValue();
        return amountDouble;
    }

    public ResponseEntity<?> validateAndDepositAmount(double amount) {
        try {
            // Validate the amount
            if (amount <= 0 || amount > 50000) {
                return ResponseEntity.badRequest().body("Amount should be less between 1 and 50000.");
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
        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");

        try {
            UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());

            BigDecimal currentAmount = userDetails.getWallet();
            BigDecimal newAmount = currentAmount.subtract(BigDecimal.valueOf(amount));

            if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Insufficient funds. The new wallet amount cannot be negative.");
            }

            userDetails.setWallet(newAmount);
            userdetailsdao.updateUserDetails(userDetails);

            return newAmount.setScale(2, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            // Handle exceptions and errors
            throw new RuntimeException("Failed to deposit amount into wallet.", e);
        }
    }


    public List<RiderWalletDataDto> getPaymentData() {
        List<RiderWalletDataDto> walletList = new ArrayList<>();

        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");
        List<Trip> tripList = tripDao.getTripForPaymentForCaptain(userSessionObj.getUserId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm d'th' MMMM");

        for (Trip perTrip : tripList) {
            RiderWalletDataDto riderWalletDataDto = new RiderWalletDataDto();
            riderWalletDataDto.setServiceType(perTrip.getServiceType().getServiceTypeId());
            riderWalletDataDto.setDateAndTime(formatLocalDateTime(perTrip.getCreatedDate(), formatter));

            UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(perTrip.getTripUserId().getUserId());
//            UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(8);
            if ("Pay with cash".equalsIgnoreCase(perTrip.getPaymentMethod())) {
                riderWalletDataDto.setPaymentMethod(1);
                riderWalletDataDto.setWalletHeader(userDetails.getFirstName() + " , " + userDetails.getLastName() + "paid you via wallet. ");

            } else {
                riderWalletDataDto.setPaymentMethod(2);
                riderWalletDataDto.setWalletHeader(userDetails.getFirstName() + " " + userDetails.getLastName() + "paid you via cash. ");

            }
            riderWalletDataDto.setPaidAmount(perTrip.getCharges());
            walletList.add(riderWalletDataDto);
        }
        return walletList;
    }


}

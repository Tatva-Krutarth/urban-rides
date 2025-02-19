package com.urbanrides.service;

import com.urbanrides.dao.*;
import com.urbanrides.dtos.*;
import com.urbanrides.enums.NotificationTypeEnum;
import com.urbanrides.enums.SupportType;
import com.urbanrides.helper.*;
import com.urbanrides.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RiderOtherService {

    @Autowired
    private HttpSession session;

    @Autowired
    private GeneralTripDetailsDao generalTripDetailsDao;

    @Autowired
    private PackageTripDao packageTripDao;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    UsersDao usersDao;

    @Autowired
    private UserDetailsDao userdetailsdao;

    @Autowired
    private com.urbanrides.dao.NotificationLogsDao notificationLogsDao;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    TripDao tripDao;

    @Autowired
    PasswordToHash passwordToHash;

    @Autowired
    CommonValidation commonValidation;

    @Autowired
    private SupportTypeLogsDao supportTypeLogsDao;

    @Autowired
    private EmailSend emailSend;

    @Autowired
    private DateTimeConverter  dateTimeConverter;

    public List<NotificationDataDto> getNotificationData() {
        List<NotificationDataDto> notiDtoList = new ArrayList<>();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
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

    public double getAmount() {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
        UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());
        BigDecimal walletAmount = userDetails.getWallet();
        double amountDouble = walletAmount.setScale(2, RoundingMode.HALF_UP).doubleValue();
        return amountDouble;
    }

    public ResponseEntity<Map<String, String>> validateAndDepositAmount(double amount) {
        Map<String, String> response = new HashMap<>();
        try {
            if (amount <= 0 || amount > 5000) {
                response.put("message", "Amount should be between 1 and 5000.");
                return ResponseEntity.badRequest().body(response);
            }
            double updatedBalance = depositAmount(amount);
            response.put("message", "Amount added successfully.");
            response.put("updatedBalance", String.valueOf(updatedBalance));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Failed to update wallet balance.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public double depositAmount(double amount) {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
        try {
            UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());
            BigDecimal currentAmount = userDetails.getWallet();
            BigDecimal newAmount = currentAmount.add(BigDecimal.valueOf(amount));
            userDetails.setWallet(newAmount);
            userdetailsdao.updateUserDetails(userDetails);
            NotificationLogs notificationLogs = new NotificationLogs();
            notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID1"));
            notificationLogs.setNotificationMsg("You have deposited " + amount + " to your wallet");
            User user = usersDao.getUserByUserId(userSessionObj.getUserId());
            notificationLogs.setUser(user);
            sendNotiToRider(notificationLogs.getNotificationMsg());
            notificationLogsDao.saveNotificationLog(notificationLogs);
            return newAmount.setScale(2, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to deposit amount into wallet.", e);
        }
    }

    public void sendNotiToRider(String notiMsg) {
        Map<String, String> notification = new HashMap<>();
        notification.put("message", notiMsg);
        simpMessagingTemplate.convertAndSend("/topic/rider-incoming-notifications", notification);
    }

    public List<RiderWalletDataDto> getPaymentData() {
        List<RiderWalletDataDto> walletList = new ArrayList<>();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
        List<Trip> tripList = tripDao.getTripForPayment(userSessionObj.getUserId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm d'th' MMMM");
        for (Trip perTrip : tripList) {
            RiderWalletDataDto riderWalletDataDto = new RiderWalletDataDto();
            riderWalletDataDto.setServiceType(perTrip.getServiceType().getServiceTypeId());
            riderWalletDataDto.setDateAndTime(formatLocalDateTime(perTrip.getCreatedDate(), formatter));
            UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(perTrip.getCaptainUserObj().getUserId());
            if ("Pay with cash".equalsIgnoreCase(perTrip.getPaymentMethod())) {
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

    public UserManagementDataDto getUserManagementDetails() {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) httpSession.getAttribute("riderSessionObj");
        UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());
        UserManagementDataDto userManagementDataDto = new UserManagementDataDto();
        userManagementDataDto.setFirstName(userDetails.getFirstName());
        userManagementDataDto.setLastName(userDetails.getLastName());
        userManagementDataDto.setEmail(userDetails.getUser().getEmail());
        userManagementDataDto.setPhone(userDetails.getPhone());
        if (userDetails.isProfilePhoto()) {
            userManagementDataDto.setProfilePhotoPath("/UrbanRides/resources/uploads/riderDocuments/riderProfilePics" + userSessionObj.getUserId() + "/riderProfile" + userSessionObj.getUserId() + userDetails.getProfilePhotoExtention());
        }
        return userManagementDataDto;
    }

    public ResponseEntity<Map<String, String>> riderPersonalDetailSubmit(RiderUMPersonalDetailDto riderUMPersonalDetailDto) {
        Map<String, String> response = new HashMap<>();
        try {
            UserSessionObjDto userSessionObj = (UserSessionObjDto) httpSession.getAttribute("riderSessionObj");
            UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());
            if (userDetails == null) {
                response.put("message", "User not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            String firstName = capitalizeFirstLetter(riderUMPersonalDetailDto.getFirstName());
            String lastName = capitalizeFirstLetter(riderUMPersonalDetailDto.getLastName());
            userDetails.setFirstName(firstName);
            userDetails.setLastName(lastName);
            userDetails.setPhone(riderUMPersonalDetailDto.getPhone());
            userdetailsdao.updateUserDetails(userDetails);
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

    public String sendPassToService(RiderUMLoginDetailsDto riderUMLoginDetails, HttpServletRequest req) throws Exception {
        try {
            String error = passwordValidation(riderUMLoginDetails);
            if (error != null) {
                return error;
            }
            UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
            if (userSessionObj == null) {
                throw new Exception("User session not found");
            }
            User user = usersDao.getUserByUserId(userSessionObj.getUserId());
            if (user == null) {
                throw new Exception("User not found");
            }
            updatePassword(riderUMLoginDetails);
            NotificationLogs notificationLogs = new NotificationLogs();
            notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID4"));
            notificationLogs.setNotificationMsg("The password has been changed from your profile.");
            notificationLogs.setUser(user);
            notificationLogsDao.saveNotificationLog(notificationLogs);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update login details: " + e.getMessage();
        }
    }

    public String passwordValidation(RiderUMLoginDetailsDto riderUMLoginDetails) {
        if (!commonValidation.isValidPassword(riderUMLoginDetails.getNewPassword())) {
            return "Password must be between 8 and 13 characters.";
        }
        if (!commonValidation.confirmPassword(riderUMLoginDetails.getNewPassword(), riderUMLoginDetails.getConfirmPassword())) {
            return "Passwords do not match.";
        }
        return null;
    }

    public void updatePassword(RiderUMLoginDetailsDto riderUMLoginDetails) throws Exception {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) httpSession.getAttribute("riderSessionObj");
        if (userSessionObj == null) {
            throw new Exception("User session not found");
        }
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

    public User updateToDB(User user, RiderUMLoginDetailsDto riderUMLoginDetails) throws Exception {
        try {
            String saltString = passwordToHash.generateSalt();
            String hashedPasswordString = passwordToHash.hashPassword(riderUMLoginDetails.getNewPassword(), saltString);
            user.setSalt(saltString);
            user.setPasswordHash(hashedPasswordString);
            usersDao.updateUser(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error updating password: " + e.getMessage());
        }
    }

    public Map<String, String> updateProfilePic(RiderUMUpdateProfileLogoDto riderUMUpdateProfileLogo, HttpSession session) throws IOException {
        Map<String, String> response = new HashMap<>();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
        UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());
        String riderId = String.valueOf(userSessionObj.getUserId());
        String folderName = "riderProfilePics" + riderId;
        String folderPath = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "riderDocuments" + File.separator + folderName;
        File folder = new File(folderPath);
        deleteFilesInFolder(folder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String fileExtension = getFileExtension(riderUMUpdateProfileLogo.getProfilePhoto().getOriginalFilename());
        if (!isValidFileExtension(fileExtension)) {
            response.put("error", "Invalid file extension: " + fileExtension);
            return response;
        }
        String fileNameBase = "riderProfile";
        String fileName = fileNameBase + riderId + fileExtension;
        String fileProfile = folderPath + File.separator + fileName;
        try (FileOutputStream fos = new FileOutputStream(fileProfile)) {
            byte[] data = riderUMUpdateProfileLogo.getProfilePhoto().getBytes();
            fos.write(data);
        } catch (IOException e) {
            response.put("error", "Failed to upload profile photo: " + e.getMessage());
            throw e;
        }
        userDetails.setProfilePhoto(true);
        userDetails.setProfilePhotoExtention(fileExtension);
        userdetailsdao.updateUserDetails(userDetails);
        userSessionObj.setProfileLoc("/resources/uploads/riderDocuments/riderProfilePics" + userSessionObj.getUserId() + "/riderProfile" + userSessionObj.getUserId() + userDetails.getProfilePhotoExtention());
        userSessionObj.setProfilePhoto(1);
        session.setAttribute("riderSessionObj", userSessionObj);
        String relativePath = getRelativePath(fileProfile, session);
        response.put("relativePath", relativePath);
        return response;
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
        String rootDir = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "riderDocuments" + File.separator;
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

    public List<RiderMyTripDataDto> getTripDetails() {
        List<RiderMyTripDataDto> riderMyTripList = new ArrayList<>();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
        List<Trip> tripList = tripDao.getAllTrip(userSessionObj.getUserId());
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
            riderDataList.setTripId(perTrip.getTripCode());
            riderDataList.setCharges(perTrip.getCharges());
            if (perTrip.isAccepted()) {
                riderDataList.setIsAccepted(1);
                riderDataList.setIsCancelationDetails(2);
                riderDataList.setIsCaptainDetails(1);
                try {
                    riderDataList.setDuration(perTrip.getEstimatedTime().toString());
                } catch (NullPointerException e) {
                    riderDataList.setDuration("--");
                }
                UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(perTrip.getCaptainUserObj().getUserId());
                riderDataList.setCaptainName(userDetails.getFirstName() + " , " + userDetails.getLastName());
                String captainProfilePath = "/UrbanRides/resources/uploads/captainDocuments/captain" + perTrip.getCaptainUserObj().getUserId() + "/profilePhoto.png";
                riderDataList.setCaptainProfilePath(captainProfilePath);
                GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripByTripId(perTrip.getTripId());
                if (generalTripDetails == null) {
                    riderDataList.setStatus(2);
                } else {
                    riderDataList.setCaptainRatting(generalTripDetails.getCaptainRating());
                    if (generalTripDetails.getIsTripCompleted()) {
                        riderDataList.setStatus(1);
                        riderDataList.setConcludeNotes(generalTripDetails.getFeedback());
                    } else {
                        riderDataList.setStatus(2);
                    }
                }
                if (perTrip.getServiceType().getServiceTypeId() != 1) {
                    PackageTrip packageTrip = packageTripDao.getPackageTripDataByTripId(perTrip.getTripId());
                    riderDataList.setPickupDate(dateTimeConverter.formatDateToString(packageTrip.getPickupDate()));
                    riderDataList.setDropOffDate(dateTimeConverter.formatDateToString(packageTrip.getDropOffDate()));
                    riderDataList.setPickupTime(dateTimeConverter.convertTo24HourFormat(packageTrip.getPickupTime()));
                    riderDataList.setNumberOfPassengers(packageTrip.getNumPassengers());
                    riderDataList.setNumberOfDays(packageTrip.getNumOfDays());
                    riderDataList.setDailyPickUpDays(dateTimeConverter.convertToDayNames(packageTrip.getDailyPickUp()));
                    if (perTrip.getPaymentMethod() != null) {
                        // Completed
                        riderDataList.setStatus(1);
                        riderDataList.setDistance(perTrip.getDistance());
                        riderDataList.setConcludeNotes(packageTrip.getConcludeNotes());
                    } else {
                        // Pending
                        riderDataList.setStatus(5);
                    }
                    riderDataList.setSpecialInstruction(packageTrip.getSpecialInstructions());
                    riderDataList.setEmergencyContact(packageTrip.getEmergencyContact());
                }

            } else {
                riderDataList.setIsAccepted(2);
                riderDataList.setIsCaptainDetails(2);
                riderDataList.setIsCancelationDetails(1);
                riderDataList.setStatus(2);
                riderDataList.setCancellationReason(perTrip.getReasonForCancellation());
                if (perTrip.getReasonForCancellation() == null) {
                    riderDataList.setCancellationReason("Captain Not Available Or Cancelled By Rider");
                } else {
                    riderDataList.setCancellationReason(perTrip.getReasonForCancellation());
                }
                if (perTrip.getServiceType().getServiceTypeId() != 1) {
                    PackageTrip packageTrip = packageTripDao.getPackageTripDataByTripId(perTrip.getTripId());
                    LocalDate today = LocalDate.now();
                    if (packageTrip != null) {
                        riderDataList.setPickupDate(dateTimeConverter.formatDateToString(packageTrip.getPickupDate()));
                        riderDataList.setDropOffDate(dateTimeConverter.formatDateToString(packageTrip.getDropOffDate()));
                        riderDataList.setNumberOfPassengers(packageTrip.getNumPassengers());
                        riderDataList.setDailyPickUpDays(dateTimeConverter.convertToDayNames(packageTrip.getDailyPickUp()));
                        riderDataList.setSpecialInstruction(packageTrip.getSpecialInstructions());
                        riderDataList.setEmergencyContact(packageTrip.getEmergencyContact());
                        riderDataList.setNumberOfDays(packageTrip.getNumOfDays());
                        riderDataList.setPickupTime(dateTimeConverter.convertTo24HourFormat(packageTrip.getPickupTime()));
                    }
                    if (packageTrip != null && packageTrip.getConcludeNotes() != null && perTrip.getPaymentMethod() != null) {
                        // Completed
                        riderDataList.setStatus(1);
                        riderDataList.setDistance(perTrip.getDistance());
                        riderDataList.setConcludeNotes(packageTrip.getConcludeNotes());
                    } else if (packageTrip != null && !perTrip.isAccepted() && (today.isAfter(packageTrip.getPickupDate()) || today.isEqual(packageTrip.getPickupDate()))) {
                        // Pending
                        riderDataList.setStatus(4);
                    } else {
                        // Pending
                        riderDataList.setStatus(3);
                    }
                }
            }
            riderMyTripList.add(riderDataList);
        }
        return riderMyTripList;
    }

    public void getSupportSaveToLogs(RiderGetSupportDto riderGetSupportDto, HttpSession session) throws IOException {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
        SupportTypeLogs supportTypeLogs = new SupportTypeLogs();
        try {
            SupportTypeLogs activeSupport = supportTypeLogsDao.checkActiveRequests(userSessionObj.getUserId());
            if (activeSupport != null) {
                throw new IllegalStateException("You already have a pending request");
            }
            supportTypeLogs.setSupportType(SupportType.getValueById(riderGetSupportDto.getSupportType()));
            supportTypeLogs.setDescription(riderGetSupportDto.getDescription());
            MultipartFile complainFile = riderGetSupportDto.getUploadFile();
            if (complainFile == null || complainFile.isEmpty()) {
                supportTypeLogs.setFile(false);
            } else {
                supportTypeLogs.setFile(true);
                String baseDir = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "riderDocuments" + File.separator;
                String userDir = baseDir + "riderComplain" + userSessionObj.getUserId() + File.separator;
                File userDirectory = new File(userDir);
                if (!userDirectory.exists()) {
                    userDirectory.mkdirs();
                }
                String fileExtension = getFileExtension(complainFile.getOriginalFilename());
                String fileName = "complain" + userSessionObj.getUserId() + fileExtension;
                String filePath = userDir + fileName;
                complainFile.transferTo(new File(filePath));
                supportTypeLogs.setFileName(fileName);
                supportTypeLogs.setFileExtention(fileExtension);
            }
            User user = usersDao.getUserByUserId(userSessionObj.getUserId());
            supportTypeLogs.setUserObj(user);
            supportTypeLogs.setSupportCaseId(generateSupportCaseId());
            NotificationLogs notificationLogs = new NotificationLogs();
            notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID5"));
            notificationLogs.setNotificationMsg("You have raised a " + supportTypeLogs.getSupportType());
            notificationLogs.setUser(user);
            try {
                emailSend.getSupportRequest(user.getEmail(), supportTypeLogs.getSupportCaseId(), supportTypeLogs.getSupportType(), supportTypeLogs.getDescription());
            } catch (Exception e) {
                throw new IllegalStateException("Error while sending email");
            }
            notificationLogsDao.saveNotificationLog(notificationLogs);
            supportTypeLogsDao.saveSupportLogs(supportTypeLogs);

        } catch (IOException e) {
            throw new IOException("Failed to handle file upload", e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    private String generateSupportCaseId() {
        return UUID.randomUUID().toString();
    }

    public SupportRequestDataDto findSupportRequestById(String id) {
        SupportTypeLogs supportTypeLogs = supportTypeLogsDao.getSupportPerData(id);
        SupportRequestDataDto supportRequestDataDto = new SupportRequestDataDto();

        if (supportTypeLogs == null) {
            return supportRequestDataDto;
        }
        supportRequestDataDto.setId(supportTypeLogs.getSupportCaseId());
        supportRequestDataDto.setType(supportTypeLogs.getSupportType());
        supportRequestDataDto.setDescription(supportTypeLogs.getDescription());
        if (supportTypeLogs.getAdminObj() == null) {
            supportRequestDataDto.setStatus("Pending");
        } else if (supportTypeLogs.getAdminObj() != null && !supportTypeLogs.isSolved()) {
            supportRequestDataDto.setStatus("Active");
        } else {
            supportRequestDataDto.setStatus("Completed");
        }
        return supportRequestDataDto;
    }
}


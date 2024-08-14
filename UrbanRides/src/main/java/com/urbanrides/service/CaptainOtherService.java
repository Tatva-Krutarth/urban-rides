package com.urbanrides.service;


import com.urbanrides.dao.*;
import com.urbanrides.dtos.*;
import com.urbanrides.helper.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    EmailSend emailSend;

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


    public ResponseEntity<Map<String, String>> captainPersonalDetailSubmit(RiderUMPersonalDetailDto riderUMPersonalDetailDto) {
        Map<String, String> response = new HashMap<>();

        try {
            UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");
            String firstName = capitalizeFirstLetter(riderUMPersonalDetailDto.getFirstName());
            String lastName = capitalizeFirstLetter(riderUMPersonalDetailDto.getLastName());

            UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());
            if (userDetails == null) {
                response.put("message", "User not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            userDetails.setFirstName(firstName);
            userDetails.setLastName(lastName);
            userDetails.setPhone(riderUMPersonalDetailDto.getPhone());
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
    //-------------------------------------------------password--------------------------


    public String sendPassToService(RiderUMLoginDetails riderUMLoginDetails, HttpServletRequest req) throws Exception {
        try {
            // Custom validation
            String error = passwordValidation(riderUMLoginDetails);
            if (error != null) {
                return error;
            }
            UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");
            if (userSessionObj == null) {
                throw new Exception("User session not found");
            }
            User user = usersDao.getUserByUserId(userSessionObj.getUserId());
            if (user == null) {
                throw new Exception("User not found");
            }

            updatePassword(riderUMLoginDetails, req);

            NotificationLogs notificationLogs = new NotificationLogs();
            notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID4"));
            notificationLogs.setNotificationMsg("The password has been changed from your profile.");
            notificationLogs.setUser(user);
            notificationLogsDao.saveNotificationLog(notificationLogs);
            return null;
        } catch (Exception e) {
            // Log the exception (you can use a logger here)
            e.printStackTrace();
            return "Failed to update login details: " + e.getMessage();
        }
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
            return user;
        } catch (Exception e) {
            throw new Exception("Error updating password: " + e.getMessage());
        }
    }


    //    -----------------file-----------------------


    public String updateProfilePic(RiderUMUpdateProfileLogo riderUMUpdateProfileLogo, HttpSession session) throws IOException {
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("captainSessionObj");
        if (userSessionObj == null) {
            throw new IOException("User session not found.");
        }

        UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());
        if (userDetails == null) {
            throw new IOException("User details not found.");
        }

        String captainId = String.valueOf(userSessionObj.getUserId());
        String folderName = "captain" + captainId; // Dynamic folder name
        String folderPath = session.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "captainDocuments" + File.separator + folderName;
        File folder = new File(folderPath);

        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new IOException("Failed to create directory: " + folderPath);
            }
        }

        String fileExtension = getFileExtension(riderUMUpdateProfileLogo.getProfilePhoto().getOriginalFilename()); // Get the actual file extension

        // Check for valid file extensions (.png or .jpg)
        if (!isValidFileExtension(fileExtension)) {
            throw new IOException("Invalid file extension: " + fileExtension);
        }

        String fileNameBase = "profilePhoto";
        String fileName = fileNameBase + fileExtension; // Use captainId in the filename

        String fileProfile = folderPath + File.separator + fileName;
        System.out.println("Saving profile photo to: " + fileProfile);

        try (FileOutputStream fos = new FileOutputStream(fileProfile)) {
            byte[] data = riderUMUpdateProfileLogo.getProfilePhoto().getBytes();
            fos.write(data);
            System.out.println("Profile photo uploaded successfully!");
        } catch (IOException e) {
            System.out.println("Error uploading profile photo: " + e.getMessage());
            throw new IOException("Error uploading profile photo: " + e.getMessage(), e);
        }

        // Set profile photo flag and extension
        userDetails.setProfilePhoto(true);
        userDetails.setProfilePhotoExtention(fileExtension);

        // Update user details in the database
        try {
            userdetailsdao.updateUserDetails(userDetails);
        } catch (Exception e) {
            throw new IOException("Failed to update user details in the database: " + e.getMessage(), e);
        }

        // Update CaptainDetails
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
        if (captainDetails == null) {
            throw new IOException("Captain details not found.");
        }
        captainDetails.setProfilePhotoExtension(fileExtension);
        try {
            captainDetailsDao.updateCaptainDetail(captainDetails);
        } catch (Exception e) {
            throw new IOException("Failed to update captain details in the database: " + e.getMessage(), e);
        }

        userSessionObj.setProfileLoc("/resources/uploads/captainDocuments/captain" + userSessionObj.getUserId() + "/profilePhoto" + fileExtension);
        session.setAttribute("captainSessionObj", userSessionObj);

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

        try {
            supportTypeLogs.setSupportType(SupportType.getValueById(riderGetSupportDto.getSupportType()));
            supportTypeLogs.setDescription(riderGetSupportDto.getDescription());
            MultipartFile complainFile = riderGetSupportDto.getUploadFile();
            // Check for active requests
            SupportTypeLogs activeSupport = supportTypeLogsDao.checkActiveRequests(userSessionObj.getUserId());
            if (activeSupport != null) {
                throw new IllegalStateException("You already have a pending request");
            }
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

            User user = usersDao.getUserByUserId(userSessionObj.getUserId()); // Default user, replace with actual logic
            supportTypeLogs.setUserObj(user);

            // Generate support case ID
            supportTypeLogs.setSupportCaseId(generateSupportCaseId());
            // Create and save notification log
            NotificationLogs notificationLogs = new NotificationLogs();
            notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID5"));
            notificationLogs.setNotificationMsg("You have raised a " + supportTypeLogs.getSupportType());
            notificationLogs.setUser(user);
            notificationLogsDao.saveNotificationLog(notificationLogs);
            // Save to database
            supportTypeLogsDao.saveSupportLogs(supportTypeLogs);

            try {
                emailSend.getSupportRequest(user.getEmail(), supportTypeLogs.getSupportCaseId(), supportTypeLogs.getSupportType(), supportTypeLogs.getDescription());
            } catch (Exception e) {
                throw new IllegalStateException("Error while sending email");

            }
        } catch (IOException e) {
            // Log and handle file-related issues
            System.err.println("File handling error: " + e.getMessage());
            throw new IOException("Failed to handle file upload", e);
        } catch (IllegalStateException e) {
            // Log and handle application-specific errors
            System.err.println("Application error: " + e.getMessage());
            throw new IllegalStateException(e.getMessage(), e);
        } catch (Exception e) {
            // Log and handle any other unexpected exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred", e);
        }
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

    public double getTotalEarnings() {
        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");
        List<Trip> trips = tripDao.getTripsForTotalEarnings(userSessionObj.getUserId());
        double totalEarnings = 0.0;
        for (Trip trip : trips) {
            totalEarnings += trip.getCharges();
        }
        System.out.println(totalEarnings);
        return totalEarnings;
    }


    public ResponseEntity<Map<String, String>> validateAndDepositAmount(double amount) {
        Map<String, String> response = new HashMap<>();
        try {
            // Validate the amount
            if (amount <= 0 || amount > 50000) {
                response.put("error", "Amount should be between 1 and 50000.");
                return ResponseEntity.badRequest().body(response);
            }

            // Process the deposit
            double updatedBalance = depositAmount(amount);

            // Return success response with updated balance
            response.put("success", "Amount deposited successfully.");
            response.put("updatedBalance", String.valueOf(updatedBalance));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle exceptions and errors
            response.put("error", "Insufficient balance.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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

            NotificationLogs notificationLogs = new NotificationLogs();
            notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID10"));
            notificationLogs.setNotificationMsg("You have withdrawn " + amount + " Rs from your account ");
            User user = usersDao.getUserByUserId(userSessionObj.getUserId());
            notificationLogs.setUser(user);
            notificationLogsDao.saveNotificationLog(notificationLogs);
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
                riderWalletDataDto.setWalletHeader(userDetails.getFirstName() + " , " + userDetails.getLastName() + " paid you via cash. ");

            } else {
                riderWalletDataDto.setPaymentMethod(2);
                riderWalletDataDto.setWalletHeader(userDetails.getFirstName() + " " + userDetails.getLastName() + " paid you via Wallet. ");

            }
            riderWalletDataDto.setPaidAmount(perTrip.getCharges());
            walletList.add(riderWalletDataDto);
        }
        return walletList;
    }


//    ----------captain my trips=--------------------------

    @Autowired
    private GeneralTripDetailsDao generalTripDetailsDao;
    @Autowired
    private PackageTripDao packageTripDao;
//    -------------------------get trip details-----------------------------------------

    public List<RiderMyTripDataDto> getTripDetails() {
        List<RiderMyTripDataDto> riderMyTripList = new ArrayList<>();
        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");

        List<Trip> tripList = tripDao.getAllTripOfCaptain(userSessionObj.getUserId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a, d'th' MMMM yyyy");

        for (Trip perTrip : tripList) {
            RiderMyTripDataDto riderDataList = new RiderMyTripDataDto();
            riderDataList.setServiceTypeId(perTrip.getServiceType().getServiceTypeId());
            riderDataList.setVehicleTypeId(perTrip.getVehicleId().getVehicleId());
            riderDataList.setPickUpLocation(perTrip.getPickupAddress());
            riderDataList.setDropOffLocation(perTrip.getDropoffAddress());
            riderDataList.setTripDate(formatLocalDateTime(perTrip.getCreatedDate(), formatter));
            riderDataList.setDistance(perTrip.getDistance());
            riderDataList.setTripId(perTrip.getTripCode());
            riderDataList.setTripId(perTrip.getTripCode());


//            for service type 1 only
            if (perTrip.isAccepted()) {
                riderDataList.setIsAccepted(1);
                riderDataList.setIsCancelationDetails(2);
                riderDataList.setIsCaptainDetails(1);
                //it should be ride completion time
                try {
                    riderDataList.setDuration(perTrip.getEstimatedTime().toString());
                } catch (NullPointerException e) {
                    riderDataList.setDuration("--");
                }

                riderDataList.setCharges(perTrip.getCharges());
                UserDetails userDetails = userdetailsdao.getUserDetailsByUserId(perTrip.getCaptainUserObj().getUserId());
                riderDataList.setCaptainName(userDetails.getFirstName() + " , " + userDetails.getLastName());

                UserDetails userDetailsOfRider = userdetailsdao.getUserDetailsByUserId(perTrip.getTripUserId().getUserId());

                String captainProfilePath = "/UrbanRides/resources/uploads/riderDocuments/riderProfilePics" + perTrip.getTripUserId().getUserId() + "/riderProfile" + perTrip.getTripUserId().getUserId() + userDetailsOfRider.getProfilePhotoExtention();
                riderDataList.setCaptainProfilePath(captainProfilePath);

                //data for general detail
                GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripByTripId(perTrip.getTripId());
                if (generalTripDetails == null) {
                    riderDataList.setStatus(2);
                } else {
                    riderDataList.setCaptainRatting(generalTripDetails.getCaptainRatting());

                    if (generalTripDetails.getIsTripCompleted()) {
                        riderDataList.setStatus(1);
                    } else {
                        riderDataList.setStatus(2);
                    }
                }

                if (perTrip.getServiceType().getServiceTypeId() != 1) {
                    //package trip
                    PackageTrip packageTrip = packageTripDao.getPackageTripDataByTripId(perTrip.getTripId());

                    riderDataList.setPickupDate(formatDateToString(packageTrip.getPickupDate()));
                    riderDataList.setPickupTime(convertTo24HourFormat(packageTrip.getPickupTime()));
                    riderDataList.setNumberOfPassengers(packageTrip.getNumPassengers());
                    riderDataList.setDailyPickUpDays(convertToDayNames(packageTrip.getDailyPickUp()));

                    if (packageTrip.getConcludeNotes() != null && perTrip.getPaymentMethod() != null) {
                        // Completed
                        riderDataList.setStatus(5);
                        riderDataList.setDistance(perTrip.getDistance());
                        riderDataList.setConcludeNotes(packageTrip.getConcludeNotes());
                    } else {
                        // Pending
                        riderDataList.setStatus(6);
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
                    //package trip
                    PackageTrip packageTrip = packageTripDao.getPackageTripDataByTripId(perTrip.getTripId());

                    riderDataList.setPickupDate(formatDateToString(packageTrip.getPickupDate()));
                    riderDataList.setPickupTime(convertTo24HourFormat(packageTrip.getPickupTime()));
                    riderDataList.setNumberOfPassengers(packageTrip.getNumPassengers());
                    riderDataList.setDailyPickUpDays(convertToDayNames(packageTrip.getDailyPickUp()));

                    if (packageTrip.getConcludeNotes() != null && perTrip.getPaymentMethod() != null) {
                        // Completed
                        riderDataList.setStatus(5);
                        riderDataList.setDistance(perTrip.getDistance());
                        riderDataList.setConcludeNotes(packageTrip.getConcludeNotes());
                    } else {
                        // Pending
                        riderDataList.setStatus(3);
                    }
                    riderDataList.setSpecialInstruction(packageTrip.getSpecialInstructions());
                    riderDataList.setEmergencyContact(packageTrip.getEmergencyContact());
                }
            }


            riderMyTripList.add(riderDataList);
        }

        return riderMyTripList;
    }

    public String formatDateToString(LocalDate date) {
        // Define the date format pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the date to string
        String formattedDate = date.format(formatter);

        return formattedDate;
    }

    public static String convertTo24HourFormat(LocalTime time) {
        // Define the output time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Format the LocalTime object into a string in 24-hour format
        return time.format(formatter);
    }

    public static String convertToDayNames(String numOfDays) {
        // Map day numbers to day names
        Map<String, String> dayMap = new HashMap<>();
        dayMap.put("1", "Monday");
        dayMap.put("2", "Tuesday");
        dayMap.put("3", "Wednesday");
        dayMap.put("4", "Thursday");
        dayMap.put("5", "Friday");
        dayMap.put("6", "Saturday");
        dayMap.put("7", "Sunday");

        // Split the input string by commas
        String[] daysArray = numOfDays.split(",");

        // Convert the day numbers to day names
        String dailyPickUpDays = java.util.Arrays.stream(daysArray).map(dayMap::get) // Map each day number to its name
                .filter(day -> day != null) // Filter out null values if any
                .collect(Collectors.joining(", ")); // Join the names with commas

        return dailyPickUpDays;
    }


//    -------------captain package trip data----------------------


    public List<CaptainPackageTripsDataDto> getCaptainPackageTripData() {
        List<CaptainPackageTripsDataDto> riderMyTripList = new ArrayList<>();
        UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");
        List<Trip> tripList = tripDao.getAllPackageTrips(userSessionObj.getUserId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a, d'th' MMMM yyyy");

        for (Trip perTrip : tripList) {
            CaptainPackageTripsDataDto captainPackageTripsDataDto = new CaptainPackageTripsDataDto();
            captainPackageTripsDataDto.setServiceTypeId(perTrip.getServiceType().getServiceTypeId());
            captainPackageTripsDataDto.setVehicleName(perTrip.getVehicleId().getVehicleTypeName());
            captainPackageTripsDataDto.setPickUpLocation(perTrip.getPickupAddress());
            captainPackageTripsDataDto.setDropOffLocation(perTrip.getDropoffAddress());
            captainPackageTripsDataDto.setTripDate(formatLocalDateTime(perTrip.getCreatedDate(), formatter));
            captainPackageTripsDataDto.setTripCode(perTrip.getTripCode());
            captainPackageTripsDataDto.setTripId(perTrip.getTripId());
            captainPackageTripsDataDto.setDistance(perTrip.getDistance());
            captainPackageTripsDataDto.setCharges(perTrip.getCharges());

            UserDetails UserDetails = userdetailsdao.getUserDetailsByUserId(perTrip.getTripUserId().getUserId());

            captainPackageTripsDataDto.setRiderName(UserDetails.getFirstName() + " " + UserDetails.getLastName());


            if (perTrip.getServiceType().getServiceTypeId() != 1) {
                //package trip
                PackageTrip packageTrip = packageTripDao.getPackageTripDataByTripId(perTrip.getTripId());
                if (packageTrip != null) {
                    captainPackageTripsDataDto.setPickupTime(convertTo24HourFormat(packageTrip.getPickupTime()));
                    captainPackageTripsDataDto.setNumberOfPassengers(packageTrip.getNumPassengers());
                    captainPackageTripsDataDto.setNumberOfDays(packageTrip.getNumOfDays());
                    captainPackageTripsDataDto.setDailyPickUpDays(convertToDayNames(packageTrip.getDailyPickUp()));
                    captainPackageTripsDataDto.setSpecialInstruction(packageTrip.getSpecialInstructions());
                    captainPackageTripsDataDto.setEmergencyContact(packageTrip.getEmergencyContact());
                    captainPackageTripsDataDto.setPickupDate(formatDateToString(packageTrip.getPickupDate()));
                }
                LocalDate today = LocalDate.now();

                if (packageTrip != null && !perTrip.isAccepted() && (today.isAfter(packageTrip.getPickupDate()) || today.isEqual(packageTrip.getPickupDate()))) {
                    continue;
                }

            }

            if (perTrip.getServiceType().getServiceTypeId() == 3) {
                captainPackageTripsDataDto.setIsCharges(1);
            } else {
                captainPackageTripsDataDto.setIsCharges(2);
            }
            if (perTrip.getCaptainUserObj() != null && perTrip.getCaptainUserObj().getUserId() == userSessionObj.getUserId() && perTrip.getPaymentMethod() == null) {
                captainPackageTripsDataDto.setIsTripLive(1);
            }

            riderMyTripList.add(captainPackageTripsDataDto);
        }
        return riderMyTripList;
    }


    public ResponseEntity<Map<String, String>> acceptPackageRide(int tripId) {
        Map<String, String> response = new HashMap<>();

        try {
            Trip trip = tripDao.getTipById(tripId);
            if (trip == null) {
                throw new IllegalArgumentException("Trip not found with ID: " + tripId);
            }

            UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");
            if (userSessionObj == null) {
                throw new IllegalStateException("User session not found");
            }
            CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
            if (captainDetails.getVehicleType().getVehicleId() != trip.getVehicleId().getVehicleId()) {
                throw new IllegalStateException("You don't have the vehicle required by rider.");
            }

            User user = usersDao.getUserByUserId(userSessionObj.getUserId());
            if (user == null) {
                throw new IllegalArgumentException("User not found with ID: " + userSessionObj.getUserId());
            }

            trip.setCaptainUserObj(user);
            if (trip.isAccepted()) {
                throw new IllegalArgumentException("The trip is already accepted by another user");
            } else {
                trip.setAccepted(true);
            }

            UserDetails captainUserDetails = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());
            if (captainUserDetails == null) {
                throw new IllegalArgumentException("User details not found for user ID: " + userSessionObj.getUserId());
            }

            NotificationLogs notificationLogs = createNotificationLog(trip, captainUserDetails);

            try {
                this.emailSend.acceptPackageRide(trip.getTripUserId().getEmail(), trip.getServiceType().getServiceType(), trip.getTripCode());
            } catch (Exception e) {
                // Log the error and handle the failure to send email
                System.err.println("Error sending email: " + e.getMessage());
                throw new RuntimeException("Email not sent");
            }
            notificationLogsDao.saveNotificationLog(notificationLogs);
            tripDao.updateTrip(trip);

            response.put("message", "Package ride accepted successfully");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Log the error
            System.err.println("Error: " + e.getMessage());
            throw e;  // Rethrow to handle in the controller method
        } catch (Exception e) {
            // Log the error
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    private NotificationLogs createNotificationLog(Trip trip, UserDetails captainUserDetails) {
        NotificationLogs notificationLogs = new NotificationLogs();
        notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID8"));
        notificationLogs.setNotificationMsg("Your service ride with ID " + trip.getTripCode() + " has been accepted by " + captainUserDetails.getFirstName() + " " + captainUserDetails.getLastName());

        User riderUser = usersDao.getUserByUserId(trip.getTripUserId().getUserId());
        if (riderUser == null) {
            throw new IllegalArgumentException("User not found with ID: " + trip.getTripUserId().getUserId());
        }

        notificationLogs.setUser(riderUser);
        return notificationLogs;
    }


    public ResponseEntity<String> concludeRentTaxi(ConcludeRideRequestRentTaxDto concludeRideRequestRentTaxDto) {
        try {
            // Fetch trip and package trip data
            Trip trip = tripDao.getTipById(concludeRideRequestRentTaxDto.getTripId());
            if (trip == null) {
                throw new IllegalArgumentException("Trip not found with ID: " + concludeRideRequestRentTaxDto.getTripId());
            }


            UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");
            UserDetails captainUser = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());
            CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
            if (captainUser == null || captainDetails == null) {
                throw new IllegalArgumentException("Captain details not found");
            }
            PackageTrip packageTrip = packageTripDao.getPackageTripDataByTripId(concludeRideRequestRentTaxDto.getTripId());
            if (packageTrip == null) {
                throw new IllegalArgumentException("Package trip not found with ID: " + concludeRideRequestRentTaxDto.getTripId());
            }
            LocalDate today = LocalDate.now();
            if (today.isBefore(packageTrip.getDropOffDate())) {
                throw new IllegalArgumentException("You cannot conclude the ride before the last date of the trip.");
            }
            // Update trip details
            packageTrip.setConcludeNotes(concludeRideRequestRentTaxDto.getConclusionNote());
            int chargesAsInt = (int) concludeRideRequestRentTaxDto.getCharges();
            trip.setCharges(chargesAsInt);
            trip.setDistance(concludeRideRequestRentTaxDto.getDistance());
            trip.setPaymentMethod(concludeRideRequestRentTaxDto.getPaymentMethod());

            String distanceStr = concludeRideRequestRentTaxDto.getDistance();
            double distance = Double.parseDouble(distanceStr);
            int backendCharge = calculateChargeByDistance(distance, trip.getVehicleId().getVehicleTypeName());

            // Validate the charge received from the frontend
            int frontendCharge = (int) Math.round(concludeRideRequestRentTaxDto.getCharges());
            if (frontendCharge != backendCharge) {
                throw new IllegalArgumentException("Mismatch between frontend and backend charge calculations.");
            }
            trip.setCharges(trip.getCharges() + concludeRideRequestRentTaxDto.getCharges());
            tripDao.updateTrip(trip);
            captainUser.setSuccessTripCount(captainUser.getSuccessTripCount() + 1);
            userdetailsdao.updateUserDetails(captainUser);
            captainDetails.setTotalEarnings(captainDetails.getTotalEarnings() + trip.getCharges());
            captainDetailsDao.updateCaptainDetail(captainDetails);
            // Prepare notification
            NotificationLogs notificationLogs = new NotificationLogs();
            notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID7"));
            notificationLogs.setNotificationMsg("Your service ride with ID " + trip.getTripCode() + " has been concluded and you have paid " + trip.getCharges());


            User user = usersDao.getUserByUserId(trip.getTripUserId().getUserId());
            if (user == null) {
                throw new IllegalArgumentException("User not found with ID: " + trip.getTripUserId().getUserId());
            }


            notificationLogs.setUser(user);

            // Save notification log
            notificationLogsDao.saveNotificationLog(notificationLogs);

            // Send email
            try {
                this.emailSend.concludeRentTaxiEmail(trip.getTripUserId().getEmail(), trip.getTripCode(), concludeRideRequestRentTaxDto.getConclusionNote());
            } catch (Exception e) {
                // Log error and handle email sending failure
                System.err.println("Error sending email: " + e.getMessage());
                // Optionally, you could save an error status or retry sending email
            }

            // Update package trip details
            packageTripDao.updatePackageTrip(packageTrip);

            return ResponseEntity.ok("Rent taxi concluded successfully");
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Log the error
            System.err.println("Error: " + e.getMessage());
            throw e;  // Rethrow to handle in the controller method
        } catch (Exception e) {
            // Log the error
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred while concluding rent taxi", e);
        }
    }

    public int calculateChargeByDistance(double distance, String vehicleName) {
        int chargeFactor;

        switch (vehicleName.toLowerCase()) {
            case "bike":
                chargeFactor = 2;
                break;
            case "rickshaw":
                chargeFactor = 3;
                break;
            case "car":
                chargeFactor = 6;
                break;
            case "luxury car":
                chargeFactor = 10;
                break;
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicleName);
        }

        return (int) Math.round(distance * chargeFactor);
    }


    public ResponseEntity<String> concludeDailyPickUp(ConcludeRideRequestDailyPickUpDto concludeRideRequestDailyPickUpDto) {
        try {
            // Fetch trip and package trip data
            Trip trip = tripDao.getTipById(concludeRideRequestDailyPickUpDto.getTripId());
            if (trip == null) {
                throw new IllegalArgumentException("Trip not found with ID: " + concludeRideRequestDailyPickUpDto.getTripId());
            }


            UserSessionObj userSessionObj = (UserSessionObj) httpSession.getAttribute("captainSessionObj");
            UserDetails captainUser = userdetailsdao.getUserDetailsByUserId(userSessionObj.getUserId());

            CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
            if (captainUser == null || captainDetails == null) {
                throw new IllegalArgumentException("Captain details not found");
            }
            PackageTrip packageTrip = packageTripDao.getPackageTripDataByTripId(concludeRideRequestDailyPickUpDto.getTripId());
            if (packageTrip == null) {
                throw new IllegalArgumentException("Package trip not found with ID: " + concludeRideRequestDailyPickUpDto.getTripId());
            }
            LocalDate today = LocalDate.now();
            if (today.isBefore(packageTrip.getDropOffDate())) {
                throw new IllegalArgumentException("You cannot conclude the ride before the last date of the trip.");
            }


            // Update trip details
            packageTrip.setConcludeNotes(concludeRideRequestDailyPickUpDto.getConclusionNote());
            trip.setPaymentMethod(concludeRideRequestDailyPickUpDto.getPaymentMethod());
            tripDao.updateTrip(trip);
            captainUser.setSuccessTripCount(captainUser.getSuccessTripCount() + 1);
            userdetailsdao.updateUserDetails(captainUser);
            captainDetails.setTotalEarnings(captainDetails.getTotalEarnings() + trip.getCharges());
            captainDetailsDao.updateCaptainDetail(captainDetails);
            // Prepare notification
            NotificationLogs notificationLogs = new NotificationLogs();
            notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID7"));
            notificationLogs.setNotificationMsg("Your service ride with ID " + trip.getTripCode() + " has been concluded and you have paid " + trip.getCharges());


            User user = usersDao.getUserByUserId(trip.getTripUserId().getUserId());
            if (user == null) {
                throw new IllegalArgumentException("User not found with ID: " + trip.getTripUserId().getUserId());
            }

            notificationLogs.setUser(user);

            // Save notification log
            notificationLogsDao.saveNotificationLog(notificationLogs);

            // Send email
            try {
                this.emailSend.concludeDailyPickup(trip.getTripUserId().getEmail(), trip.getTripCode(), concludeRideRequestDailyPickUpDto.getConclusionNote());
            } catch (Exception e) {
                // Log error and handle email sending failure
                System.err.println("Error sending email: " + e.getMessage());
                throw new RuntimeException("Daily pickup concluded, but failed to send email", e);
            }

            // Update package trip details
            packageTripDao.updatePackageTrip(packageTrip);

            return ResponseEntity.ok("Daily pickup concluded successfully");
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Log the error
            System.err.println("Error: " + e.getMessage());
            throw e;  // Rethrow the exception to be handled by a higher-level handler
        } catch (Exception e) {
            // Log the error
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred while concluding daily pickup", e);
        }
    }


    public SupportRequestDataDto findSupportRequestById(String id) {
        SupportTypeLogs supportTypeLogs = supportTypeLogsDao.getSupportPerData(id);

        SupportRequestDataDto supportRequestDataDto = new SupportRequestDataDto();
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

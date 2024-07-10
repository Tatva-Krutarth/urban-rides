package com.urbanrides.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbanrides.dao.*;
import com.urbanrides.dao.TripDao;
import com.urbanrides.dtos.*;
import com.urbanrides.helper.DateTimeConverter;
import com.urbanrides.helper.NotificationTypeEnum;
import com.urbanrides.helper.OtpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.urbanrides.model.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@EnableScheduling

public class CabBookingService {
    @Autowired
    UsersDao usersDao;
    @Autowired
    UserDetailsDao userDetailsDao;

    @Autowired
    TripDao tripDao;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    VehicleTypeDao vehicleTypeDao;
    @Autowired
    private HttpSession httpSession;

    @Autowired
    DateTimeConverter dateTimeConverter;

    private List<double[]> coordinates = Arrays.asList(
            new double[]{23.033983, 72.509583},
            new double[]{23.038475, 72.511868},
            new double[]{23.049612, 72.517190},
            new double[]{23.058321, 72.520050},
            new double[]{23.076474, 72.525328},
            new double[]{23.080911, 72.526556}
    );
    private AtomicInteger index = new AtomicInteger(0);
    private boolean sendUpdates = false;

    @Autowired
    private UserRegistrationDao userRegistrationDao;

    @Autowired
    private ServiceTypeDao serviceTypeDao;
    @Autowired
    private HttpSession session;
    @Autowired
    private PackageTripDao packageTripDao;
    @Autowired
    private CaptainDetailsDao captainDetailsDao;
    @Autowired
    NotificationLogsDao notificationLogsDao;

    public String generalRide(RiderNormalRideDto riderNormalRideDto) {
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("riderSessionObj");
        User userObj = usersDao.getUserByUserId(userSessionObj.getUserId());
        Trip trip = new Trip();
        trip.setTripCode(generateTripId(userSessionObj.getUserId()));
        trip.setTripUserId(userObj);
        trip.setCharges(riderNormalRideDto.getCharges());
        trip.setPickupAddress(riderNormalRideDto.getPickup());
        trip.setDropoffAddress(riderNormalRideDto.getDropoff());
        trip.setDistance(riderNormalRideDto.getDistance());
        ServiceType serviceType = serviceTypeDao.getServiceType(1);
        trip.setServiceType(serviceType);
        trip.setEstimatedTime(convertMinsToLocalTime(riderNormalRideDto.getEstimatedTime()));
        VehicleType vehicleTypeObj = vehicleTypeDao.getVehicaleId(Integer.parseInt(riderNormalRideDto.getVehicleId()));
        trip.setVehicleId(vehicleTypeObj);
        int id = tripDao.saveGeneralTrip(trip);
        System.out.println(riderNormalRideDto);
        return "Ride Registered " + id;
    }

    OtpGenerator otpGenerator;

    public OtpLogs setOtpLogData(String email) {
        OtpLogs otpLogs = new OtpLogs();
        otpLogs.setEmail(email);
        otpLogs.setOptReqSendTime(LocalTime.now());
//        otpLogs.setAttempt(1);
        otpLogs.setOtpPassed(false);

        otpLogs.setGeneratedOtp(otpGenerator.generateOTP());
        userRegistrationDao.saveUser(otpLogs);

        return otpLogs;
    }


    //this is the captain side service(send captain data to the rider side
    public String acceptRideRiderSide(int tripId) {


//        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("captainSessionObj");
//        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
        User captainUser = usersDao.getUserByUserId(44);
//        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
//        UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
        Trip trip = tripDao.getTipById(tripId);
        trip.setAccepted(true);
        trip.setCaptainUserObj(captainUser);
        tripDao.updateTrip(trip);

//        String folderName = "captain" + user.getUserId();
        String folderName = "captain" + 44;
        String folderPath = httpSession.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "captainDocuments" + File.separator + folderName;
//        String extension = captainDetails.getProfilePhotoExtension();
        String extension = ".png";
        String fileNameProfile = "profilePhoto" + extension;
//        String filePathProfile = folderPath + File.separator + fileNameProfile;

        CaptainInfoDto captainInfoDto = new CaptainInfoDto();
//        captainInfoDto.setCaptainId(userSessionObj.getUserId());
        captainInfoDto.setCaptainId(44);
//        captainInfoDto.setCaptainName(userDetails.getFirstName() + " , " + userDetails.getLastName());
        captainInfoDto.setCaptainName("Jambudo");
//        captainInfoDto.setCaptainContact(userDetails.getPhone());
        captainInfoDto.setCaptainContact("12345678");
        OtpLogs otpLogs = setOtpLogData(trip.getTripUserId().getEmail());


        captainInfoDto.setOtp(otpLogs.getGeneratedOtp());
        captainInfoDto.setPhoto("/UrbanRides/resources/uploads/captainDocuments/" + folderName + "/" + fileNameProfile);
        captainInfoDto.setRatings(1.6);
        captainInfoDto.setTripId(tripId);
        captainInfoDto.setLatitude("23.033983");
        captainInfoDto.setLongitude("72.509583");
//        captainInfoDto.setRiderAddress(trip.getPickupAddress());
        captainInfoDto.setRiderAddress("The Retail Park Rajyash City BRTS Main Rd Central Bopal, Bopal , Ahmedabad, Gujarat 380058, India");
//        captainInfoDto.setVehicleNumber(captainDetails.getNumberPlate());
        captainInfoDto.setVehicleNumber("AA34AS1212");
        ObjectMapper mapper = new ObjectMapper();
        String captainInfoJson;
        try {
            captainInfoJson = mapper.writeValueAsString(captainInfoDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error converting CaptainInfoDto to JSON";
        }

        notifyRider(tripId, "Your ride has been confirmed.", captainInfoJson);

        // Start sending location updates
        sendUpdates = true;

        return "Done";
    }


    //this is the captain side service(send rider data to the captain side

    public RiderInfoDto acceptRideCaptainSide(int tripId) {


//        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("riderSessionObj");
//        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
        User riderUser = usersDao.getUserByUserId(43);
//        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(44);
//        UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
        Trip trip = tripDao.getTipById(tripId);
        trip.setAccepted(true);
        trip.setCaptainUserObj(riderUser);
        tripDao.updateTrip(trip);

//        String folderName = "captain" + user.getUserId();
        String folderName = "riderProfilePics" + 43;
        String folderPath = httpSession.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "riderDocuments" + File.separator + folderName;
//        String extension = captainDetails.getProfilePhotoExtension();
        String extension = ".jpg";
        String fileNameProfile = "riderProfile43" + extension;
//        String filePathProfile = folderPath + File.separator + fileNameProfile;

        RiderInfoDto riderInfoDto = new RiderInfoDto();
//        captainInfoDto.setCaptainId(userSessionObj.getUserId());
        riderInfoDto.setRiderId(43);
//        captainInfoDto.setCaptainName(userDetails.getFirstName() + " , " + userDetails.getLastName());
        riderInfoDto.setRiderName("Jambudo");
//        captainInfoDto.setCaptainContact(userDetails.getPhone());
        riderInfoDto.setRiderContact("12345678");


//        OtpLogs otpLogs = setOtpLogData(trip.getTripUserId().getEmail());


//        riderInfoDto.setOtp(otpLogs.getGeneratedOtp());
        riderInfoDto.setPhotoLocation("/UrbanRides/resources/uploads/riderDocuments/" + folderName + "/" + fileNameProfile);
        riderInfoDto.setTripId(tripId);
        riderInfoDto.setCharges(String.valueOf(trip.getCharges()));
        riderInfoDto.setRiderPickupLocation(trip.getPickupAddress());
        riderInfoDto.setCaptainLocation(captainDetails.getCaptainLocatation());
        riderInfoDto.setRiderDropOffLocation(trip.getDropoffAddress());
//        ObjectMapper mapper = new ObjectMapper();
//        String riderInfoJson;
//        try {
//            riderInfoJson = mapper.writeValueAsString(riderInfoDto);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return "Error converting CaptainInfoDto to JSON";
//        }
//
//        notifyRider(tripId, "You have confirmed the ride.", riderInfoJson);
        return riderInfoDto;
    }

    private void notifyRider(int rideId, String message, String captainInfoJson) {
        System.out.println("Notification fired for ride: " + rideId);
        simpMessagingTemplate.convertAndSend("/topic/rideConfirmed", message + ":" + captainInfoJson);
    }

    @Scheduled(fixedRate = 5000)
    public void sendLocationUpdates() {
        if (!sendUpdates) {
            return;
        }

        int currentIndex = index.getAndUpdate(i -> (i + 1) % coordinates.size());
        double[] currentCoords = coordinates.get(currentIndex);
        CaptainLocationUpdate captainLocationUpdate = new CaptainLocationUpdate(currentCoords[0], currentCoords[1]);

        ObjectMapper mapper = new ObjectMapper();
        String locationUpdateJson;
        try {
            locationUpdateJson = mapper.writeValueAsString(captainLocationUpdate);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }

        simpMessagingTemplate.convertAndSend("/topic/locationUpdates", locationUpdateJson);

        // Check if currentIndex is the last index
        if (currentIndex == coordinates.size() - 1) {
            // Stop sending updates
            sendUpdates = false;
            System.out.println("Reached last index. Stopping location updates.");
        }
    }

    public String captainOtp(int otp, int tripId) {
        Trip trip = tripDao.getTipById(tripId);
        OtpLogs otpLogs = userRegistrationDao.getOtpLogsByEmail(trip.getTripUserId().getEmail());
        if (otpLogs.getGeneratedOtp() != otp) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripByTripId(tripId);
        generalTripDetails.setIsOtpValidated(true);
        generalTripDetailsDao.updateGeneralTripDetails(generalTripDetails);
        // live ride with the rider
        captainReached();
        return "Done";
    }

    private void captainReached() {
        System.out.println("Notification fired for ride: ");
        String locationUpdateJson = "tdssfsfd";
        simpMessagingTemplate.convertAndSend("/topic/captainReached", locationUpdateJson);
    }


    @Scheduled(fixedRate = 5000)
    public void sendDestinationLocation() {
        if (!sendUpdates) {
            return;
        }
        System.out.println("weeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        int currentIndex = index.getAndUpdate(i -> (i + 1) % coordinates.size());
        double[] currentCoords = coordinates.get(currentIndex);
        CaptainLocationUpdate captainLocationUpdate = new CaptainLocationUpdate(currentCoords[0], currentCoords[1]);

        ObjectMapper mapper = new ObjectMapper();
        String locationUpdateJson;
        try {
            locationUpdateJson = mapper.writeValueAsString(captainLocationUpdate);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }

        simpMessagingTemplate.convertAndSend("/topic/bothReachingDestinations", locationUpdateJson);

        // Check if currentIndex is the last index
        if (currentIndex == coordinates.size() - 1) {
            // Stop sending updates
            sendUpdates = false;
            System.out.println("Reached last index. Stopping location updates.");
        }
    }


    public String cancelRide(String cancelReason, int tripId) {

        Trip trip = tripDao.getTipById(tripId);
        trip.setReasonForCancellation(cancelReason);
        tripDao.updateTrip(trip);
        return "done";
    }


    @Autowired
    private GeneralTripDetailsDao generalTripDetailsDao;

    public static LocalTime convertMinsToLocalTime(String estimatedTime) {
        String[] parts = estimatedTime.split(" ");
        int minutes = Integer.parseInt(parts[0]);
        return LocalTime.of(0, minutes);
    }

    public static LocalDateTime convertMinsToLocalDateTime(String estimatedTime) {
        String[] parts = estimatedTime.split(" ");
        int minutes = Integer.parseInt(parts[0]);
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(0, minutes));
    }

    public int saveGeneralTripInfo(RiderReachInfo riderReachInfo) {
        GeneralTripDetails generalTripDetails = new GeneralTripDetails();
        Trip trip = tripDao.getTipById(riderReachInfo.getTripId());
        generalTripDetails.setCaptainAway(riderReachInfo.getCaptainAway());
        generalTripDetails.setCaptainEstimatedReachTime(convertMinsToLocalDateTime(riderReachInfo.getCaptainEstimatedReachTime()));
        generalTripDetails.setTripObj(trip);
        int tripID = generalTripDetailsDao.saveGeneralTripDetails(generalTripDetails);

        return tripID;
    }

    public void saveRiderStartInfo(int tripId) {
        GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripDetailsById(tripId);
        generalTripDetails.setCaptainActualReachTime(LocalDateTime.now());
        generalTripDetails.setIsCaptainReached(true);
        generalTripDetailsDao.updateGeneralTripDetails(generalTripDetails);
    }

    public RattingModalDataDto saveRideEndInfo(int tripId) {

        //        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("captainSessionObj");
//        User user = usersDao.getUserByUserId(userSessionObj.getUserId());
//        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
//        UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());

        GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripDetailsById(tripId);
        generalTripDetails.setTripEndTime(LocalDateTime.now());
        generalTripDetailsDao.updateGeneralTripDetails(generalTripDetails);
        Trip generalTrip = tripDao.getTipById(generalTripDetails.getTripObj().getTripId());
//        String folderName = "captain" + user.getUserId();
        String folderName = "captain" + 44;
        String folderPath = httpSession.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "captainDocuments" + File.separator + folderName;
//      String extension = captainDetails.getProfilePhotoExtension();
        String extension = ".png";
        String fileNameProfile = "profilePhoto" + extension;
//      String filePathProfile = folderPath + File.separator + fileNameProfile;


        UserDetails userDetails = userDetailsDao.getUserDetailsById(32);
        RattingModalDataDto rattingModalDataDto = new RattingModalDataDto();
        rattingModalDataDto.setCharges(generalTrip.getCharges());
        rattingModalDataDto.setProfilePhoto("/UrbanRides/resources/uploads/captainDocuments/" + folderName + "/" + fileNameProfile);
        rattingModalDataDto.setBalance(userDetails.getWallet());
//      captainInfoDto.setCaptainName(userDetails.getFirstName() + " , " + userDetails.getLastName());
        rattingModalDataDto.setCaptainName("Jamdudo de al petron");
        return rattingModalDataDto;
    }


    public void saveRattingInfo(RiderRattingConclude riderRattingConclude) {
        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("riderSessionObj");
        User riderUser = usersDao.getUserByUserId(userSessionObj.getUserId());

        GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripDetailsById(riderRattingConclude.getTripId());
        Trip trip = tripDao.getTipById(generalTripDetails.getTripObj().getTripId());
        UserDetails capUserDetails = userDetailsDao.getUserDetailsByUserId(trip.getCaptainUserObj().getUserId());

        generalTripDetails.setTripEndTime(LocalDateTime.now());
        generalTripDetails.setFeedback(riderRattingConclude.getFeedback());
        generalTripDetails.setIsTripCompleted(true);
        generalTripDetails.setCaptainRatting(riderRattingConclude.getRattings());
        UserDetails userDetails = userDetailsDao.getUserDetailsById(32);
//        GeneralTrip generalTrip = generalTripDao.getTipById(generalTripDetails.getGeneralTripId().getTripId());
        Trip tripp = tripDao.getTipById(generalTripDetails.getTripObj().getTripId());
        BigDecimal tripCharge = BigDecimal.valueOf(tripp.getCharges());
        if ("Pay with cash".equalsIgnoreCase(riderRattingConclude.getPayMethod())) {
            userDetails.setSuccessTripCount(userDetails.getSuccessTripCount() + 1);
            tripp.setPaymentMethod(riderRattingConclude.getPayMethod());
            userDetailsDao.updateUserDetails(userDetails); // Update user details in DAO
            tripDao.updateTrip(tripp);

        } else {
            BigDecimal wallet = userDetails.getWallet();
            BigDecimal updatedWallet = wallet.subtract(tripCharge);
            userDetails.setSuccessTripCount(userDetails.getSuccessTripCount() + 1);
            userDetails.setWallet(updatedWallet);
            userDetailsDao.updateUserDetails(userDetails); // Update user details in DAO
            tripp.setPaymentMethod(riderRattingConclude.getPayMethod());
            tripDao.updateTrip(tripp);
        }


        NotificationLogs notificationLogs = new NotificationLogs();
        notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID3"));
        notificationLogs.setNotificationMsg("You have paid " + trip.getCharges() + " to " + capUserDetails.getFirstName() + " , " + capUserDetails.getLastName() + " with " + riderRattingConclude.getPayMethod());
        notificationLogs.setUser(riderUser);
        notificationLogsDao.saveNotificationLog(notificationLogs);
        generalTripDetailsDao.updateGeneralTripDetails(generalTripDetails);
    }


    public String savePackageTripDetails(PackageServiceDto packageServiceDto) {
        String errormsg;
        if (packageServiceDto.getServiceType().equals("Rent a taxi")) {
            errormsg = savePackageRentATaxi(packageServiceDto);
            System.out.println(errormsg);
        } else if (packageServiceDto.getServiceType().equals("Daily pick up")) {
            errormsg = saveValidateDailyPickup(packageServiceDto);
        } else {
            throw new IllegalArgumentException("Service type is not specified");
        }
        return errormsg;
    }

    public String savePackageRentATaxi(PackageServiceDto packageServiceDto) {
        String errormsg = validateRentATaxi(packageServiceDto);
        return "Not implemented for Rent a taxi";
    }


    public String validateRentATaxi(PackageServiceDto packageServiceDto) {
        String errorInInput = "";
        //session

        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("riderSessionObj");
        User userObj = usersDao.getUserByUserId(userSessionObj.getUserId());
        Trip trip = new Trip();
        trip.setTripCode(generateTripId(userSessionObj.getUserId()));
        trip.setTripUserId(userObj);
        VehicleType vehicleTypeObj = vehicleTypeDao.getVehicaleId(Integer.parseInt(packageServiceDto.getVehicleId()));
        trip.setVehicleId(vehicleTypeObj);
        trip.setPickupAddress(packageServiceDto.getPickup());
        trip.setDropoffAddress(packageServiceDto.getPickup());
        ServiceType serviceType = serviceTypeDao.getServiceType(2);
        trip.setServiceType(serviceType);
        trip.setDistance(packageServiceDto.getDistance());
        trip.setCharges(packageServiceDto.getCharges());
        int tripId = tripDao.saveGeneralTrip(trip);
        trip.setTripId(tripId);
        PackageTrip packageTripe = new PackageTrip();
        packageTripe.setTripId(trip);
        packageTripe.setPickupDate(dateTimeConverter.stringToLocalDate(packageServiceDto.getPickUpDate()));
        packageTripe.setPickupTime(convertStringToLocalTime(packageServiceDto.getPickUpTime()));
        packageTripe.setDropoffTime(convertStringToLocalTime(packageServiceDto.getDropoffTime()));
        packageTripe.setNumPassengers(packageServiceDto.getNumberOfPassengers());
        packageTripe.setEmergencyContact(packageServiceDto.getEmergencyContact());
        packageTripe.setSpecialInstructions(packageServiceDto.getSpecialInstructions());
        packageTripe.setNumOfDays(packageServiceDto.getNumbOfDays());
        packageTripe.setDailyPickUp(packageServiceDto.getDailyPickUp());
        packageTripe.setNumOfDays(packageServiceDto.getNumbOfDays());
        packageTripDao.savePackageTrip(packageTripe);
        return errorInInput;
    }

    private LocalTime convertStringToLocalTime(String timeString) {
        try {
            LocalTime dropoffTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
            System.out.println("Dropoff Time: " + dropoffTime);
            return dropoffTime;

        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format: " + e.getMessage());
            return null;

        }

    }


//    -----------validate daily pickup--------------------------


    private String saveValidateDailyPickup(PackageServiceDto packageServiceDto) {
        String errorInInput = validateDailyPickup(packageServiceDto);

        if (!errorInInput.equals("Done")) {
            throw new IllegalArgumentException(errorInInput);
        }

        saveTripAndPackage(packageServiceDto);

        return errorInInput;
    }


    private String validateDailyPickup(PackageServiceDto packageServiceDto) {
        int numDays = packageServiceDto.getNumbOfDays();
        LocalDate tripStartDate = dateTimeConverter.stringToLocalDate(packageServiceDto.getPickUpDate());
        String dailyPickupDays = packageServiceDto.getDailyPickUp();

        String errorInInput = validateDailyPickup(numDays, tripStartDate, dailyPickupDays);

        if (!errorInInput.equals("Done")) {
            return errorInInput;
        }


        return "Done";
    }

    public static String validateDailyPickup(int numDays, LocalDate tripStartDate, String dailyPickupDays) {
        // Calculate end date by adding numDays to start date
        LocalDate endDate = tripStartDate.plusDays(numDays);

        // Convert daily pickup days string to a list of integers
        List<Integer> pickupDaysList = Arrays.stream(dailyPickupDays.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // Initialize a counter for the number of pickup days within the trip period
        int pickupDaysCount = 0;

        // Iterate through each day of the trip period
        for (LocalDate date = tripStartDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            int dayOfWeek = date.getDayOfWeek().getValue(); // 1 = Monday, 2 = Tuesday,..., 7 = Sunday

            // Check if the current day is a pickup day
            if (pickupDaysList.contains(dayOfWeek)) {
                pickupDaysCount++;
            }
        }

        // Check if the number of pickup days is less than the number of days
        if (pickupDaysCount < numDays) {
            return "Not enough pickup days (" + pickupDaysCount + " out of " + numDays + ")";
        } else {
            return "Done";
        }
    }


    public static String generateTripId(int userId) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);

        UUID randomUUID = UUID.randomUUID();
        String randomPart = randomUUID.toString().substring(0, 6); // First 6 characters of UUID

        String tripUniqueCode = formattedDate + "_" + userId + "_" + randomPart;
        return tripUniqueCode;
    }

    private void saveTripAndPackage(PackageServiceDto packageServiceDto) {
        Trip trip = buildTripObject(packageServiceDto);
        int tripId = tripDao.saveGeneralTrip(trip);

        PackageTrip packageTrip = buildPackageTripObject(packageServiceDto, tripId);
        packageTripDao.savePackageTrip(packageTrip);
    }

    private Trip buildTripObject(PackageServiceDto packageServiceDto) {
        Trip trip = new Trip();

        UserSessionObj userSessionObj = (UserSessionObj) session.getAttribute("riderSessionObj");
        User userObj = usersDao.getUserByUserId(userSessionObj.getUserId());

        trip.setTripCode(generateTripId(userSessionObj.getUserId()));
        trip.setTripUserId(userObj);
        VehicleType vehicleTypeObj = vehicleTypeDao.getVehicaleId(Integer.parseInt(packageServiceDto.getVehicleId()));
        trip.setVehicleId(vehicleTypeObj);
        trip.setPickupAddress(packageServiceDto.getPickup());
        trip.setDropoffAddress(packageServiceDto.getPickup());
        ServiceType serviceType = serviceTypeDao.getServiceType(3);
        trip.setServiceType(serviceType);
        trip.setDistance(packageServiceDto.getDistance());
        trip.setCharges(packageServiceDto.getCharges());
        return trip;
    }

    private PackageTrip buildPackageTripObject(PackageServiceDto packageServiceDto, int tripId) {
        PackageTrip packageTrip = new PackageTrip();

        packageTrip.setTripId(tripDao.getTipById(tripId));
        packageTrip.setPickupDate(dateTimeConverter.stringToLocalDate(packageServiceDto.getPickUpDate()));
        packageTrip.setPickupTime(convertStringToLocalTime(packageServiceDto.getPickUpTime()));
        packageTrip.setDropoffTime(convertStringToLocalTime(packageServiceDto.getDropoffTime()));
        packageTrip.setNumPassengers(packageServiceDto.getNumberOfPassengers());
        packageTrip.setEmergencyContact(packageServiceDto.getEmergencyContact());
        packageTrip.setSpecialInstructions(packageServiceDto.getSpecialInstructions());
        packageTrip.setNumOfDays(packageServiceDto.getNumbOfDays());
        packageTrip.setDailyPickUp(packageServiceDto.getDailyPickUp());

        return packageTrip;
    }

}












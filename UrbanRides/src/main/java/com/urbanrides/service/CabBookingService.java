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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.urbanrides.model.*;

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
import java.util.concurrent.atomic.AtomicInteger;

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

    public String generalRide(RiderNormalRideDto riderNormalRideDto) {
        User userObj = usersDao.getUserByUserId(1);

        Trip trip = new Trip();
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
        otpLogs.setAttempt(1);
        otpLogs.setOtpPassed(false);

        otpLogs.setGeneratedOtp(otpGenerator.generateOTP());
        userRegistrationDao.saveUser(otpLogs);

        return otpLogs;
    }

    public String acceptRide(int tripId) {

        Trip trip = tripDao.getTipById(tripId);
        trip.setAccepted(true);
        tripDao.updateGeneralTrip(trip);

        String folderName = "captain" + 2;
        String folderPath = httpSession.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "captainDocuments" + File.separator + folderName;
        String extension = ".png";
        String fileNameProfile = "profilePhoto" + extension;
        String filePathProfile = folderPath + File.separator + fileNameProfile;

        CaptainInfoDto captainInfoDto = new CaptainInfoDto();
        captainInfoDto.setCaptainId(1);
        captainInfoDto.setCaptainName("Jambudo");
        captainInfoDto.setCaptainContact("12345678");
        OtpLogs otpLogs = setOtpLogData(trip.getTripUserId().getEmail());


        captainInfoDto.setOtp(otpLogs.getGeneratedOtp());
        captainInfoDto.setPhoto("/UrbanRides/resources/uploads/captainDocuments/" + folderName + "/" + fileNameProfile);
        captainInfoDto.setRatings(1.6);
        captainInfoDto.setGeneralTripId(3);
        captainInfoDto.setLatitude("23.033983");
        captainInfoDto.setLongitude("72.509583");
        captainInfoDto.setRiderAddress("The Retail Park Rajyash City BRTS Main Rd Central Bopal, Bopal , Ahmedabad, Gujarat 380058, India");

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

    public String captainOtp(int otp) {
        //varify the otp

        //live ride with the rider
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
        tripDao.updateGeneralTrip(trip);
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
        generalTripDetails.setGeneralTripId(trip);
        int tripID = generalTripDetailsDao.saveGeneralTripDetails(generalTripDetails);

        return tripID;
    }

    public void saveRiderStartInfo(int tripId) {
        GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripDetails(tripId);
        generalTripDetails.setCaptainActualReachTime(LocalDateTime.now());
        generalTripDetails.setIsCaptainReached(true);
        generalTripDetailsDao.saveGeneralTripDetails(generalTripDetails);
    }

    public RattingModalDataDto saveRideEndInfo(int tripId) {
        GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripDetails(tripId);
        generalTripDetails.setTripEndTime(LocalDateTime.now());
        generalTripDetailsDao.saveGeneralTripDetails(generalTripDetails);
        Trip generalTrip = tripDao.getTipById(generalTripDetails.getGeneralTripId().getTripId());


        String folderName = "captain" + 2;
        String extension = ".png";
        String fileNameProfile = "profilePhoto" + extension;


//get the id from session
        UserDetails userDetails = userDetailsDao.getUserDetailsById(1);
        RattingModalDataDto rattingModalDataDto = new RattingModalDataDto();
        rattingModalDataDto.setCharges(generalTrip.getCharges());
        rattingModalDataDto.setProfilePhoto("/UrbanRides/resources/uploads/captainDocuments/" + folderName + "/" + fileNameProfile);
        rattingModalDataDto.setBalance(userDetails.getWallet());
        //get captian details latter
        rattingModalDataDto.setCaptainName("Jamdudo de al petron");
        return rattingModalDataDto;
    }


    public void saveRattingInfo(RiderRattingConclude riderRattingConclude) {
        GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripDetails(riderRattingConclude.getTripId());
        generalTripDetails.setTripEndTime(LocalDateTime.now());
        generalTripDetails.setFeedback(riderRattingConclude.getFeedback());
        UserDetails userDetails = userDetailsDao.getUserDetailsById(1);
//        GeneralTrip generalTrip = generalTripDao.getTipById(generalTripDetails.getGeneralTripId().getTripId());
        Trip tripp = tripDao.getTipById(1);
        BigDecimal tripCharge = BigDecimal.valueOf(tripp.getCharges());
        if ("Pay with cash".equals(riderRattingConclude.getPayMethod())) {

            System.out.println("paid with cash");
        } else {
            BigDecimal wallet = userDetails.getWallet();
            BigDecimal updatedWallet = wallet.subtract(tripCharge);
            userDetails.setWallet(updatedWallet);
            userDetailsDao.updateUserDetails(userDetails); // Update user details in DAO
            tripp.setPaymentMethod(riderRattingConclude.getPayMethod());
            tripDao.updateGeneralTrip(tripp);
        }

        //session user
        User riderUser = usersDao.getUserByUserId(1);
        Trip trip = tripDao.getTipById(generalTripDetails.getGeneralTripId().getTripId());
        UserDetails capUserDetails = userDetailsDao.getUserDetailsById(trip.getCaptainUserObj().getUserId());

        NotificationLogs notificationLogs = new NotificationLogs();
        notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID3"));
        notificationLogs.setNotificationMsg("You have paid " + trip.getCharges() + " to " + capUserDetails.getFirstName() + " , " + capUserDetails.getLastName() + " with " + riderRattingConclude.getPayMethod());
        notificationLogs.setUser(riderUser);
        generalTripDetailsDao.saveGeneralTripDetails(generalTripDetails);
    }

    @Autowired
    private PackageTripDao packageTripDao;


    public String savePackageTripDetails(PackageServiceDto packageServiceDto) {
        String errormsg;
        if (packageServiceDto.getServiceType().equals("Rent a taxi")) {
            errormsg = savePackageRentATaxi(packageServiceDto);
            System.out.println(errormsg);
        } else if (packageServiceDto.getServiceType().equals("Package service")) {
            errormsg = saveLugguageService(packageServiceDto);
            System.out.println(errormsg);
        } else if (packageServiceDto.getServiceType().equals("Daily pick up")) {
            errormsg = saveValidateDailyPickup(packageServiceDto);
            System.out.println(errormsg);
        } else {
            errormsg = "Service type is not specified";
        }
        return errormsg;
    }

    public String savePackageRentATaxi(PackageServiceDto packageServiceDto) {
        String errormsg = validateRentATaxi(packageServiceDto);
        return errormsg;
    }

    @Autowired
    DateTimeConverter dateTimeConverter;

    public String validateRentATaxi(PackageServiceDto packageServiceDto) {
        String errorInInput = "";
        //session
        User userObj = usersDao.getUserByUserId(1);
        Trip trip = new Trip();
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

    //    -----------------------------for lugguage service------------------

    public String saveLugguageService(PackageServiceDto packageServiceDto) {
        String errormsg = validateLugguageService(packageServiceDto);
        return errormsg;
    }

    public String validateLugguageService(PackageServiceDto packageServiceDto) {
        String errorInInput = "";
        User userObj = usersDao.getUserByUserId(1);
        Trip trip = new Trip();
        trip.setTripUserId(userObj);
        VehicleType vehicleTypeObj = vehicleTypeDao.getVehicaleId(Integer.parseInt(packageServiceDto.getVehicleId()));
        trip.setVehicleId(vehicleTypeObj);
        trip.setPickupAddress(packageServiceDto.getPickup());
        trip.setDropoffAddress(packageServiceDto.getDropOff());
        ServiceType serviceType = serviceTypeDao.getServiceType(3);
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
        packageTripe.setNumOfDays(1);
        packageTripe.setDailyPickUp(packageServiceDto.getDailyPickUp());
        packageTripe.setNumOfDays(packageServiceDto.getNumbOfDays());
        packageTripDao.savePackageTrip(packageTripe);
        return errorInInput;
    }

//    -----------validate lugguage servide3--------------------------


    public String saveValidateDailyPickup(PackageServiceDto packageServiceDto) {
        String errormsg = validateDailyPickup(packageServiceDto);
        return errormsg;
    }

    public String validateDailyPickup(PackageServiceDto packageServiceDto) {
        String errorInInput = "";
        User userObj = usersDao.getUserByUserId(1);
        Trip trip = new Trip();
        trip.setTripUserId(userObj);
        VehicleType vehicleTypeObj = vehicleTypeDao.getVehicaleId(Integer.parseInt(packageServiceDto.getVehicleId()));
        trip.setVehicleId(vehicleTypeObj);
        trip.setPickupAddress(packageServiceDto.getPickup());
        trip.setDropoffAddress(packageServiceDto.getPickup());
        ServiceType serviceType = serviceTypeDao.getServiceType(4);
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
}












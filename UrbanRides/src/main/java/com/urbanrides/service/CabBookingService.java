package com.urbanrides.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbanrides.dao.*;
import com.urbanrides.dao.TripDao;
import com.urbanrides.dtos.*;
import com.urbanrides.enums.NotificationTypeEnum;
import com.urbanrides.exceptions.CustomExceptions;
import com.urbanrides.exceptions.InsufficientFundsException;
import com.urbanrides.helper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
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
import java.time.temporal.ChronoUnit;
import java.util.*;
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
    EmailSend emailSend;

    @Autowired
    DateTimeConverter dateTimeConverter;

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


    @Autowired
    private GeneralTripDetailsDao generalTripDetailsDao;

    @Autowired
    private EmailConfig emailConfig;

    OtpGenerator otpGenerator;

    public String generalRide(RiderNormalRideDto riderNormalRideDto) throws CustomExceptions {
        try {
            UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
            User userObj = usersDao.getUserByUserId(userSessionObj.getUserId());
            Trip trip = new Trip();
            trip.setTripCode(generateTripId(userSessionObj.getUserId()));
            trip.setTripUserId(userObj);
            double distance;
            try {
                String distanceStr = riderNormalRideDto.getDistance().replaceAll("[^\\d.]", "").trim();
                distance = Double.parseDouble(distanceStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid distance format: " + riderNormalRideDto.getDistance());
            }
            double calculatedCharges = calculateCharges(distance, Integer.parseInt(riderNormalRideDto.getVehicleId()));
            if (Double.compare(calculatedCharges, riderNormalRideDto.getCharges()) != 0) {
                throw new IllegalArgumentException("Charges are invalid. Expected: " + calculatedCharges + ", Provided: " + riderNormalRideDto.getCharges());
            }
            trip.setCharges(riderNormalRideDto.getCharges());
            trip.setPickupAddress(riderNormalRideDto.getPickup());
            trip.setDropoffAddress(riderNormalRideDto.getDropoff());
            trip.setDistance(riderNormalRideDto.getDistance());
            ServiceType serviceType = serviceTypeDao.getServiceType(1);
            trip.setServiceType(serviceType);
            trip.setEstimatedTime(dateTimeConverter.convertMinsToLocalTime(riderNormalRideDto.getEstimatedTime()));
            VehicleType vehicleTypeObj = vehicleTypeDao.getVehicaleId(Integer.parseInt(riderNormalRideDto.getVehicleId()));
            trip.setVehicleId(vehicleTypeObj);
            int id = tripDao.saveGeneralTrip(trip);
            return "Ride Registered " + id;
        } catch (IllegalArgumentException e) {
            throw new CustomExceptions("Validation error: " + e.getMessage());
        } catch (Exception e) {
            throw new CustomExceptions("An error occurred while registering the ride", e);
        }
    }

    private double calculateCharges(double distanceInKm, int vehicleType) {
        int distanceInNumber = (int) Math.round(distanceInKm);
        double[] multipliers = {5.0, 6.0, 8.0, 10.0};
        double timeWeightage = getTimeWeightage();
        if (vehicleType < 1 || vehicleType > multipliers.length) {
            throw new IllegalArgumentException("Invalid vehicle type");
        }

        return Math.round(distanceInNumber * multipliers[vehicleType - 1] * timeWeightage);
    }

    private double getTimeWeightage() {
        int currentHour = LocalDateTime.now().getHour();
        if (currentHour >= 19 || currentHour < 8) {
            return 3.0;
        } else if (currentHour >= 8 && currentHour < 14) {
            return 2.0;
        } else if (currentHour >= 14 && currentHour < 17) {
            return 2.5;
        } else if (currentHour >= 17 && currentHour < 19) {
            return 2.0;
        } else {
            return 1.0; // Default weightage
        }
    }

    public OtpLogs setOtpLogData(String email) {
        OtpLogs otpLogs = new OtpLogs();
        otpLogs.setEmail(email);
        otpLogs.setOptReqSendTime(LocalTime.now());
        otpLogs.setGeneratedOtp(otpGenerator.generateOTP());
        userRegistrationDao.saveUser(otpLogs);
        return otpLogs;
    }


    public List<RiderCaptainDetailsOnMapDto> getFreeCaptain() {
        List<RiderCaptainDetailsOnMapDto> riderCaptainDetailsOnMapDtoList = new ArrayList<>();
        List<CaptainDetails> captainDetailsList = captainDetailsDao.getCaptainDataList();
        for (CaptainDetails capPerDetails : captainDetailsList) {
            RiderCaptainDetailsOnMapDto dto = new RiderCaptainDetailsOnMapDto();
            UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(capPerDetails.getUser().getUserId());
            dto.setCaptainLocation(capPerDetails.getCaptainLocatation());
            dto.setCaptainName(userDetails.getFirstName() + " " + userDetails.getLastName());
            riderCaptainDetailsOnMapDtoList.add(dto);
        }
        return riderCaptainDetailsOnMapDtoList;
    }

    public String acceptRideRiderSide(int tripId) {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("captainSessionObj");
        User captainUser = usersDao.getUserByUserId(userSessionObj.getUserId());
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
        UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
        Trip trip = tripDao.getTipById(tripId);
        trip.setAccepted(true);
        trip.setCaptainUserObj(captainUser);
        tripDao.updateTrip(trip);
        String folderName = "captain" + captainUser.getUserId();
        String folderPath = httpSession.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "captainDocuments" + File.separator + folderName;
        String extension = captainDetails.getProfilePhotoExtension();
        String fileNameProfile = "profilePhoto" + extension;
        CaptainInfoDto captainInfoDto = new CaptainInfoDto();
        captainInfoDto.setCaptainId(userSessionObj.getUserId());
        captainInfoDto.setCaptainName(userDetails.getFirstName() + " , " + userDetails.getLastName());
        captainInfoDto.setCaptainContact(userDetails.getPhone());
        OtpLogs otpLogs = setOtpLogData(trip.getTripUserId().getEmail());
        captainInfoDto.setOtp(otpLogs.getGeneratedOtp());
        captainInfoDto.setPhoto("/UrbanRides/resources/uploads/captainDocuments/" + folderName + "/" + fileNameProfile);
        captainInfoDto.setRatings(captainDetails.getRating());
        captainInfoDto.setTripId(tripId);
        captainInfoDto.setRiderAddress(captainDetails.getCaptainLocatation());
        captainInfoDto.setVehicleNumber(captainDetails.getNumberPlate());
        ObjectMapper mapper = new ObjectMapper();
        String captainInfoJson;
        try {
            captainInfoJson = mapper.writeValueAsString(captainInfoDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error converting CaptainInfoDto to JSON";
        }
        NotificationLogs notificationLogs = new NotificationLogs();
        notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID2"));
        notificationLogs.setNotificationMsg("Captain " + userDetails.getFirstName() + " , " + userDetails.getLastName() + " is on the way , comming from " + captainDetails.getCaptainLocatation());
        User user = usersDao.getUserByUserId(trip.getTripUserId().getUserId());
        notificationLogs.setUser(user);
        sendNotiToRider(notificationLogs.getNotificationMsg());
        notificationLogsDao.saveNotificationLog(notificationLogs);
        notifyRider(tripId, "Your ride has been confirmed.", captainInfoJson);
        return "Done";
    }


    public void sendNotiToRider(String notiMsg) {
        Map<String, String> notification = new HashMap<>();
        notification.put("message", notiMsg);
        simpMessagingTemplate.convertAndSend("/topic/rider-incoming-notifications", notification);
    }

    public RiderInfoDto acceptRideCaptainSide(int tripId) throws CustomExceptions {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("captainSessionObj");
        Trip trip = tripDao.getTipById(tripId);
        if (trip.getCaptainUserObj() != null || trip.getReasonForCancellation() != null || trip.isAccepted()) {
            throw new CustomExceptions("The trip has already been accepted by another captain or it has been cancelled by the rider.");
        }
        User riderUser = usersDao.getUserByUserId(trip.getTripUserId().getUserId());
        UserDetails riderUserDetails = userDetailsDao.getUserDetailsByUserId(trip.getTripUserId().getUserId());
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
        trip.setAccepted(true);
        trip.setCaptainUserObj(riderUser);
        tripDao.updateTrip(trip);
        String folderName = "riderProfilePics" + riderUser.getUserId();
        String folderPath = httpSession.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "riderDocuments" + File.separator + folderName;
        String extension = riderUserDetails.getProfilePhotoExtention();
        String fileNameProfile = "riderProfile" + riderUser.getUserId() + extension;
        RiderInfoDto riderInfoDto = new RiderInfoDto();
        riderInfoDto.setRiderId(userSessionObj.getUserId());
        riderInfoDto.setRiderName(riderUserDetails.getFirstName() + " , " + riderUserDetails.getLastName());
        riderInfoDto.setRiderContact(riderUserDetails.getPhone());
        if (riderUserDetails.getProfilePhotoExtention() != null) {
            riderInfoDto.setPhotoLocation("/UrbanRides/resources/uploads/riderDocuments/" + folderName + "/" + fileNameProfile);
        }
        riderInfoDto.setTripId(tripId);
        riderInfoDto.setCharges(String.valueOf(trip.getCharges()));
        riderInfoDto.setRiderPickupLocation(trip.getPickupAddress());
        riderInfoDto.setCaptainLocation(captainDetails.getCaptainLocatation());
        riderInfoDto.setRiderDropOffLocation(trip.getDropoffAddress());
        return riderInfoDto;
    }

    private void notifyRider(int rideId, String message, String captainInfoJson) {
        simpMessagingTemplate.convertAndSend("/topic/rideConfirmed", message + ":" + captainInfoJson);
    }

    public void captainOtp(int otp, int tripId) {
        Trip trip = tripDao.getTipById(tripId);
        OtpLogs otpLogs = userRegistrationDao.getOtpLogsByEmail(trip.getTripUserId().getEmail());
        if (otpLogs.getGeneratedOtp() != otp) {
            throw new IllegalArgumentException("Invalid OTP");
        }
        GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripByTripId(tripId);
        generalTripDetails.setIsOtpValidated(true);
        generalTripDetailsDao.updateGeneralTripDetails(generalTripDetails);
        captainReached();
    }

    private void captainReached() {
        String locationUpdateJson = "connected via websocket";
        simpMessagingTemplate.convertAndSend("/topic/captain-reached", locationUpdateJson);
    }

    public String cancelRide(String cancelReason, int tripId) {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
        UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
        userDetails.setFailTripCount(userDetails.getFailTripCount() + 1);
        userDetailsDao.updateUserDetails(userDetails);
        Trip trip = tripDao.getTipById(tripId);
        trip.setReasonForCancellation(cancelReason);
        tripDao.updateTrip(trip);
        return "done";
    }

    public static LocalDateTime convertMinsToLocalDateTime(String estimatedTime) {
        String[] parts = estimatedTime.split(" ");
        int minutes = Integer.parseInt(parts[0]);
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(0, minutes));
    }

    public int saveGeneralTripInfo(RiderReachInfoDto riderReachInfo) {
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
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
        UserDetails riderUserDetails = userDetailsDao.getUserDetailsByUserId(userSessionObj.getUserId());
        GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripDetailsById(tripId);
        generalTripDetails.setTripStartTime(LocalDateTime.now());
        generalTripDetailsDao.updateGeneralTripDetails(generalTripDetails);
        Trip trip = tripDao.getTipById(generalTripDetails.getTripObj().getTripId());
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(trip.getCaptainUserObj().getUserId());
        UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(trip.getCaptainUserObj().getUserId());
        String folderName = "captain" + captainDetails.getUser().getUserId();
        String folderPath = httpSession.getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "resources" + File.separator + "uploads" + File.separator + "captainDocuments" + File.separator + folderName;
        String extension = captainDetails.getProfilePhotoExtension();
        String fileNameProfile = "profilePhoto" + extension;
        RattingModalDataDto rattingModalDataDto = new RattingModalDataDto();
        rattingModalDataDto.setCharges(trip.getCharges());
        rattingModalDataDto.setProfilePhoto("/UrbanRides/resources/uploads/captainDocuments/" + folderName + "/" + fileNameProfile);
        rattingModalDataDto.setBalance(riderUserDetails.getWallet());
        rattingModalDataDto.setCaptainName(userDetails.getFirstName() + " , " + userDetails.getLastName());
        return rattingModalDataDto;
    }

    public void saveRattingInfo(RiderRattingConcludeDto riderRattingConclude) {
        GeneralTripDetails generalTripDetails = generalTripDetailsDao.getGeneralTripDetailsById(riderRattingConclude.getTripId());
        Trip trip = tripDao.getTipById(generalTripDetails.getTripObj().getTripId());
        UserDetails captainUserDetails = userDetailsDao.getUserDetailsByUserId(trip.getCaptainUserObj().getUserId());
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(trip.getCaptainUserObj().getUserId());
        UserDetails riderUserDetails = userDetailsDao.getUserDetailsByUserId(trip.getTripUserId().getUserId());
        User riderUser = usersDao.getUserByUserId(trip.getTripUserId().getUserId());
        BigDecimal chargesInInt = riderUserDetails.getWallet();
        int chargesAsInt = chargesInInt.intValue();
        if ("Pay with wallet".equalsIgnoreCase(riderRattingConclude.getPayMethod()) && trip.getCharges() > chargesAsInt) {
            throw new InsufficientFundsException("Insufficient funds in wallet.");
        }
        generalTripDetails.setTripEndTime(LocalDateTime.now());
        generalTripDetails.setFeedback(riderRattingConclude.getFeedback());
        generalTripDetails.setIsTripCompleted(true);
        generalTripDetails.setCaptainRating(riderRattingConclude.getRattings());
        BigDecimal tripCharge = BigDecimal.valueOf(trip.getCharges());
        float earnings = tripCharge.floatValue();
        captainDetails.setTotalEarnings(captainDetails.getTotalEarnings() + earnings);
        updateCaptainRating(captainDetails, riderRattingConclude, captainUserDetails);
        captainUserDetails.setSuccessTripCount(captainUserDetails.getSuccessTripCount() + 1);
        trip.setPaymentMethod(riderRattingConclude.getPayMethod());
        if ("Pay with wallet".equalsIgnoreCase(riderRattingConclude.getPayMethod())) {
            BigDecimal walletRider = riderUserDetails.getWallet();
            BigDecimal walletCaptain = captainUserDetails.getWallet();
            BigDecimal updateRiderdWallet = walletRider.subtract(tripCharge);
            BigDecimal updateCaptaindWallet = walletCaptain.add(tripCharge);
            riderUserDetails.setWallet(updateRiderdWallet);
            captainUserDetails.setWallet(updateCaptaindWallet);
        }
        userDetailsDao.updateUserDetails(captainUserDetails);
        riderUserDetails.setSuccessTripCount(riderUserDetails.getSuccessTripCount() + 1);
        tripDao.updateTrip(trip);
        userDetailsDao.updateUserDetails(riderUserDetails);
        captainDetailsDao.updateCaptainDetail(captainDetails);
        NotificationLogs notificationLogs = new NotificationLogs();
        notificationLogs.setNotificationType(NotificationTypeEnum.getValueById("ID3"));
        notificationLogs.setNotificationMsg("You have paid " + trip.getCharges() + " Rs to " + captainUserDetails.getFirstName() + " " + captainUserDetails.getLastName() + " with " + riderRattingConclude.getPayMethod());
        notificationLogs.setUser(riderUser);
        sendNotiToRider(notificationLogs.getNotificationMsg());
        notificationLogsDao.saveNotificationLog(notificationLogs);
        generalTripDetailsDao.updateGeneralTripDetails(generalTripDetails);
    }

    public void updateCaptainRating(CaptainDetails captainDetails, RiderRattingConcludeDto riderRattingConclude, UserDetails captainUserDetails) {
        float currentRating = captainDetails.getRating();
        float newRating = riderRattingConclude.getRattings();
        int successTripCount = captainUserDetails.getSuccessTripCount();
        int failTripCount = captainUserDetails.getFailTripCount();
        int totalTrips = successTripCount + failTripCount + 1;
        if (totalTrips == 0) {
            captainDetails.setRating(0.0f);
        } else {
            float updatedRating = (newRating + currentRating * successTripCount) / totalTrips;
            captainDetails.setRating(updatedRating);
        }
    }

    public void savePackageTripDetails(PackageServiceDto packageServiceDto) {
        double calculatedCharges = calculateCharges(packageServiceDto);
        calculatedCharges = Math.ceil(calculatedCharges);
        if (Double.compare(calculatedCharges, packageServiceDto.getCharges()) != 0) {
            throw new IllegalArgumentException("Charges are invalid. Expected: " + calculatedCharges + ", Provided: " + packageServiceDto.getCharges());
        }
        if (packageServiceDto.getServiceType().equals("Rent a taxi")) {
            savePackageRentATaxi(packageServiceDto);
        } else if (packageServiceDto.getServiceType().equals("Daily pick up")) {
            saveValidateDailyPickup(packageServiceDto);
        } else {
            throw new IllegalArgumentException("Service type is not specified");
        }
    }
    public List<CaptainAllTripsDataDto> getTripsData() {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) httpSession.getAttribute("captainSessionObj");
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
        List<Trip> trips = tripDao.getAllTripOfCaptainDashboard(captainDetails.getVehicleType().getVehicleId());
        List<CaptainAllTripsDataDto> captainDataList = new ArrayList<>();
        if (trips != null) {
            for (Trip trip : trips) {
                UserDetails userDetails = userDetailsDao.getUserDetailsByUserId(trip.getTripUserId().getUserId());
                CaptainAllTripsDataDto captainData = new CaptainAllTripsDataDto();
                captainData.setTripId(trip.getTripId());
                captainData.setPassengerName(userDetails.getFirstName() + " , " + userDetails.getLastName());
                captainData.setPickUpLocation(trip.getPickupAddress());
                captainData.setCharges(trip.getCharges());
                captainData.setDropLocation(trip.getDropoffAddress());
                captainDataList.add(captainData);
            }
        }
        return captainDataList;
    }

    private double calculateCharges(PackageServiceDto packageServiceDto) {
        String serviceType = packageServiceDto.getServiceType();
        int numPassengers = packageServiceDto.getNumberOfPassengers();
        int vehicleType = Integer.parseInt(packageServiceDto.getVehicleId());
        String pickupDate = packageServiceDto.getPickUpDate();
        String dropOffDate = packageServiceDto.getDropOffDate();
        double charges = 0;
        if (serviceType.equals("Rent a taxi")) {
            int numDays = (int) ChronoUnit.DAYS.between(LocalDate.parse(pickupDate), LocalDate.parse(dropOffDate));
            double baseRate = getBaseRate(vehicleType);
            charges = baseRate * numPassengers * numDays;
        } else if (serviceType.equals("Daily pick up")) {
            double distance = Double.parseDouble(packageServiceDto.getDistance());
            Set<Integer> selectedDays = Arrays.stream(packageServiceDto.getDailyPickUp().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
            int numOfDays = calculateNumberOfDays(LocalDate.parse(pickupDate), LocalDate.parse(dropOffDate), selectedDays);
            double baseRatePerKm = getBaseRatePerKm(vehicleType);
            double ratePerPassenger = getRatePerPassenger(vehicleType);
            charges = (baseRatePerKm * distance + ratePerPassenger * numPassengers) * numOfDays;
        }
        return charges;
    }

    private double getBaseRate(int vehicleType) {
        switch (vehicleType) {
            case 1:
                return 100; // Bike
            case 2:
                return 200; // Rickshaw
            case 3:
                return 300; // Car
            case 4:
                return 400; // Luxury Car
            default:
                throw new IllegalArgumentException("Invalid vehicle type");
        }
    }

    private double getBaseRatePerKm(int vehicleType) {
        switch (vehicleType) {
            case 1:
                return 5; // Bike
            case 2:
                return 10; // Rickshaw
            case 3:
                return 15; // Car
            case 4:
                return 20; // Luxury Car
            default:
                throw new IllegalArgumentException("Invalid vehicle type");
        }
    }

    private double getRatePerPassenger(int vehicleType) {
        switch (vehicleType) {
            case 1:
                return 2; // Bike
            case 2:
                return 5; // Rickshaw
            case 3:
                return 10; // Car
            case 4:
                return 15; // Luxury Car
            default:
                throw new IllegalArgumentException("Invalid vehicle type");
        }
    }

    private int calculateNumberOfDays(LocalDate pickupDate, LocalDate dropOffDate, Set<Integer> selectedDays) {
        int daysCount = 0;
        LocalDate currentDate = pickupDate;
        while (!currentDate.isAfter(dropOffDate)) {
            int dayOfWeek = currentDate.getDayOfWeek().getValue();
            if (selectedDays.contains(dayOfWeek)) {
                daysCount++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return daysCount;
    }

    private void saveValidateDailyPickup(PackageServiceDto packageServiceDto) {
        Trip trip = buildTripObject(packageServiceDto);
        int tripId = tripDao.saveGeneralTrip(trip);
        PackageTrip packageTrip = buildPackageTripObject(packageServiceDto, tripId);
        packageTripDao.savePackageTrip(packageTrip);
    }

    private PackageTrip buildPackageTripObject(PackageServiceDto packageServiceDto, int tripId) {
        PackageTrip packageTrip = new PackageTrip();
        Trip trip = tripDao.getTipById(tripId);
        packageTrip.setTripId(trip);
        try {
            packageTrip.setPickupDate(dateTimeConverter.stringToLocalDate(packageServiceDto.getPickUpDate()));
            packageTrip.setDropOffDate(dateTimeConverter.stringToLocalDate(packageServiceDto.getDropOffDate()));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
        try {
            packageTrip.setPickupTime(dateTimeConverter.convertStringToLocalTime(packageServiceDto.getPickUpTime()));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format", e);
        }
        packageTrip.setNumPassengers(packageServiceDto.getNumberOfPassengers());
        packageTrip.setEmergencyContact(packageServiceDto.getEmergencyContact());
        packageTrip.setSpecialInstructions(packageServiceDto.getSpecialInstructions());
        packageTrip.setDailyPickUp(packageServiceDto.getDailyPickUp());
        try {
            packageTrip.setNumOfDays(calculateNumberOfDays(packageTrip.getPickupDate(), packageTrip.getDropOffDate(), packageServiceDto.getDailyPickUp()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error calculating number of days", e);
        }
        User user = usersDao.getUserByUserId(trip.getTripUserId().getUserId());
        try {
            emailSend.notifyRiderAboutTrip(packageServiceDto, user.getEmail(), trip.getTripCode());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while sending the mail", e);
        }
        return packageTrip;
    }


    private int calculateNumberOfDays(LocalDate pickupDate, LocalDate dropOffDate, String dailyPickUp) {
        if (pickupDate == null || dropOffDate == null || dailyPickUp == null || dailyPickUp.isEmpty()) {
            throw new IllegalArgumentException("Invalid input for date calculation");
        }
        Set<Integer> pickupDays = Arrays.stream(dailyPickUp.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
        int daysCount = 0;
        LocalDate currentDate = pickupDate;
        while (!currentDate.isAfter(dropOffDate)) {
            int dayOfWeek = currentDate.getDayOfWeek().getValue();
            if (pickupDays.contains(dayOfWeek)) {
                daysCount++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return daysCount;
    }

    public void savePackageRentATaxi(PackageServiceDto packageServiceDto) {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
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
        trip.setCharges(packageServiceDto.getCharges());
        int tripId = tripDao.saveGeneralTrip(trip);
        trip.setTripId(tripId);
        PackageTrip packageTrip = new PackageTrip();
        packageTrip.setTripId(trip);
        try {
            packageTrip.setPickupDate(dateTimeConverter.stringToLocalDate(packageServiceDto.getPickUpDate()));
            packageTrip.setDropOffDate(dateTimeConverter.stringToLocalDate(packageServiceDto.getDropOffDate()));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
        try {
            packageTrip.setPickupTime(dateTimeConverter.convertStringToLocalTime(packageServiceDto.getPickUpTime()));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format", e);
        }
        packageTrip.setNumOfDays(calculateNumberOfDays(packageTrip.getPickupDate(), packageTrip.getDropOffDate()));
        packageTrip.setNumPassengers(packageServiceDto.getNumberOfPassengers());
        packageTrip.setEmergencyContact(packageServiceDto.getEmergencyContact());
        packageTrip.setSpecialInstructions(packageServiceDto.getSpecialInstructions());
        packageTrip.setDailyPickUp(packageServiceDto.getDailyPickUp());
        packageTrip.setDailyPickUp(packageServiceDto.getDailyPickUp());
        packageTripDao.savePackageTrip(packageTrip);
        User user = usersDao.getUserByUserId(trip.getTripUserId().getUserId());
        try {
            emailSend.notifyRiderAboutTrip(packageServiceDto, user.getEmail(), trip.getTripCode());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while sending the mail", e);
        }
    }

    private int calculateNumberOfDays(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Invalid input for date calculation");
        }
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static String generateTripId(int userId) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);
        UUID randomUUID = UUID.randomUUID();
        String randomPart = randomUUID.toString().substring(0, 6);
        String tripUniqueCode = formattedDate + "_" + userId + "_" + randomPart;
        return tripUniqueCode;
    }

    private Trip buildTripObject(PackageServiceDto packageServiceDto) {
        Trip trip = new Trip();
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("riderSessionObj");
        User userObj = usersDao.getUserByUserId(userSessionObj.getUserId());
        trip.setTripCode(generateTripId(userSessionObj.getUserId()));
        trip.setTripUserId(userObj);
        VehicleType vehicleTypeObj = vehicleTypeDao.getVehicaleId(Integer.parseInt(packageServiceDto.getVehicleId()));
        trip.setVehicleId(vehicleTypeObj);
        trip.setPickupAddress(packageServiceDto.getPickup());
        trip.setDropoffAddress(packageServiceDto.getDropOff());
        ServiceType serviceType = serviceTypeDao.getServiceType(3);
        trip.setServiceType(serviceType);
        trip.setDistance(packageServiceDto.getDistance() + " Km");
        trip.setCharges(packageServiceDto.getCharges());
        return trip;
    }

    public void saveCaptainLocation(String address) {
        UserSessionObjDto userSessionObj = (UserSessionObjDto) session.getAttribute("captainSessionObj");
        CaptainDetails captainDetails = captainDetailsDao.getCaptainDetailByUserId(userSessionObj.getUserId());
        captainDetails.setCaptainLocatation(address);
        captainDetails.setAvailable(true);
        captainDetailsDao.updateCaptainDetail(captainDetails);
    }
}












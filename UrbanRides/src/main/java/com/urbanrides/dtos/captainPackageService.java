package com.urbanrides.dtos;


import lombok.Data;

@Data
public class captainPackageService {

    private int tripId;
    private int passengeUserId;
    private String pickUpLocation;
    private String dropLocation;
    private String createdDate;
    private int serviceTypeId;
    private int charges;
    private String distance;
    private String estimatedTime;
    private String tripCode;
    private String passengerName;
    private String contacNumber;
    private String passengerProfilePicLoc;
    private int isProfilePhoto;
    private int numberOfDays;
    private int numberOfPassengers;
    private String dailyPickupDays;


}

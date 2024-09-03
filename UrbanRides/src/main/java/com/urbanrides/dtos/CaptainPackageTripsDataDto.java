package com.urbanrides.dtos;

import lombok.Data;

@Data
public class CaptainPackageTripsDataDto {


    private int tripId;
    private String tripCode;
    private int serviceTypeId;
    private String pickUpLocation;
    private String dropOffLocation;
    private String tripDate;
    private String riderName;
    private String distance;

    private String emergencyContact;
    private String pickupDate;
    private String pickupTime;
    private int numberOfPassengers;
    private int numberOfDays;
    private String dailyPickUpDays;
    private String specialInstruction;
    private String vehicleName;
    private int isTripLive;
    private int isCharges;
    private int charges;


}

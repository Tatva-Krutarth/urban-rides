package com.urbanrides.dtos;


import lombok.Data;

@Data
public class RiderMyTripDataDto {

    //1 for yes 2 for no
    private int serviceTypeId;
    private int vehicleTypeId;
    private String pickUpLocation;
    private String dropOffLocation;
    private String riderName;

    //1 for completed 2 for failed
    private int status;
    private String tripDate;
    private String captainName;
    private String captainProfilePath;
    private float captainRatting;
    private String distance;
    private String duration;
    private int charges;
    private String tripId;
    private String cancellationReason;
    private int isCaptainDetails;
    private int isCancelationDetails;
    private int isAccepted;


    //for rent a taxi and service ride
    private String emergencyContact;
    private String pickupDate;
    private String dropOffDate;
    private String pickupTime;
    private int numberOfPassengers;
    private int numberOfDays;
    private String concludeNotes;
    private String dailyPickUpDays;
    private String specialInstruction;


}

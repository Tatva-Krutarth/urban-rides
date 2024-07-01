package com.urbanrides.dtos;


import lombok.Data;

@Data
public class RiderMyTripDataDto {

    //1 for yes 2 for no
    private int serviceTypeId;
    private int vehicleTypeId;
    private String pickUpLocation;
    private String dropOffLocation;

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

}

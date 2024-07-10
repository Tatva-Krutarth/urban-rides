package com.urbanrides.dtos;

import lombok.Data;


@Data
public class RiderInfoDto {

    private int riderId;
    private int tripId;

    private String riderName;
    private String riderContact;
    private String photoLocation;
    private String riderPickupLocation;
    private String riderDropOffLocation;
    //    private String distance;
//    private String estimatedTime;
    private String charges;
    private int isProfilePhoto;
    private String captainLocation;


}

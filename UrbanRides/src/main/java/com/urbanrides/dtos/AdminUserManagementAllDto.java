package com.urbanrides.dtos;


import lombok.Data;

@Data
public class AdminUserManagementAllDto {
    private int accountType;
    private String userName;
    private String email;
    private String phone;
    private int status;
    private int totalNumberofRides;
    private int totalSuccestrip;
    private int totalFailedTrip;
    private int riderUserId;
//    private int onGoingRide;
}

package com.urbanrides.dtos;

import lombok.Data;

@Data
public class CaptainInfoDto {

    private int captainId;
    private String captainName;
    private String captainContact;
    private int otp;
    private String photo;
    private String location;
    private double ratings;
    private int tripId;
    private String latitude;
    private String longitude;
    private String riderAddress;
    private String vehicleNumber;
}

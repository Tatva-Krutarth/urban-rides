package com.urbanrides.dtos;


import lombok.Data;

@Data
public class CaptainAllTripsDataDto {

    private int tripId;
    private String passengerName;
    private String pickUpLocation;
    private int charges;
    private String dropLocation;
}

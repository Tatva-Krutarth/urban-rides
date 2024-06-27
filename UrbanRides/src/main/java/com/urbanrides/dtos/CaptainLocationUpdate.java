package com.urbanrides.dtos;


import lombok.Data;

@Data
public class CaptainLocationUpdate {
    private double latitude;
    private double longitude;

    public CaptainLocationUpdate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
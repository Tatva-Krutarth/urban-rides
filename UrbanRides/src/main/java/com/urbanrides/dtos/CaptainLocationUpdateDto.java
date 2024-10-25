package com.urbanrides.dtos;


import lombok.Data;

@Data
public class CaptainLocationUpdateDto {
    private double latitude;
    private double longitude;

    public CaptainLocationUpdateDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
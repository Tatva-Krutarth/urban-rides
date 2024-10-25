package com.urbanrides.dtos;


import lombok.Data;

@Data
public class AdminCountDataDto {
    private int totalUserCount;
    private int generalBooking;
    private int serviceBooking;
    private int totalSuccessBooking;
}

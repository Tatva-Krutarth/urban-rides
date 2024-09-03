package com.urbanrides.dtos;


import lombok.Data;

@Data
public class AdminCountData {
    private int totalUserCount;
    private int generalBooking;
    private int serviceBooking;
    private int totalSuccessBooking;
}

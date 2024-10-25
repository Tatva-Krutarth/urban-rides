package com.urbanrides.dtos;

import lombok.Data;

@Data
public class RiderReachInfoDto {

    private String captainEstimatedReachTime;
    private int tripId;
    private String captainAway;
}

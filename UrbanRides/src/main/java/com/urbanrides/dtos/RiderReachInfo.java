package com.urbanrides.dtos;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class RiderReachInfo {

    private String captainEstimatedReachTime;
    private int tripId;
    private String captainAway;
}

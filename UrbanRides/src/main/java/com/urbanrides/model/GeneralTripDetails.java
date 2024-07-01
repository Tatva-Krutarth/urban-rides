package com.urbanrides.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "general_trip_details")
public class GeneralTripDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id", nullable = false)
    private int generalTripDetailId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "general_trip_id", nullable = false)
    @NotNull(message = "General Trip ID cannot be null")
    private Trip tripObj;

    @Column(name = "captain_estimated_reach_time", nullable = false)
    private LocalDateTime captainEstimatedReachTime;

    @Column(name = "captain_away", nullable = false)
    private String captainAway;

    @Column(name = "captain_actual_reach_time")
    private LocalDateTime captainActualReachTime;

    @Column(name = "is_otp_validated", nullable = false)
    private Boolean isOtpValidated = false;

    @Column(name = "is_captain_reached", nullable = false)
    private Boolean isCaptainReached = false;

    @Column(name = "trip_start_time")
    private LocalDateTime tripStartTime;

    @Column(name = "trip_end_time")
    private LocalDateTime tripEndTime;

    @Column(name = "is_trip_completed", nullable = false)
    private Boolean isTripCompleted = false;

    @Size(max = 100, message = "Feed back should not be more than 50 characters")
    @Column(name = "feedback" )
    private String feedback;



    private float captainRating;


}

package com.urbanrides.model;

import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "package_trip")
@Data
public class PackageTrip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_trip_id")
    private int packageTripId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_id", nullable = false)
    @NotNull(message = "Trip ID cannot be null")
    private Trip tripId;


    @NotNull(message = "Pickup date is required")
    @Column(name = "pickup_date", nullable = false)
    private LocalDate pickupDate;

    @NotNull(message = "Pickup time is required")
    @Column(name = "pickup_time", nullable = false)
    private LocalTime pickupTime;

    @NotNull(message = "Drop-off time is required")
    @Column(name = "dropoff_time", nullable = false)
    private LocalTime dropoffTime;

    @Min(value = 1, message = "Number of passengers must be at least 1")
    @Max(value = 30, message = "Number of passengers cannot be more than 30")

    @Column(name = "num_passengers")
    private Integer numPassengers;

    @Max(value = 30, message = "Number of days must be at most 30")
    @Column(name = "num_of_days", nullable = false)
    private Integer numOfDays;

    @Size(max = 10, message = "Emergency contact must be less than 20 characters")
    @Column(name = "emergency_contact", length = 10)
    private String emergencyContact;

    @Size(max = 100, message = "Name must be less than 100 characters")
    @Column(name = "special_instructions", length = 100)
    private String specialInstructions;

    @Size(max = 100, message = "Name must be less than 100 characters")
    @Column(name = "conclude_notes", length = 100)
    private String concludeNotes;

    @Column(name = "pickup_day")

    @Pattern(regexp = "^(|[1-7](,[1-7])*)$", message = "Invalid pickup days format. Should be comma-separated numbers between 1 and 7.")
    private String dailyPickUp;


}

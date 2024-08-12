package com.urbanrides.model;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "trip")
@Setter
@Getter
@NoArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id", nullable = false)
    private int tripId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    private User tripUserId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "servide_type_id", nullable = false)
    @NotNull(message = "Service type cannot be null")
    private ServiceType ServiceType;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleType vehicleId;

    @NotBlank(message = "Pickup Address is required")
    @Length(max = 200, message = "Pickup address cannot be more than 200 characters")
    private String pickupAddress;

    @NotBlank(message = "Dropoff Address is required")
    @Length(max = 200, message = "Dropoff address cannot be more than 200 characters")
    private String dropoffAddress;

    @Column(name = "is_accepted", nullable = false, columnDefinition = "boolean default false")
    private boolean isAccepted;

    @Nullable
    @Length(max = 200, message = "Reason for cancellation cannot be more than 200 characters")
    private String reasonForCancellation;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    //    @NotBlank(message = "Distance is required")
    @Size(max = 20, message = "Distance cannot be more than 20 characters")
    private String distance;

    // Eestimated time to reach destination
    //@NotNull(message = "Estimated Time is required")
    private LocalTime estimatedTime;

    @Column(name = "charges", nullable = false)
    private int charges;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "captain_user_id", nullable = false)
    private User captainUserObj;

    @Column(name = "trip_code")
    @NotNull(message = "trip code is required")
    private String TripCode;

    @Column(name = "payment_method")
    private String paymentMethod;
}

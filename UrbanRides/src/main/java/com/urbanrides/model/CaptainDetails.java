package com.urbanrides.model;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "captain_details")
public class CaptainDetails {
    @Id
    @Column(name = "captainDetailsId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int captainDetailsId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    private User user;

    @Column(nullable = false)
    private boolean adharCard = false;

    @Column(nullable = false)
    private boolean drivingLicense = false;

    @Column(name = "LicenseExpirationDate")
    @Future(message = "License expiration date should be in future")
    private LocalDate LicenseExpirationDate;

    @Column(nullable = false)
    private boolean profilePhoto = false;

    @Column(nullable = false)
    private boolean registrationCertificate = false;

    @Column(name = "rcExpirationDate")
    @Future(message = "RC expiration date should be in future")
    private LocalDate rcExpirationDate;

    @Column(nullable = false)
    private boolean uploadedCertificate = false;

    @Column(nullable = false)
    private boolean isApproved = false;

    @Column(nullable = false)
    private boolean isLive = false;

    @Column(nullable = false)
    @Min(value = 0, message = "Total earnings cannot be negative")
    private float totalEarnings = 0;

    @CreationTimestamp
    @Column(name = "uploadedDate")
    private LocalDate uploadedDate;

}
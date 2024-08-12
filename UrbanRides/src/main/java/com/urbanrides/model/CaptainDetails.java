package com.urbanrides.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "captain_details")
public class CaptainDetails {

    @Id
    @Column(name = "captain_details_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int captainDetailsId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    private User user;

    @Column(name = "adhar_card", nullable = false)
    private boolean adharCard = false;

    @Column(name = "is_adhar_approved", nullable = false)
    private boolean isAdharApproved = false;

    @Column(name = "driving_license", nullable = false)
    private boolean drivingLicense = false;

    @Column(name = "is_license_approved", nullable = false)
    private boolean isLicenseApproved = false;
    @Column(name = "is_free", nullable = false)
    private boolean isFree = true;

    @Column(name = "license_expiration_date")
    @Future(message = "License expiration date should be in the future")
    private LocalDate licenseExpirationDate;

    @Column(name = "profile_photo", nullable = false)
    private boolean profilePhoto = false;

    @Column(name = "registration_certificate", nullable = false)
    private boolean registrationCertificate = false;

    @Column(name = "is_rc_approved", nullable = false)
    private boolean isRcApproved = false;

    @Column(name = "rc_expiration_date")
    @Future(message = "RC expiration date should be in the future")
    private LocalDate rcExpirationDate;

    @Column(name = "is_document_approved", nullable = false)
    private boolean isDocumentApproved = false;

    @Column(name = "is_rc_expiration_date_approved", nullable = false)
    private boolean isRcExpirationDateApproved = false;

    @Column(name = "is_license_expiration_date_approved", nullable = false)
    private boolean isLicenseExpirationDateApproved = false;

    @Column(name = "is_live", nullable = false)
    private boolean isLive = false;

    @Column(name = "total_earnings", nullable = false)
    @Min(value = 0, message = "Total earnings cannot be negative")
    private float totalEarnings = 0;

    @CreationTimestamp
    @Column(name = "uploaded_date")
    private LocalDate uploadedDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_type_id")
    private VehicleType vehicleType;

    @NotNull(message = "Profile photo estension is required")
    @Column(name = "profile_photo_extension")
    private String profilePhotoExtension;

    @Column(name = "captain_location")
    private String captainLocatation;

    @NotNull(message = "Number plate is required")
    @Column(name = "number_plate")
    private String numberPlate;

    @Column(name = "is_number_plate_approved", nullable = false)
    private boolean isNumberplateApproved = false;

    @Column(name = "ratting", nullable = false)
    private float ratting = 0.0f;
}

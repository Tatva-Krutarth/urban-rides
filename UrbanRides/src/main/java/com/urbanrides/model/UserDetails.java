package com.urbanrides.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @Column(name = "user_details_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userDetailsId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name is required")
    @Size(max = 10, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name is required")
    @Size(max = 10, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
//    @NotNull(message = "Created date is required")
    private LocalDate createdDate;

    @Column(name = "is_blocked_by_admin", nullable = false, columnDefinition = "boolean default true")
    private boolean isBlockedByAdmin = true;


    @Column(precision = 10, scale = 2)
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount must be greater than or equal to 0.00")
    private BigDecimal wallet = BigDecimal.valueOf(0.00);

    @Column(name = "success_trip_count", columnDefinition = "int default 0")
    private int successTripCount;

    @Column(name = "fail_trip_count", columnDefinition = "int default 0")
    private int failTripCount;

    @Column(name = "profile_photo", columnDefinition = "bit default 0")
    private boolean profilePhoto;

    @Column(name = "age", nullable = false)
    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be greater than or equal to 0")
    @Max(value = 120, message = "Age must be less than or equal to 120")
    private int age;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 13, message = "Phone number must be between 10 and 13 characters")
    @Column(name = "phone")
    private String phone;

    @Column(name = "profile_extention")
    private String profilePhotoExtention;

}
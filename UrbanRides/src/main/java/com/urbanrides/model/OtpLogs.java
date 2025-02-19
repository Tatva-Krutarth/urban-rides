package com.urbanrides.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "otp_logs")
public class OtpLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    private int otpId;

    @Nullable
    @Email(message = "Email must be a valid email address or phone number")
    @Column(name = "email")
    private String email;


    @NotNull(message = "Request send time cannot be null")
    @Column(name = "opt_req_send_time")
    private LocalTime optReqSendTime;



    @NotNull(message = "Generated OTP cannot be null")
    @Column(name = "generated_otp")
    private int generatedOtp;


    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDate createdDate;
}
package com.urbanrides.model;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid email address")
    @Column(name = "email")
    private String email;

//    @Size(min = 10, max = 13, message = "Phone number must be between 10 and 15 characters")
//    @Column(name = "phone")
//    @Nullable
//
//    private String phone;
    @Column(name = "password_hash", length = 60) // adjusted length to 60
    @NotBlank(message = "Password hash cannot be blank")
    @Size(min = 60, message = "Password hash must be at least 60 characters")
    private String passwordHash;

    @Column(name = "salt", length = 24) // adjusted length to 24
    @NotBlank(message = "Salt cannot be blank")
    @Size(min = 22, message = "Salt must be at least 22 characters")
    private String salt;

    @NotNull(message = "Account type cannot be null")
    @Column(name = "account_type")
    private int accountType;

}
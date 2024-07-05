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

    @NotNull
    @Column(name = "account_status", columnDefinition = "int default 0")
    private int accountStatus = 0;

    @Column(name = "password_hash", length = 60)
    @NotBlank(message = "Password hash cannot be blank")
    @Size(min = 60, message = "Password hash must be at least 60 characters")
    private String passwordHash;

    @Column(name = "salt", length = 24)
    @NotBlank(message = "Salt cannot be blank")
    @Size(min = 22, message = "Salt must be at least 22 characters")
    private String salt;

    @NotNull(message = "Account type cannot be null")
    @Column(name = "account_type")
    private int accountType;

}
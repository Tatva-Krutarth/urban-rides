package com.urbanrides.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "service_type")
@Data
public class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceTypeId;

    @NotBlank(message = "Service type is mandatory")
    @Size(max = 20, message = "Service type must be less than 20 characters")
    private String serviceType;
}

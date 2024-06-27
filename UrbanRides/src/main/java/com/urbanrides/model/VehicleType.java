package com.urbanrides.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vehicle_type") // Specify the table name
public class VehicleType {

    @Id
    @Column(name = "vehicle_id", nullable = false)
    private Integer vehicleId;

    @Column(name = "vehicale_type_name", nullable = false) // Max length, unique constraint
    private String vehicleTypeName;


}
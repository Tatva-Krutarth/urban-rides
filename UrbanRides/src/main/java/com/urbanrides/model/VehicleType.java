package com.urbanrides.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vehicle_type")
public class VehicleType {

    @Id
    @Column(name = "vehicle_id", nullable = false)
    private Integer vehicleId;

    @Column(name = "vehicale_type_name", nullable = false)
    private String vehicleTypeName;


}
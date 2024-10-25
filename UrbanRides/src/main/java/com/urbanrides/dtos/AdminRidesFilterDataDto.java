package com.urbanrides.dtos;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class AdminRidesFilterDataDto {


    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Trip ID must be alphanumeric.")
    private String tripCode;
    @NotNull(message = "Service type is required.")
    private int serviceType;
    @NotNull(message = "Trip status is required.")
    private int tripStatus;


}
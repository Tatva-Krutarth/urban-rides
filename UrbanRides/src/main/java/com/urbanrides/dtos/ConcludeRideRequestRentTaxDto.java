package com.urbanrides.dtos;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class ConcludeRideRequestRentTaxDto {


    @NotNull(message = "Trip id cannot be null")
    private int tripId;
    @Size(max = 150, message = "conclusionNote message must be less than 150 characters")
    private String conclusionNote;
    @Min(value = 4, message = "Charges must be at least 4")
    private int charges;
    @Pattern(regexp = "^(Pay with wallet|Pay with cash)$", message = "Payment method must be either 'cash', 'wallet'")
    private String paymentMethod;

    private String distance;

}

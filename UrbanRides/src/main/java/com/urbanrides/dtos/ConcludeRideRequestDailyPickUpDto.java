package com.urbanrides.dtos;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ConcludeRideRequestDailyPickUpDto {
    @NotNull(message = "Trip id cannot be null")

    private int tripId;
    @Size(max = 150, message = "conclusionNote message must be less than 150 characters")

    private String conclusionNote;
    @Pattern(regexp = "^(Pay with wallet|Pay with cash)$", message = "Payment method must be either 'cash', 'wallet'")

    private String paymentMethod;

}

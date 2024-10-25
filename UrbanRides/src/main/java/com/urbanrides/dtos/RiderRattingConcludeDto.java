package com.urbanrides.dtos;


import lombok.Data;

import javax.validation.constraints.*;

@Data
public class RiderRattingConcludeDto {

    @NotNull(message = "Trip ID cannot be null")
    @Positive(message = "Trip ID must be positive")
    private int tripId;
    @Min(value = 0, message = "Ratings must be at least 0")
    @Max(value = 5, message = "Ratings cannot exceed 5")
    private float rattings;
    @Size(max = 255, message = "Feedback cannot exceed 255 characters")
    private String feedback;
    @Pattern(regexp = "^(Pay with wallet|Pay with cash)$", message = "Payment method must be either 'cash', 'wallet'")
    private String payMethod;

}

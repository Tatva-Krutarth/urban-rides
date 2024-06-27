package com.urbanrides.dtos;


import lombok.Data;

@Data
public class RiderRattingConclude {

    private int tripId;
    private float rattings;
    private String feedback;
    private String payMethod;

}

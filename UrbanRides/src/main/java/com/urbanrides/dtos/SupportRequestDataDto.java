package com.urbanrides.dtos;


import lombok.Data;

@Data
public class SupportRequestDataDto {

    private String id;
    private String type;
    private String description;
    private String status;

}

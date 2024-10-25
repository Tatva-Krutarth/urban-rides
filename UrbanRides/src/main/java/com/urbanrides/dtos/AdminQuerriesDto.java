package com.urbanrides.dtos;

import lombok.Data;

@Data
public class AdminQuerriesDto {

    private int userID;
    private int supportId;
    private String contactDetails;
    private String SypportType;
    private String accountType;
    private String message;
    private String createdDate;
    private String userName;
    private String adminName;
    private int fileAvailable ;
    private String fileLocaton;
}

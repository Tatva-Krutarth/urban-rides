package com.urbanrides.dtos;


import lombok.Data;

@Data
public class AdminCaptainApproveDataDto {

    private int captainId;
    private String captainName;
    private String phone;
    private String email;
    private String createdDate;
    private String status;
    private String adharCard;
    private boolean adharApprovedApprove;

    private String DrivingLicence;
    private boolean drivingLicenceApprove;

    private String DrivingLicenceExpiryDate;
    private boolean drivingLicenceExpiryDateApprove;


    private boolean rCCertificateApprove;
    private String rCCertificate;
    private String rCExpirationDate;
    private boolean rCExpirationDateApprove;

    private String numberPlate;
    private boolean isNumberPlateApprove;


    private String vehicleNumber;


}

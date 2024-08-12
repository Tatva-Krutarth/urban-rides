package com.urbanrides.dtos;

import lombok.Data;

@Data
public class CaptainReuploadDataRendering {

    private int captainId;
    private String captainName;

    private String adharCard;
    private boolean isAdharApprovedApprove;

    private String DrivingLicence;
    private boolean isDrivingLicenceApprove;

    private String DrivingLicenceExpiryDate;
    private boolean isDrivingLicenceExpiryDateApprove;


    private boolean isRCCertificateApprove;
    private String rCCertificate;

    private String rCExpirationDate;
    private boolean isRCExpirationDateApprove;

    private String numberPlate;
    private boolean isNumberPlateApprove;


}

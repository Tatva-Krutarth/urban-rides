package com.urbanrides.dtos;


import lombok.Data;

@Data
public class RiderWalletDataDto {
    private int serviceType;
    private String dateAndTime;
    private String walletHeader;
    private float paidAmount;
    private int paymentMethod;

}

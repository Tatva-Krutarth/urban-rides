package com.urbanrides.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RattingModalDataDto {
    private int charges ;
    private String profilePhoto;
    private BigDecimal balance;
    private String captainName;

}

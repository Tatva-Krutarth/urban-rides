package com.urbanrides.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor

public class UserSessionObjDto {

    private int userId;
    private int accountTypeId;
    private int accountStatus;
    private int profilePhoto;
    private String profileLoc;
}

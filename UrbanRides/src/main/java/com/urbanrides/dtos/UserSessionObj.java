package com.urbanrides.dtos;

import lombok.Data;

@Data

public class UserSessionObj {

    private int userId;
    private int accountTypeId;
    private int accountStatus;
    private String profileLoc;
}

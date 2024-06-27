package com.urbanrides.dtos;

import lombok.Data;

@Data
public class UserManagementDataDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String profilePhotoPath;
}

package com.auth.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ChangePasswordDto {


    private String email;

    private String oldPassword;

    private String newPassword;

}

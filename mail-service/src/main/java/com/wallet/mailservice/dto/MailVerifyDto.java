package com.wallet.mailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailVerifyDto {

    @NotBlank
    @Size(min = 2, max = 20, message = "name length must be between 10 and 500")
    private String name;

    @NotBlank
    @Size(min = 2, max = 20, message = "surname length must be between 10 and 500")
    private String surname;

    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+){5,}$", message = "email is invalid")
    private String email;

    private String mailVerificationLink;

    private String mailType;

}
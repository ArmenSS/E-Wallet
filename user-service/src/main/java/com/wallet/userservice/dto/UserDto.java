package com.wallet.userservice.dto;

import com.wallet.userservice.entity.Gender;
import com.wallet.userservice.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private UserRole userRole;
    private UUID mailVerifyToken;
    private boolean isMailVerified;
    private String phoneNumber;
    private int age;
    private Gender gender;

}

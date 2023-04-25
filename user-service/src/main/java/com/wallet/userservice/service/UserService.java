package com.wallet.userservice.service;

import com.wallet.userservice.dto.LoginRequest;
import com.wallet.userservice.dto.UserAuthResponse;
import com.wallet.userservice.dto.UserDto;
import com.wallet.userservice.exception.DuplicateUserException;

public interface UserService {

    UserDto save(UserDto userDto) throws DuplicateUserException;

    UserAuthResponse auth(LoginRequest loginRequest);

    UserDto findByEmail(String email);

    String verifyUserByEmailToken(String token);

    UserDto findById(Long id);

    void delete(Long id);

    UserDto update(UserDto userDto);

}

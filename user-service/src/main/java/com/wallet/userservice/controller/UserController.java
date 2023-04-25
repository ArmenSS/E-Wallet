package com.wallet.userservice.controller;

import com.wallet.userservice.dto.LoginRequest;
import com.wallet.userservice.dto.UserAuthResponse;
import com.wallet.userservice.dto.UserDto;
import com.wallet.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * @param userDto
     * @return ResponseEntity<UserDto> This controller accept UserDto ans save it
     * in DB after that return UserDto
     */
    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> registration(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.save(userDto));
    }

    /**
     * @param email
     * @return UserDto This controller find classroom byEmail, when email exist return
     * UserDto
     */
    @GetMapping("/byEmail/{email}")
    public UserDto findByEmail(@PathVariable("email") String email) {
        return userService.findByEmail(email);
    }

    /**
     * @param id
     * @return UserDto This controller find classroom byId, when id exist return
     * UserDto
     */
    @GetMapping("/byId/{id}")
    public UserDto findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    /**
     * @param id This controller find classroom byId, when id exist remove classroom from DB
     */
    @GetMapping("/deleteById/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    /**
     * @param userDto
     * @return ResponseEntity<UserDto> This controller accept UserDto ans updates it
     */
    @GetMapping("/update")
    public UserDto update(@RequestBody UserDto userDto) {
        return userService.update(userDto);
    }

    /**
     * @param token
     * @return String This controller need as for email verification when you verify
     */
    @GetMapping("/verify/{token}")
    public String verifyUserByEmailToken(@PathVariable("token") String token) {
        return userService.verifyUserByEmailToken(token);
    }

    /**
     * @param loginRequest
     * @return UserAuthResponse This controller for login write your correct login and password
     */
    @GetMapping("/auth")
    public UserAuthResponse auth(@PathVariable LoginRequest loginRequest) {
        return userService.auth(loginRequest);
    }
}

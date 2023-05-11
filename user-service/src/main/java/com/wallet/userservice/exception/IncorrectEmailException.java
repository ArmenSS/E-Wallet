package com.wallet.userservice.exception;

public class IncorrectEmailException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Email format is incorrect";
    }
}

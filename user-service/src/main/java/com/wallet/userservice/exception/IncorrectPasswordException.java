package com.wallet.userservice.exception;

public class IncorrectPasswordException extends RuntimeException {

    @Override
    public String getMessage() {
        return "your password must have capital letter, small letter, number, symbol";
    }

}

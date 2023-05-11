package com.wallet.transactionservice.exception;

public class AccessDeniedException extends RuntimeException {

    @Override
    public String getMessage() {
        return "access denied";
    }
}
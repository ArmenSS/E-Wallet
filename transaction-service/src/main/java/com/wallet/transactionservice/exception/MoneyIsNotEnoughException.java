package com.wallet.transactionservice.exception;

public class MoneyIsNotEnoughException extends RuntimeException {

    @Override
    public String getMessage() {
        return "you have not enough money for transfer";
    }
}
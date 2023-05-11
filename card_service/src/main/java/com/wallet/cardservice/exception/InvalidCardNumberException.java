package com.wallet.cardservice.exception;

public class InvalidCardNumberException extends RuntimeException {

  @Override
  public String getMessage() {
    return "Card number is invalid";
  }
}
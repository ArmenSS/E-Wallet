package com.wallet.cardservice.exception;

public class CardDateExpiredException extends RuntimeException {

  @Override
  public String getMessage() {
    return "Your card date expired";
  }
}
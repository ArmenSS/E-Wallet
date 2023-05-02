package com.wallet.userservice.exception;

public class DuplicateUserException extends RuntimeException {

  @Override
  public String getMessage() {
    return "Email already exist";
  }
}
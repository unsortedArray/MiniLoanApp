package com.example.demo.model.exceptions;

public class UserAlreadyExistsException extends Throwable {
  public UserAlreadyExistsException(String s) {
    super(s);
  }
}

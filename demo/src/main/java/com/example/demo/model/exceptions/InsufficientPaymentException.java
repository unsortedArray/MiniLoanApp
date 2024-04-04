package com.example.demo.model.exceptions;

public class InsufficientPaymentException extends RuntimeException {
  public InsufficientPaymentException() {
    super("Payment amount is insufficient for loan EMI");
  }
}
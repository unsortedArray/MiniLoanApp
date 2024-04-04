package com.example.demo.model.exceptions;

public class NoPendingRepaymentException extends RuntimeException {
  public NoPendingRepaymentException() {
    super("No pending repayment found for loan ID");
  }
}

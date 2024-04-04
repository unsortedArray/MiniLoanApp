package com.example.demo.model.exceptions;

public class LoanPendingApprovalException extends RuntimeException {
  public LoanPendingApprovalException() {
    super("The loan still requires verification");
  }
}

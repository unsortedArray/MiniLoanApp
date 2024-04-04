package com.example.demo.model.exceptions;

public class LoanNotFoundException extends RuntimeException {
  public LoanNotFoundException(int loanId) {
    super("No loan found with ID: " + loanId);
  }
}
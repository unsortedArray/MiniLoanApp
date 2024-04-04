package com.example.demo.model.exceptions;

public class LoanRejectedException extends RuntimeException  {
  public LoanRejectedException() {
    super("Loan was rejected");
  }
}

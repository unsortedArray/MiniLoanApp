package com.example.demo.model.exceptions;

import java.time.LocalDateTime;

public class LoanAlreadyPaidException extends RuntimeException {
  private LocalDateTime dateClosed;

  public LoanAlreadyPaidException(LocalDateTime dateClosed) {
    super("Loan was paid completely on " + dateClosed);
    this.dateClosed = dateClosed;
  }

  public LocalDateTime getDateClosed() {
    return dateClosed;
  }
}
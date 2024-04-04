package com.example.demo.model.loan;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayEwiRequest {
  @NotNull(message = "LoanId is required")
  @Positive(message = "loanId number must be present.")
  int loanId;

  @NotNull
  @Positive(message = "Amount must be present")
  double ewiAmount;
}

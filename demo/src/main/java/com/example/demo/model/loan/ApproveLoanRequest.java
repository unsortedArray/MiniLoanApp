package com.example.demo.model.loan;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
public class ApproveLoanRequest {
  @NotNull(message = "LoanId is required")
  @Positive(message = "loanId number must be present.")
  int loanId;
}

package com.example.demo.model.loan;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLoanRequest {
  @NotNull(message = "Amount is required")
  @Min(value = 0, message = "Amount must be greater than or equal to 0")
  private double amount;
  @NotNull(message = "Term is required")
  @Min(value = 1, message = "Term must be greater than or equal to 1")
  private Integer term;

}

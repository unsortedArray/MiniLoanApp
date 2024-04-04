package com.example.demo.model.loan;

import com.example.demo.enums.LoanStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoanDTO {
  private double amount;
  private int term;
  private int dueTerm;
  private LoanStatus loanStatus;
  private double weeklyEMI;
  private double remainingAmount;
  private LocalDateTime nextDueDate;
}

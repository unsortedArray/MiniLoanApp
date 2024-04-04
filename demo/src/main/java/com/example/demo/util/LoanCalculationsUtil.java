package com.example.demo.util;

import static com.example.demo.util.Constants.INTEREST_RATE_FED;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LoanCalculationsUtil {

  public static double calculateEWI(double amount, int term) {
    double result = (double) amount / term;
    return  Math.round(result * 1000.0) / 1000.0;
  }

  public static double calculateLoan(double amount, int term) {
    // Convert weekly interest rate to decimal/
    double rate = INTEREST_RATE_FED / 100.0;

    // Calculate simple interest
    double simpleInterest = amount * rate * term;

    // Calculate total amount
    return amount + simpleInterest;
  }
}

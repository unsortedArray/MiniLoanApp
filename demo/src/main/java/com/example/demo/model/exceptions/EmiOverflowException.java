package com.example.demo.model.exceptions;

/**
 * Exception indicating that the customer is attempting to pay more than the total pending amount.
 */
public class EmiOverflowException extends RuntimeException{

  /**
   * Exception indicating that the customer is attempting to pay more than the total pending amount.
   */
  public EmiOverflowException(){
    super("Customer cannot pay more than the total pending amount");
  }
}

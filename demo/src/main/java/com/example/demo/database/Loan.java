package com.example.demo.database;


import com.example.demo.enums.LoanStatus;
import com.example.demo.enums.LoanType;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a loan.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Data
public class Loan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private double amount;

  @Column(nullable = false)
  private int term;

  @Column(name = "due_term")
  private int dueTerm;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LoanStatus status;

  @Column(name = "user_name")
  private String username;

  @Column(name = "interest_rate")
  private double interestRate;

  @Column(name = "total_amount")
  private double totalAmount;

  @Column(name = "pending_amount")
  private double pendingAmount;

  @Column(name = "weekly_emi")
  private double weeklyEmi;

  @Column(nullable = false)
  private LocalDateTime dateCreated;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LoanType type;

  @Column(name = "date_approved")
  private LocalDateTime dateApproved;

  @Column(name="date_closed")
  private LocalDateTime dateClosed;

  @Column(name = "next_due_date")
  private LocalDateTime nextDueDate;

}

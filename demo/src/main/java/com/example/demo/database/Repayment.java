package com.example.demo.database;
import com.example.demo.enums.RepaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table

/**
 * Entity class representing a repayment for a loan.
 */
public class Repayment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private double amount;

  @Column(nullable = false)
  private LocalDateTime dueDate;

  @Column(name = "payment_date")
  private LocalDateTime paymentDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RepaymentStatus status;

  @ManyToOne
  @JoinColumn(name = "loan_id", nullable = false)
  private Loan loan;
}

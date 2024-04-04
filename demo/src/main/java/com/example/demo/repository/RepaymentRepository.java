package com.example.demo.repository;

import com.example.demo.database.Loan;
import com.example.demo.database.Repayment;
import com.example.demo.enums.RepaymentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RepaymentRepository extends JpaRepository<Repayment, Long> {

  @Query("SELECT r FROM Repayment r WHERE r.loan.id = :loanId AND r.status = 'PENDING' ORDER BY r.dueDate ASC LIMIT 1")
  Optional<Repayment> findFirstByLoanIdAndStatusOrderByDueDateAsc(int loanId);

  List<Repayment> findByLoanAndStatus(Loan loan, RepaymentStatus repaymentStatus);

}


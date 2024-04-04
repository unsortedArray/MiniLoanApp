package com.example.demo.repository;

import com.example.demo.database.Loan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {

  Loan findById(long id);

  List<Loan> findAllByUsername(String username);

  List<Loan> findByUsername(String username);

  Loan findByIdAndUsername(long id, String username);

}

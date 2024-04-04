package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.database.Loan;
import com.example.demo.enums.LoanStatus;
import com.example.demo.enums.LoanType;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LoanRepositoryTest {


  @Autowired
  private LoanRepository loanDao;
  private final String USERNAME = "root";
  private final Loan LOAN = Loan.builder()
      .username(USERNAME)
      .amount(1500.0)
      .pendingAmount(1500.0)
      .weeklyEmi(500.0)
      .type(LoanType.SIMPLE)
      .term(3)
      .dueTerm(3)
      .status(LoanStatus.PENDING)
      .dateCreated(LocalDateTime.now())
      .build();

  @BeforeEach
  void setUp() {
    loanDao.save(LOAN);
  }

  @AfterEach
  void tearDown() {
    loanDao.deleteAll();
  }

  @Test
  void findByUsernameSuccess() {
    List<Loan> actual = loanDao.findByUsername(USERNAME);
    assertNotNull(actual);
    assertEquals(1, actual.size());
    assertEquals(USERNAME, actual.get(0).getUsername());
  }

  @Test
  void findByIdAndUsernameSuccess() {
    Loan actual = loanDao.findById(2);
    assertNotNull(actual);
  }

}

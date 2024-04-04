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
  private LoanRepository loanRepository;
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
    loanRepository.save(LOAN);
  }

  @AfterEach
  void tearDown() {
    loanRepository.deleteAll();
  }

  @Test
  void findByUsernameSuccess() {
    List<Loan> actual = loanRepository.findByUsername(USERNAME);
    assertNotNull(actual);
    assertEquals(1, actual.size());
    assertEquals(USERNAME, actual.get(0).getUsername());
  }

  @Test
  void findByIdAndUsernameSuccess() {
    Loan actual = loanRepository.findById(1);
    assertNotNull(actual);
  }


  @Test
  void findByUsernameEmpty() {
    // Arrange
    String nonExistingUsername = "nonexistent";

    // Act
    List<Loan> actual = loanRepository.findByUsername(nonExistingUsername);

    // Assert
    assertNotNull(actual);
    assertTrue(actual.isEmpty());
  }

  @Test
  void findByIdAndUsernameNotFound() {
    // Arrange
    int nonExistingId = 999;

    // Act
    Loan actual = loanRepository.findByIdAndUsername(nonExistingId, USERNAME);

    // Assert
    assertNull(actual);
  }

  @Test
  void findByIdAndUsernameFoundButDifferentUsername() {
    // Arrange
    Loan loanWithDifferentUsername = Loan.builder()
        .username("otheruser")
        .amount(2000.0)
        .pendingAmount(2000.0)
        .weeklyEmi(600.0)
        .type(LoanType.SIMPLE)
        .term(4)
        .dueTerm(4)
        .status(LoanStatus.PENDING)
        .dateCreated(LocalDateTime.now())
        .build();
    loanRepository.save(loanWithDifferentUsername);

    // Act
    Loan actual = loanRepository.findByIdAndUsername(loanWithDifferentUsername.getId(),USERNAME);

    // Assert
    assertNull(actual);
  }


}

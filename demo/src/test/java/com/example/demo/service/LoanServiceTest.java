package com.example.demo.service;

import com.example.demo.database.Loan;
import com.example.demo.enums.LoanStatus;
import com.example.demo.enums.LoanType;
import com.example.demo.model.loan.ApproveLoanRequest;
import com.example.demo.model.loan.CreateLoanRequest;
import com.example.demo.model.loan.LoanDTO;
import com.example.demo.model.loan.PayEwiRequest;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.RepaymentRepository;
import com.example.demo.util.LoanCalculationsUtil;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.util.LoanCalculationsUtil.calculateEWI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LoanServiceTest {
  @Mock
  private LoanRepository loanDao;
  @Mock
  private RepaymentRepository loanInstalmentDao;
  @Mock
  private LoanCalculationsUtil instalmentCalculator;
  @InjectMocks
  private LoanService loanService;
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

  @BeforeAll
  static void setup() {
    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(authentication.getName()).thenReturn("root");
  }

  @Test
  void createLoan() {
    CreateLoanRequest request = CreateLoanRequest.builder()
        .amount(1500)
        .term(3)
        .build();


    // Call the method under test
    ResponseEntity<String> response = loanService.save(request);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Loan Created Successfully", response.getBody());
  }

  @Test
  void getLoanByUsername() throws BadRequestException {
    Mockito.when(loanDao.findAllByUsername(USERNAME)).thenReturn(List.of(LOAN));
    List<LoanDTO> loan = loanService.getAllLoans();
    assertNotNull(loan);
    assertEquals(1, loan.size());
  }

  @Test
  void approveLoan() throws BadRequestException {
    ApproveLoanRequest approveLoanRequest = ApproveLoanRequest.builder()
        .loanId(1)
        .build();

    ResponseEntity response= loanService.approve(approveLoanRequest);
  }


  @Test
  void repayEwiLoanNotFound() {
    PayEwiRequest payEwiRequest = PayEwiRequest.builder()
        .loanId(999) // Set a non-existent loan ID
        .ewiAmount(500.0)
        .build();

    // Call the method under test
    ResponseEntity<String> response = loanService.repayEwi(payEwiRequest);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("No loan found with ID: " + payEwiRequest.getLoanId(), response.getBody());
  }
}
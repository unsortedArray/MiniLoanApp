package com.example.demo.service;

import static com.example.demo.util.LoanCalculationsUtil.calculateEWI;
import static com.example.demo.util.Constants.INTEREST_RATE_FED;
import static com.example.demo.util.LoanCalculationsUtil.calculateLoan;

import com.example.demo.database.Loan;
import com.example.demo.database.Repayment;
import com.example.demo.model.exceptions.EmiOverflowException;
import com.example.demo.model.exceptions.InsufficientPaymentException;
import com.example.demo.model.exceptions.LoanAlreadyPaidException;
import com.example.demo.model.exceptions.LoanNotFoundException;
import com.example.demo.model.exceptions.LoanPendingApprovalException;
import com.example.demo.model.exceptions.LoanRejectedException;
import com.example.demo.model.exceptions.NoPendingRepaymentException;
import com.example.demo.model.loan.ApproveLoanRequest;
import com.example.demo.model.loan.CreateLoanRequest;
import com.example.demo.model.loan.LoanDTO;
import com.example.demo.model.loan.PayEwiRequest;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.RepaymentRepository;
import com.example.demo.enums.LoanStatus;
import com.example.demo.enums.LoanType;
import com.example.demo.enums.RepaymentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


/**
 * Service class responsible for managing loan-related operations.
 */
@Service
@RequiredArgsConstructor
public class LoanService {
  @Autowired private final LoanRepository loanRepository;

  @Autowired private final RepaymentRepository repaymentRepository;

  /**
   * Saves a loan request for the customer.
   * @param request The request to create a new loan.
   * @return A response entity indicating the result of the operation.
   */
  public ResponseEntity<String> save(CreateLoanRequest request) {

    try {
      Loan loan =
          Loan.builder()
              .amount(request.getAmount())
              .term(request.getTerm())
              .status(LoanStatus.PENDING)
              .dateCreated(LocalDateTime.now())
              .username(SecurityContextHolder.getContext().getAuthentication().getName())
              .weeklyEmi(calculateEWI(request.getAmount(), request.getTerm()))
              .interestRate(INTEREST_RATE_FED)
              .type(LoanType.SIMPLE)
              .totalAmount(calculateLoan(request.getAmount(), request.getTerm()))
              .pendingAmount(calculateLoan(request.getAmount(), request.getTerm()))
              .dueTerm(request.getTerm())
              .build();
      loanRepository.save(loan);
      return ResponseEntity.status(HttpStatus.OK).body("Loan Created Successfully");
    } catch (DataIntegrityViolationException e) {
      // Handle data integrity violation exception (e.g., duplicate key, constraint violation)
      throw new RuntimeException(
          "Failed to save loan due to data integrity violation: " + e.getMessage());
    } catch (DataAccessException e) {
      // Handle other data access exceptions
      throw new RuntimeException(
          "Failed to save loan due to data access exception: " + e.getMessage());
    } catch (Exception e) {
      // Handle other general exceptions
      throw new RuntimeException("Failed to save loan due to unexpected error: " + e.getMessage());
    }
  }

  /**
   * Approves a loan request.
   * @param request The request to approve a loan.
   * @return A response entity indicating the result of the operation.
   */
  public ResponseEntity<String> approve(ApproveLoanRequest request) {

    Loan loan = loanRepository.findById(request.getLoanId());
    if (Objects.isNull(loan)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("No loan found with ID: " + request.getLoanId());
    }
    if (loan.getStatus().equals(LoanStatus.APPROVED)) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Loan is already approved for " + request.getLoanId());
    }
    if (loan.getStatus().equals(LoanStatus.PAID)) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Loan was closed on" + loan.getDateClosed() + " " + request.getLoanId());
    }
    if (loan.getStatus().equals(LoanStatus.REJECTED)) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Loan was rejected " + request.getLoanId());
    }
    try {
      loan.setStatus(LoanStatus.APPROVED);
      loan.setDateApproved(LocalDateTime.now());
      loan.setNextDueDate(LocalDateTime.now().plusWeeks(1));

      // Approve the loan and populate repayments
      populateRepayments(loan);
      loanRepository.save(loan);
    } catch (DataIntegrityViolationException e) {
      // Handle data integrity violation exception (e.g., duplicate key, constraint violation)
      throw new RuntimeException(
          "Failed to save loan due to data integrity violation: " + e.getMessage());
    } catch (DataAccessException e) {
      // Handle other data access exceptions
      throw new RuntimeException(
          "Failed to save loan due to data access exception: " + e.getMessage());
    } catch (Exception e) {
      // Handle other general exceptions
      throw new RuntimeException("Failed to save loan due to unexpected error: " + e.getMessage());
    }

    return ResponseEntity.status(HttpStatus.OK).body("Loan approved successfully");
  }

  /**
   * Populates the repayments for the approved loan.
   * @param loan The loan for which repayments are to be populated.
   */
  public void populateRepayments(Loan loan) {

    IntStream.range(0, loan.getTerm())
        .mapToObj(
            i ->
                Repayment.builder()
                    .amount(loan.getWeeklyEmi())
                    .dueDate(loan.getNextDueDate().plusWeeks(i))
                    .status(RepaymentStatus.PENDING)
                    .loan(loan)
                    .build())
        .forEach(repaymentRepository::save);
  }

  /**
   * Repays the equated weekly installment (EWI) for the loan.
   * @param request The request to pay EWI for a loan.
   * @return A response entity indicating the result of the operation.
   */

  public ResponseEntity<String> repayEwi(PayEwiRequest request) {
    try {
      Loan loan = loanRepository.findById(request.getLoanId());
      if (Objects.isNull(loan)) {
        // Handle cases where loan is not found or not eligible for repayment
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("No loan found with ID: " + request.getLoanId());
      }
      validateLoanForRepayment(loan, request);
      processRepayment(loan, request);
      return ResponseEntity.ok("Loan EMI paid successfully");
    } catch (LoanNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("No loan found with ID: " + request.getLoanId());
    } catch (LoanAlreadyPaidException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Loan was paid completely on " + e.getDateClosed());
    } catch (LoanRejectedException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Loan was rejected");
    } catch (InsufficientPaymentException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Payment amount is insufficient for loan EMI");
    } catch (NoPendingRepaymentException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("No pending repayment found for loan ID: " + request.getLoanId());
    } catch (LoanPendingApprovalException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Loan is yet to be approved: " + request.getLoanId());
    } catch (DataAccessException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to save loan due to data access exception: " + e.getMessage());
    }
  }

  /**
   * Processes the repayment for the loan.
   * This method calculates the remaining amount after payment, updates the repayment status,
   * adjusts the remaining repayments if necessary, and updates the loan status accordingly.
   * @param loan The loan for which repayment is processed.
   * @param request The request containing repayment details.
   */
  private void processRepayment(Loan loan, PayEwiRequest request) {
    Optional<Repayment> ewi =
        repaymentRepository.findFirstByLoanIdAndStatusOrderByDueDateAsc(request.getLoanId());

    if (ewi.isPresent()) {
      Repayment repayment = ewi.get();
      if (request.getEwiAmount() < loan.getPendingAmount()
          && request.getEwiAmount() < ewi.get().getAmount()) {
        throw new InsufficientPaymentException();
      }
      double remainingAmount = request.getEwiAmount() - loan.getWeeklyEmi();
      repayment.setAmount(request.getEwiAmount());
      repayment.setStatus(RepaymentStatus.PAID);
      repayment.setPaymentDate(LocalDateTime.now());
      repaymentRepository.save(repayment);

      loan.setDueTerm(loan.getDueTerm() - 1);
      loan.setPendingAmount(loan.getPendingAmount() - request.getEwiAmount());
      if (remainingAmount > 0) {
        adjustRemainingRepayments(loan, repayment, remainingAmount);
      }

      if (loan.getPendingAmount() == 0) {
        loan.setStatus(LoanStatus.PAID);
        loan.setDateClosed(LocalDateTime.now());

        prepayEWI(loan);
      } else {
        loan.setNextDueDate(repayment.getDueDate().plusWeeks(1));
      }
      loanRepository.save(loan);
    }else {
      throw new LoanAlreadyPaidException(loan.getDateClosed());
    }
  }

  private void prepayEWI(Loan loan) {
    List<Repayment> remainingRepayments =
        repaymentRepository.findByLoanAndStatus(loan, RepaymentStatus.PENDING);

    for (Repayment curRepayment: remainingRepayments){
      curRepayment.setStatus(RepaymentStatus.PAID);
      curRepayment.setPaymentDate(LocalDateTime.now());

    }
    repaymentRepository.saveAll(remainingRepayments);
  }

  /**
   * Adjusts the remaining repayments for the loan.
   * This method updates the remaining repayment amounts and dates if there is any extra payment made.
   * @param loan The loan for which repayments are adjusted.
   * @param lastPaidRepayment The last repayment made.
   * @param remainingAmount The remaining amount after the last payment.
   */
  private void adjustRemainingRepayments(
      Loan loan, Repayment lastPaidRepayment, double remainingAmount) {
    List<Repayment> remainingRepayments =
        repaymentRepository.findByLoanAndStatus(loan, RepaymentStatus.PENDING);

    for (Repayment remainingRepayment : remainingRepayments) {
      if (remainingAmount > remainingRepayment.getAmount()) {
        loan.setTerm(loan.getTerm() - 1);
        remainingRepayment.setPaymentDate(LocalDateTime.now());
        loanRepository.save(loan);
      }
      double newAmount = Math.min(remainingRepayment.getAmount(), remainingAmount);
      remainingRepayment.setAmount(newAmount);
      remainingAmount -= newAmount;

      if (remainingAmount <= 0) {
        break; // No need to adjust further
      }
    }
    repaymentRepository.saveAll(remainingRepayments);
  }

  /**
   * Validates if the loan is eligible for repayment.
   * This method checks if the loan status allows repayment and if the payment amount is valid.
   * @param loan The loan to be validated.
   * @param request The repayment request containing payment details.
   */
  private void validateLoanForRepayment(Loan loan, PayEwiRequest request) {
    if (loan.getStatus() == LoanStatus.PAID) {
      throw new LoanAlreadyPaidException(loan.getDateClosed());
    }
    if (loan.getStatus() == LoanStatus.REJECTED) {
      throw new LoanRejectedException();
    }
    if (loan.getStatus() == LoanStatus.PENDING) {
      throw new LoanPendingApprovalException();
    }
    if(request.getEwiAmount()> loan.getPendingAmount()){
      throw new EmiOverflowException();
    }
  }

  /**
   * Retrieves all loans associated with the authenticated user.
   * @return A list of LoanDTO objects representing the loans.
   */

  public List<LoanDTO> getAllLoans() {

    List<Loan> allLoans =
        (loanRepository.findAllByUsername(
            SecurityContextHolder.getContext().getAuthentication().getName()));

    return allLoans.isEmpty() ? List.of() : LoanMapper(allLoans);
  }
  /**
   * Maps a list of Loan entities to a list of LoanDTO objects.
   * @param allLoans The list of Loan entities to be mapped.
   * @return A list of LoanDTO objects representing the loans.
   */
  private List<LoanDTO> LoanMapper(List<Loan> allLoans) {
    return allLoans.stream()
        .map(
            loan -> {
              LoanDTO loanDTO =
                  LoanDTO.builder()
                      .amount(loan.getAmount())
                      .loanStatus(loan.getStatus())
                      .term(loan.getTerm())
                      .dueTerm(loan.getDueTerm())
                      .weeklyEMI(loan.getWeeklyEmi())
                      .remainingAmount(loan.getPendingAmount())
                      .nextDueDate(loan.getNextDueDate())
                      .build();
              return loanDTO;
            })
        .collect(Collectors.toList());
  }
}

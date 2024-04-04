package com.example.demo.controller;

import com.example.demo.model.loan.ApproveLoanRequest;
import com.example.demo.model.loan.CreateLoanRequest;
import com.example.demo.model.loan.LoanDTO;
import com.example.demo.model.loan.PayEwiRequest;
import com.example.demo.service.LoanService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

/**
 * Controller class for managing loan-related operations.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/loan")
public class LoanController {

  @Autowired
  private final LoanService loanService;


  /**
   * Endpoint for creating a new loan.
   *
   * @param request The request containing information to create a new loan.
   * @return ResponseEntity indicating the result of the operation.
   */

  @PostMapping(path = "/create")
  public ResponseEntity<String> createLoan(@RequestBody @Valid CreateLoanRequest request) {
    return loanService.save(request);
  }


  /**
   * Endpoint for approving a loan.
   *
   * @param request The request containing information to approve a loan.
   * @return ResponseEntity indicating the result of the operation.
   * @throws BadRequest If the request is malformed or invalid.
   */
  @PutMapping(path = "/approve")
  public ResponseEntity<String> approveLoan(@RequestBody @Valid ApproveLoanRequest request)
      throws BadRequest {
    return loanService.approve(request);

  }

  /**
   * Endpoint for repaying EWI (Equal Weekly Instalments) for a loan.
   *
   * @param request The request containing information to repay EWI for a loan.
   * @return ResponseEntity indicating the result of the operation.
   * @throws BadRequest If the request is malformed or invalid.
   */

  @PostMapping(path = "/ewi")
  public ResponseEntity<String> repayEWI(@RequestBody @Valid PayEwiRequest request)
      throws BadRequest {
    return loanService.repayEwi(request);
  }


  /**
   * Retrieves all loans associated with the current user.
   *
   * @return ResponseEntity containing a list of LoanDTO representing all loans for the current user.
   */
  @GetMapping(path ="/all")
  public ResponseEntity<List<LoanDTO>> allLoansByUser(){
    List<LoanDTO> loans = loanService.getAllLoans();
    return ResponseEntity.ok().body(loans);
  }

}

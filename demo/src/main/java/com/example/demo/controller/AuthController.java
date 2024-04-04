package com.example.demo.controller;


import com.example.demo.model.authorization.JwtAuthRequest;
import com.example.demo.model.authorization.JwtRegisterRequest;
import com.example.demo.model.exceptions.UserAlreadyExistsException;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  @Autowired
  private AuthService authService;


  /**
   * Endpoint for user registration (signup).
   *
   * @param authenticationRequest The request containing user registration information.
   * @return ResponseEntity containing the result of the signup operation.
   * @throws Exception If an error occurs during the signup process.
   */
  @RequestMapping(value = "/signup", method = RequestMethod.POST)
  public ResponseEntity<?> signup(@RequestBody JwtRegisterRequest authenticationRequest) throws Exception, UserAlreadyExistsException {


    return ResponseEntity.ok((authService.signup(authenticationRequest)));
  }


  /**
   * Endpoint for user authentication (signin).
   *
   * @param authenticationRequest The request containing user authentication information.
   * @return ResponseEntity containing the result of the signin operation.
   * @throws Exception If an error occurs during the signin process.
   */

  @RequestMapping(value = "/signin", method = RequestMethod.POST)
  public ResponseEntity<?> signin(@RequestBody JwtAuthRequest authenticationRequest) throws Exception {
    return ResponseEntity.ok(authService.signin(authenticationRequest));
  }

}

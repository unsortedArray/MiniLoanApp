package com.example.demo.controller;


import com.example.demo.service.TestService;
import javax.security.auth.login.LoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
  @Autowired
  TestService testService;
  @GetMapping(path = "/test")
  public ResponseEntity<?> testFunction() throws LoginException{
    return ResponseEntity.ok(testService.checkValidUser());
  }
}


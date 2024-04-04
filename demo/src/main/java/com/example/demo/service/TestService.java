package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
  private UserRepository userRepository;

  public ResponseEntity<String> checkValidUser() {
    Object userRoles = SecurityContextHolder.getContext().getAuthentication().getDetails();
    return ResponseEntity.ok().body(getUsername());
  }

  public static String getUsername() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}

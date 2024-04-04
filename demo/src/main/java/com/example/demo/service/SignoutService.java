package com.example.demo.service;

import com.example.demo.database.Token;
import com.example.demo.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignoutService implements LogoutHandler {
  private final TokenRepository tokenRepository;

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    jwt = authHeader.substring(7);
    Optional<Token> storedToken = tokenRepository.findByToken(jwt);
    if (storedToken.isPresent()) {
      Token token = storedToken.get();
      token.setExpired(true);
      tokenRepository.save(token);
      SecurityContextHolder.clearContext();
    }
  }
}

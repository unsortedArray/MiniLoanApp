package com.example.demo.configs;

import com.example.demo.repository.TokenRepository;
import com.example.demo.service.JwtDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

  private final HandlerExceptionResolver handlerExceptionResolver;

  private final JwtDetailService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    if (request.getServletPath().contains("api/v1/auth/")) {
      filterChain.doFilter(request, response);
      return;
    }
    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set status code 401 (Unauthorized)
      response.getWriter().write("Unauthorized: Missing or invalid token");
      return;
    }

    final String jwt = authHeader.substring(7);
    final String username = jwtService.extractUsername(jwt);

    if (username == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set status code 401 (Unauthorized)
      response.getWriter().write("Unauthorized: Invalid token");
      return;
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
      if (userDetails == null) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set status code 401 (Unauthorized)
        response.getWriter().write("Unauthorized: User not found");
        return;
      }

      boolean isTokenValid =
          tokenRepository.findByToken(jwt).map(t -> !t.isExpired()).orElse(false);

      if (!isTokenValid || !jwtService.isTokenValid(jwt, userDetails)) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Set status code 403 (Forbidden)
        response.getWriter().write("Forbidden: Invalid credentials");
        return;
      }

      if (jwtService.isTokenValid(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}

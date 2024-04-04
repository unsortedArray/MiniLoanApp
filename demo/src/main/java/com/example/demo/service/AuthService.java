package com.example.demo.service;

import com.example.demo.database.Token;
import com.example.demo.database.User;
import com.example.demo.model.authorization.JwtAuthRequest;
import com.example.demo.model.authorization.JwtAuthResponse;
import com.example.demo.model.authorization.JwtRegisterRequest;
import com.example.demo.repository.TokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.enums.TokenTypes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user authentication and token management.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;

  private final PasswordEncoder passwordEncoder;

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtDetailService jwtTokenUtil;

  /**
   * Registers a new user and generates an access token.
   *
   * @param request The registration request containing user details.
   * @return The generated access token.
   */

  public JwtAuthResponse signup(JwtRegisterRequest request) {

    User newUser =
        User.builder()
            .username(request.getUsername())
            .firstName(request.getFirstname())
            .lastName(request.getLastname())
            .role(request.getRole())
            .password(passwordEncoder.encode(request.getPassword()))
            .build();

    User savedUser = userRepository.save(newUser);
    String accessToken = jwtTokenUtil.generateToken(savedUser);
    addUserToken(savedUser, accessToken);

    return JwtAuthResponse.builder().accessToken(accessToken).build();
  }


  /**
   * Adds a new token for the given user.
   *
   * @param user  The user for whom the token is generated.
   * @param token The access token to be added.
   */

  public void addUserToken(User user, String token) {
    Token newToken =
        Token.builder()
            .tokenType(TokenTypes.BEARER)
            .token(token)
            .user(user)
            .expired(false)
            .expired(false)
            .build();

    tokenRepository.save(newToken);
  }

  /**
   * Authenticates a user and generates a new access token.
   *
   * @param jwtAuthRequest The authentication request containing user credentials.
   * @return The generated access token.
   */

  public JwtAuthResponse signin(JwtAuthRequest jwtAuthRequest) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword()));

    User user = userRepository.findByUsername(jwtAuthRequest.getUsername()).orElseThrow();

    String newToken = jwtTokenUtil.generateToken(user);
    invalidateUserToken(user);
    addUserToken(user, newToken);
    return JwtAuthResponse.builder().accessToken(newToken).build();
  }

  /**
   * Invalidates all valid tokens associated with the given user.
   *
   * @param user The user whose tokens need to be invalidated.
   */
  public void invalidateUserToken(User user) {
    List<Token> validTokens = tokenRepository.findAllValidUserToken(user.getUsername());
    validTokens.forEach(
        token -> {
          token.setExpired(true);
        });
    tokenRepository.saveAll(validTokens);
  }
}

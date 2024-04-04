package com.example.demo.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service class for JWT token generation and validation.
 */
@Service
public class JwtDetailService {

  @Value("${security.jwt.secret-key}")
  private String secretKey;

  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;

  /**
   * Extracts the username from the JWT token.
   *
   * @param token The JWT token.
   * @return The extracted username.
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extracts a specific claim from the JWT token.
   *
   * @param token          The JWT token.
   * @param claimsResolver The function to resolve the claim.
   * @param <T>            The type of the claim.
   * @return The extracted claim.
   */


  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }
  /**
   * Generates a JWT token for the given user details.
   *
   * @param extraClaims    Extra claims to be added to the token.
   * @param userDetails    The user details for whom the token is generated.
   * @return The generated JWT token.
   */
  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails
  ) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  public String generateRefreshToken(
      UserDetails userDetails
  ) {
    return buildToken(new HashMap<>(), userDetails, jwtExpiration);
  }


  /**
   * Builds a JWT token with the provided claims and expiration.
   *
   * @param extraClaims Extra claims to be added to the token.
   * @param userDetails The user details for whom the token is generated.
   * @param expiration  The token expiration time.
   * @return The generated JWT token.
   */
  private String buildToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails,
      long expiration
  ) {
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith( getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Validates whether the JWT token is valid for the given user.
   *
   * @param token       The JWT token.
   * @param userDetails The user details.
   * @return True if the token is valid for the user, otherwise false.
   */
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}

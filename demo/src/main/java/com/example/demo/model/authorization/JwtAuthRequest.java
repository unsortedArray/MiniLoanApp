package com.example.demo.model.authorization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing a JWT authentication request.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthRequest{
  private String username;
  private String password;
}

package com.example.demo.model.authorization;

import com.example.demo.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtRegisterRequest {
  private String firstname;
  private String lastname;
  private String username;
  private String password;
  private UserRoles role;
}

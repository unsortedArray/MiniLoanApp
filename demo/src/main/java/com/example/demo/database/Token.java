package com.example.demo.database;


import com.example.demo.enums.TokenTypes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a token.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table
public class Token {

  @Id
  @GeneratedValue
  public Integer id;

  @Column(unique = true)
  public String token;

  @Enumerated(EnumType.STRING)
  public TokenTypes tokenType;


  public boolean expired;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_username")
  public User user;
}

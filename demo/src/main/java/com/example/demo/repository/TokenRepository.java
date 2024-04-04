package com.example.demo.repository;

import com.example.demo.database.Loan;
import com.example.demo.database.Token;
import com.example.demo.database.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Long> {

  @Query(
      "SELECT t FROM Token t INNER JOIN t.user u WHERE u.username = :username AND t.expired =false")
  List<Token> findAllValidUserToken(String username);

  Optional<Token> findByToken(String token);
}

# Project Documentation

## Project Overview

This project is a web application developed using the Spring Boot framework, aimed at providing a comprehensive loan management solution. It encompasses features such as loan creation, approval, repayment, and user authentication.

## Project Architecture

### Controller Layer (`com.example.demo.controller`):

Responsible for handling incoming HTTP requests and directing them to the appropriate service.
Defines RESTful endpoints for loan-related operations (`create`, `approve`, `ewi`, `all`).
Makes use of Spring annotations like `@RestController`, `@PostMapping`, `@PutMapping`, `@GetMapping`, etc.

### Service Layer (`com.example.demo.service`):

Encompasses the core business logic of the application.
Handles operations pertinent to loan creation, approval, repayment, and user authentication.
Interacts with repository classes for database operations.
Implements robust exception handling mechanisms for various scenarios such as insufficient payment and loan status validation.
Leverages Spring annotations such as `@Service`, `@Autowired`, etc.

### Database Layer (`com.example.demo.database`):

Comprises entity classes representing database entities.
Utilizes JPA annotations for entity-to-table mapping (`@Entity`, `@Table`, `@Id`, `@Column`, etc.).
Establishes relationships between entities using annotations like `@ManyToOne`, `@OneToMany`, etc.
Provides repositories for CRUD operations using Spring Data JPA.

### Model Layer (`com.example.demo.model`):

Houses DTO (Data Transfer Object) classes employed for data interchange across different layers of the application.
Offers request and response models for loan operations (`CreateLoanRequest`, `ApproveLoanRequest`, `PayEwiRequest`, `LoanDTO`, etc.).

### Util Layer (`com.example.demo.util`):

Contains utility classes and constants utilized throughout the application.
Includes utility methods for loan calculations, constants for interest rates, URLs, etc.

### Security Layer (`com.example.demo.security`):

Manages user authentication and authorization.
Deploys Spring Security for securing REST endpoints and managing user sessions.
Integrates JWT (JSON Web Token) authentication using `JwtDetailService` and `JwtAuthenticationFilter`.
Defines security configurations in the `SecurityConfig` class.

### Exception Handling (`com.example.demo.model.exceptions`):

Introduces custom exception classes to manage specific error scenarios.
Exception types such as `EmiOverflowException`, `InsufficientPaymentException`, `LoanNotFoundException`, etc., are employed to communicate error states to clients effectively.

## Choices Made

1. **Spring Boot:** Chosen for its simplicity and productivity benefits, providing a robust framework for building RESTful web services.
2. **Spring Data JPA:** Adopted for its abstraction over data access operations, reducing boilerplate code and enhancing development efficiency.
3. **JWT Authentication:** Implemented for its stateless nature, enhancing security and scalability of the application.
4. **Custom Exceptions:** Utilized to deliver meaningful error messages and handle specific error scenarios gracefully.
5. **Lombok:** Leveraged for reducing boilerplate code and improving code readability, thereby enhancing maintainability.

The project adheres to a layered architecture pattern, promoting separation of concerns and making the application modular, maintainable, and scalable. It leverages popular Spring Boot features and libraries to streamline development and ensure robust security implementation.

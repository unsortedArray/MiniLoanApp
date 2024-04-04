# Loan Management System

## Overview

This project is a web application built using the Spring Boot framework, designed to provide loan management functionality. It includes features such as creating loans, approving loans, making repayments, and user authentication.

## Demo
![ScreenRecording2024-04-04at8 54 32AM-ezgif com-video-to-gif-converter](https://github.com/unsortedArray/MiniLoanApp/assets/27006043/702eb57d-affd-483d-9f47-b5f918f0858c)



## Setup

1. **Clone Repository**:
   ```bash
   git clone <https://github.com/unsortedArray/MiniLoanApp>
   
2. **Requirements**
   1. Java 17+
   2. mysql
3. **Configure Application** 
   Configure your preferred database settings in the `application.properties` file located in the `src/main/resources` directory.  
    1. Add values for following fields in application.properties  
       * spring.datasource.url
       * spring.datasource.username
       * spring.datasource.password
       * application.security.jwt.secret-key=
4. **Build and Run Bash:**
    Build the project using Maven and run it using Spring Boot. Navigate to the project directory and execute:
   ```bash
   mvn clean install
   mvn spring-boot:run
   
5. **Build and Run IntelliJ:**
   1. Open IntelliJ IDEA.
   2. Click on "File" > "Open" and select the project directory.
   3. Wait for IntelliJ to import the project.
   4. Open the `pom.xml` file.
   5. Click on "Import Changes" to load dependencies.
   6. Open the `application.properties` file located in `src/main/resources` mentioned below.
   7. Navigate to the main class (`DemoApplication.java`).
   8. Right-click on the main class.
   9. Select "Run DemoApplication". 


**Presumptions**
1. Application supports simple loans as of now, can be extended simply via changes in const values
2. All the loans will be assumed to have a “weekly” repayment frequency.
3. Another assumption is anyone can pay for loan with valid credentials, creation and approval and access controlled. All flows must be authenticated
4. As soon as loan is approved a schedule is generated the user can pay extra which goes in closing the installments



## Features

**Loan Service**

### Features List

### 1. Creating a New Loan
- **Endpoint:** `/api/v1/loan/create`
- **Description:** Allows all users with the authenticated user to create a new loan by providing loan amount and term.
  - sample request json 
  - ```json
    { 
      "amount": 10000, 
      "term": 12 
    }

### 2. Approving a Loan
- **Endpoint:** `/api/v1/loan/approve`
- **Description:** Enables Admin users to approve a pending loan.
  - sample request json
  - ```json
    {
      "loanId": "123456"
    }
  

### 3. Repaying Equated Weekly Installments (EWI)
- **Endpoint:** `/api/v1/loan/ewi`
- **Description:** Allows users to repay EWI for a loan with the authenticated user.
  - - sample request json
  - ```json
    {
      "loanId": "123456",
      "ewiAmount": 500
    }

### 4. Retrieving All Loans
- **Endpoint:** `/api/v1/loan/all`
- **Description:** Retrieves all loans associated with the authenticated user.



### Features List
**Role Based Access Control**:   All the endpoint are protected via access and auth control

### 1. SignUp : 
**The user can sign up with valid details**

### 2. SignIn:
**The user can sign up with valid credentials can sign in and returned valid token to be used for other requests**

### 3. Sign out:
**The user can sign out marking its token invalid **



***DB Schema*** 


<img width="901" alt="Screenshot 2024-04-04 at 7 24 24 AM" src="https://github.com/unsortedArray/MiniLoanApp/assets/27006043/5e5497d2-4375-4d01-a1cd-e11d77cd206d">




*** Project Arch***

[Design Document](https://github.com/unsortedArray/MiniLoanApp/blob/main/ProjectArchReadMe.md)







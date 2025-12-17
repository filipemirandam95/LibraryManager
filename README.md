# LibraryManager 📚


https://librarymanager-96yy.onrender.com/swagger-ui.html

![Header](https://github.com/filipemirandam95/assets/blob/master/LibraryManager/libHeader.png)
![Auth](https://github.com/filipemirandam95/assets/blob/master/LibraryManager/libauth.png)


## 📌 Project Context & Objectives

This repository serves as a **technical showcase** based on an existing project ("LibraryManager"). The primary goal of this iteration is to demonstrate adherence to **software engineering best practices**, specifically focusing on:

* **Clean Architecture** principles.
* **Git Workflow:** Atomic commits, semantic versioning, and clean issue management.
* **Refactoring:** Code optimizations and dependency updates.

> **⚠️ Infrastructure Note:**
> This application was previously deployed and active on **Render**. However, due to the expiration of the platform's free tier services, the live demo is currently unavailable. The codebase remains fully functional for local deployment via Docker or Maven.
>
> 
# Overview
LibraryManager is a Spring Boot application designed to efficiently manage library operations. It allows users to manage books, authors, loans, and users, ensuring smooth management of library resources. The system includes OAuth2 authentication, book tracking, loan and reservation management, and fine tracking. It uses PostgreSQL as the database and comes with API documentation via Swagger.

This project is also dockerized for easy deployment and is hosted on Render, which provides a CI/CD pipeline, ensuring that updates are automatically deployed when code changes.

# Technologies
- Java
- Spring Boot
- OAuth2 
- Swagger
- H2 Database
- Postgres Database
- JPA / Hibernate
- Maven
- Docker
- CI/CD
  
# Features
- 📚 Manage books, authors, and categories  
- 📝 Track loans, returns, and fines  
- 👤 User registration and authentication  
- 📅 Book reservation system with status management via Scheduler   
- 📖 API documentation via **Swagger UI**  
- 🐘 PostgreSQL database integration  
- 🐳 Dockerized for local and production deployment  
- ⚡ Hosted on **Render** with **CI/CD** 

# How to run the project
 Requires Java 21
```bash
# clone repository
git clone https://github.com/filipemirandam95/LibraryManager.git
# acess the back-end folder
cd backend

# runs the project
./mvnw spring-boot:run
```

# Domain Class Diagram

```mermaid
classDiagram
    class User {
        Long id
        String name
        String email
        String password
        LocalDate birthDate   
    }

  class Role {
      Long id
      String authority

  } 
    class Book {
        Long id
        String title
        String isbn
        String edition
        int totalCopies
        int availableCopies
    }
    
    class Author {
        Long id
        String name
        LocalDate birthDate
    }
    
    class Loan {
        Long loanId
        LocalDate loanDate
        LocalDate returnDate
    }
    
    class Category {
        Long id
        String name
    }

    class ReservationStatus {
      int CONFIRMED
      int CANCELLED
      int COMPLETED
    }
    class Reservation {
        Long id
        LocalDate reservationDate
        ReservationStatus status
    }
    
    class Fine {
        Long id
        BigDecimal amount
        Instant appliedAt
        Instant paidAt
        Boolean paid
    }

    User "1" --> "*" Loan : has
    User "*" --> "*" Role : has
    Book "1" --> "*" Loan : has
    Author "*" --> "*" Book : writes
    Category "*" --> "*" Book : categorizes
    User "1" --> "*" Reservation : makes
    Loan "1" --> "1" Fine : generates
    Book "1" --> "*" Reservation : is reserved in
    Reservation "1" --> "1" ReservationStatus : has
```
# Author
Filipe Miranda Maduro
https://www.linkedin.com/in/filipemirandamaduro/

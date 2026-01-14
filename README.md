# MyMealMeter (MMM)

MyMealMeter is a community-driven Spring Boot MVC web application that allows users to create and share meals, track daily calorie intake, and discover popular dishes through a leaderboard system.  
The project demonstrates real-world backend architecture, Spring Security, database persistence, and admin moderation features.

---

## Overview

The goal of MyMealMeter is to combine:
- Social interaction around food
- Nutritional awareness
- Moderation and administration tools

The application is **server-rendered (MVC + Thymeleaf)** and **not a REST API**.

It is designed as a complete, production-style web application similar to what would be built in a real company environment.

---

## Features

### User Features
- User registration and authentication
- Create, edit, and delete meal catalogs
- Add meals with detailed nutritional values (proteins, carbs, fats, calories)
- View meal details
- Comment on meals
- Upvote meals
- Add meals to favourites
- View a global leaderboard (Top 20 meals by votes)
- Daily calorie calculator based on personal metrics
- Daily meal tracker to compare consumed calories vs target
- Report inappropriate content or behavior

### Admin Features
- User management (view users, change roles, delete users)
- Meal catalog moderation
- Meal moderation
- Comment moderation
- Report review system (reviewed / not reviewed)
- Application statistics dashboards:
  - users
  - meals
  - comments
  - upvotes
  - reports
  - time-based activity tracking

---

## User Roles

| Role  | Description |
|------:|-------------|
| USER  | Standard application usage |
| ADMIN | Full moderation and admin panel access |

Role-based access control is enforced using Spring Security.

---

## Application Architecture

The application follows a classic layered architecture:

- **Controller layer**  
  Handles HTTP requests, form submissions, and view rendering.

- **Service layer**  
  Contains business logic, validation, and core application rules.

- **Persistence layer**  
  Uses Spring Data JPA and Hibernate for database interaction.

- **View layer**  
  Thymeleaf templates for server-side rendering.

- **Exception handling**  
  Centralized using `@ControllerAdvice` for validation, domain, and business errors.

---

## Technology Stack

- Java 21+
- Spring Boot 4.x
- Spring MVC
- Thymeleaf
- Spring Security
- Spring Data JPA (Hibernate)
- Jakarta Bean Validation
- Maven

### Databases
- MySQL (local development)
- Azure SQL / SQL Server (production-ready)

---

## Application Endpoints (High-Level)

> The application uses MVC controllers returning views and redirects.

### Public & Authentication
- `/` – landing page
- `/register` – user registration
- `/login` – login page
- `/logout` – logout
- `/info` – application info, FAQ, rules, contacts

### User Area
- `/home` – main authenticated page
- `/users/{id}/profile` – edit user profile
- `/users/delete_profile` – delete own account
- `/users/winner` – winner display

### Meal Catalogs & Meals
- `/meal_catalogs/**` – catalogs, meals, leaderboard, favourites
- Catalog creation and editing
- Meal creation, voting, commenting, favouriting

### Calculator & Tracking
- `/calculator/**` – calorie calculator and results
- `/calculator/did_you_eat_enough_today` – daily meal tracker

### Reports
- `/reports/**` – create and view user reports

### Admin Panel (ADMIN only)
- `/admin_panel/**` – moderation, management, statistics dashboards

---

## Configuration & Environments

The application is configured to support **multiple environments**:
- Local development
- Cloud deployment (Azure)

This is achieved through:
- Environment-specific `application.properties`
- Database-specific drivers and Hibernate dialects
- Maven dependency configuration

---

## Local Development Configuration

Local development uses **MySQL** as the database.

### Key characteristics
- MySQL JDBC driver
- MySQL Hibernate dialect
- Automatic schema updates
- Optimized for fast local iteration

The local configuration enables:
- Hidden HTTP method support (`PUT`, `PATCH`, `DELETE`)
- Form-based authentication
- Session-based security

---

## Production / Deployment Configuration

Production deployment targets **Azure SQL (SQL Server)**.

### Key characteristics
- SQL Server JDBC driver
- SQL Server Hibernate dialect
- Secure connection with encryption
- Credentials supplied via environment variables

This setup allows:
- Cloud-native deployment (Azure App Service)
- Secure database access
- Environment-specific secrets
- Zero code changes between environments

---

## Maven Configuration (pom.xml)

The project uses Maven for dependency and build management.

### Key dependencies
- Spring Boot starters:
  - Web
  - Thymeleaf
  - Security
  - Data JPA
  - Validation
  - Cache
  - Actuator
- Database drivers (runtime only):
  - MySQL Connector/J
  - Microsoft SQL Server JDBC
- Spring Cloud OpenFeign
- Testing:
  - Spring Boot Test
  - Spring Security Test

The project supports:
- Java 21+
- Spring Boot 4.x
- Spring Cloud 2025.x BOM

---

## Security

- Authentication and authorization via Spring Security
- Session-based login
- Role-based access control
- Admin-only routes protected
- Validation and error handling at controller and service levels

---

## Moderation & Reporting

MyMealMeter includes a built-in moderation workflow:
- Users submit reports for inappropriate content
- Reports are categorized as reviewed / not reviewed
- Admins review and resolve reports
- Admins can delete content or users
- All moderation actions are visible in the admin panel

---

## Error Handling

Global error handling is implemented using `@ControllerAdvice`.

Handled cases include:
- Duplicate usernames or emails
- Password mismatch during registration
- Duplicate upvotes or favourites
- Invalid form input (dates, fields)
- Domain-specific business errors

Critical errors redirect to a generic error page.

---

## Purpose of the Project

This project was built to demonstrate:
- Full-stack Spring Boot MVC development
- Clean layered architecture
- Realistic business logic
- Security and role management
- Database-driven applications
- Admin tooling and moderation systems
- Cloud-ready configuration

It is suitable as a **portfolio project** and reflects skills expected in a **junior to mid-level Java backend developer** role.

---

## Author

**Kaloyan Dimitrov**  
Email: luckykaloyan@gmail.com

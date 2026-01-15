# MyMealMeter (MMM)

MyMealMeter is a community-driven Spring Boot MVC web application that allows users to create and share meals, track daily calorie intake, and discover popular dishes through a leaderboard system.  
The project demonstrates real-world backend architecture, Spring Security, database persistence, and admin moderation features.

---

## Quick Navigation

[Overview](#overview) •
[Features](#features) •
[User Roles](#user-roles) •
[Architecture](#application-architecture) •
[Technology Stack](#technology-stack) •
[Endpoints](#application-endpoints-high-level) •
[Configuration](#configuration--environments) •
[Security](#security) •
[Moderation](#moderation--reporting) •
[Purpose](#purpose-of-the-project)

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
- Application statistics dashboards<br>
[↑ Back to Top](#quick-navigation)

---

## User Roles

| Role  | Description |
|------:|-------------|
| USER  | Standard application usage |
| ADMIN | Full moderation and admin panel access |

<br>
[↑ Back to Top](#quick-navigation)
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
  Centralized using `@ControllerAdvice`.<br>
[↑ Back to Top](#quick-navigation)

---

## Technology Stack

- **Java 25** (local development)
- Java 21+ (baseline compatibility)
- Spring Boot 4.x
- Spring MVC
- Thymeleaf
- Spring Security
- Spring Data JPA (Hibernate)
- Jakarta Bean Validation
- Maven

### Databases
- MySQL (local development)
- Azure SQL / SQL Server (production-ready)<br>
[↑ Back to Top](#quick-navigation)

---

## Application Endpoints (High-Level)

### Public & Authentication
- `/`
- `/register`
- `/login`
- `/logout`
- `/info`

### User Area
- `/home`
- `/users/{id}/profile`
- `/users/delete_profile`
- `/users/winner`

### Meal Catalogs & Meals
- `/meal_catalogs/**`
- Catalog and meal management
- Voting, commenting, favourites
- Leaderboard

### Calculator & Tracking
- `/calculator/**`
- `/calculator/did_you_eat_enough_today`

### Reports
- `/reports/**`

### Admin Panel (ADMIN only)
- `/admin_panel/**`<br>
[↑ Back to Top](#quick-navigation)

---

## Configuration & Environments

The application supports:
- Local development
- Cloud deployment (Azure)

Environment separation is achieved through:
- Environment-specific `application.properties`
- Database-specific JDBC drivers
- Hibernate dialect switching
- Maven dependency configuration<br>
[↑ Back to Top](#quick-navigation)

---

## Local Development Configuration

- Runs with **Java 25**
- Uses MySQL
- Automatic schema updates
- Hidden HTTP method support (`PUT`, `PATCH`, `DELETE`)
- Session-based authentication<br>
[↑ Back to Top](#quick-navigation)

---

## Production / Deployment Configuration

- Targets **Azure SQL (SQL Server)**
- Encrypted database connections
- Credentials provided via environment variables
- Cloud-ready without code changes<br>
[↑ Back to Top](#quick-navigation)

---

## Maven Configuration (pom.xml)

- Spring Boot parent (4.x)
- Spring Cloud BOM (2025.x)
- Runtime DB drivers (MySQL + SQL Server)
- Devtools for local development
- Test dependencies isolated from production<br>
[↑ Back to Top](#quick-navigation)

---

## Security

- Spring Security authentication & authorization
- Session-based login
- Role-based access control
- Admin-only routes protected<br>
[↑ Back to Top](#quick-navigation)

---

## Moderation & Reporting

- User-generated reports
- Admin review workflow
- Content and user moderation
- Full visibility via admin dashboards<br>
[↑ Back to Top](#quick-navigation)

---

## Error Handling

- Centralized using `@ControllerAdvice`
- Validation and domain errors handled gracefully
- Redirects with feedback messages
- Generic error page for critical failures<br>
[↑ Back to Top](#quick-navigation)

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

It is suitable as a **portfolio project** and reflects skills expected in a **junior to mid-level Java backend developer** role.<br>
[↑ Back to Top](#quick-navigation)

---

## Author

**Kaloyan Dimitrov**  
Email: kaloyandimitrov1995@gmail.com

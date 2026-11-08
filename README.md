# Finance Tracker

A multi-user personal finance management REST API built with **Java 21 and Spring Boot**. The application enables users to manage their income, expenses, categories, budgets, and financial transactions through secure, user-specific APIs.

It includes JWT-based authentication, budget notifications, monthly financial reports, scheduled alert processing, and MySQL persistence.

## Features

### Authentication & Security
- User registration and login with JWT-based authentication.
- Secure password hashing using BCrypt.
- User-specific data access and isolation.
- Protected API endpoints using Spring Security.

### Financial Management
- **Expense Management:** Create, retrieve, update, and delete expenses.
- **Income Management:** Manage income records through REST APIs.
- **Category Management:** Organize financial records using categories.
- **Budget Management:** Create and manage monthly budgets.
- **Transaction Management:** Track financial transactions associated with income and expense operations.

### Reports & Notifications
- Generate monthly financial reports.
- Monitor spending against monthly budgets.
- Automatic budget warning and exceeded notifications.
- Scheduled processing of budget alerts.

### Development & Testing
- Layered backend architecture using Controller, Service, and Repository components.
- MySQL persistence using Spring Data JPA and Hibernate.
- Unit testing with JUnit 5 and Mockito.
- Docker and Docker Compose support.
- Containerized deployment on Amazon ECS.

## Tech Stack

| Category | Technologies |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1.1 |
| Security | Spring Security, JWT, BCrypt |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL 8 |
| Testing | JUnit 5, Mockito |
| Build Tool | Maven |
| Containerization | Docker, Docker Compose |
| Cloud | Amazon ECS |

## Application Architecture

The application follows a layered architecture to separate request handling, business logic, data access, and security responsibilities.

```text
Client
  |
  v
REST Controllers
  |
  v
Service Layer
  |
  v
Repository Layer
  |
  v
MySQL Database
```

- **Controller Layer:** Handles incoming HTTP requests and API responses.
- **Service Layer:** Contains application business logic and financial operations.
- **Repository Layer:** Handles database operations through Spring Data JPA.
- **Security Layer:** Handles JWT authentication and protected resource access.

## Project Structure

```text
src/main/java/com/syed/finance_tracker/
├── Controller/
├── Service/
├── Repository/
├── Dto/
├── Security/
├── config/
└── entity/
```

## Main API Endpoints

| Module | Endpoint |
|---|---|
| User Registration | `POST /users` |
| Authentication | `POST /auth/login` |
| Expenses | `/expenses` |
| Income | `/income` |
| Categories | `/categories` |
| Budgets | `/budgets` |
| Transactions | `/transactions` |
| Notifications | `/notifications` |
| Reports | `/reports` |

Protected endpoints require a valid JWT access token.

## Getting Started

### Prerequisites

- Java 21
- MySQL 8
- Docker Desktop (optional)

The project includes Maven Wrapper scripts, so a separate Maven installation is not required.

### 1. Clone the Repository

```bash
git clone https://github.com/Syed-173/finance-tracker.git
cd finance-tracker
```

### 2. Configure Environment Variables

Configure the following environment variables for your database and JWT authentication:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
JWT_SECRET
```

Use your own local database credentials and a secure JWT secret. Do not commit actual credentials or secrets to the repository.

### 3. Run the Application

**Windows PowerShell:**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**

```bash
./mvnw spring-boot:run
```

The application runs at:

```text
http://localhost:8080
```

Make sure your MySQL database is running and the datasource configuration is correct before starting the application.

## Running with Docker

Make sure Docker Desktop is running and the required environment variables are configured in your `.env` file.

Build and start the containers:

```bash
docker compose up -d --build
```

Check running containers:

```bash
docker compose ps
```

Stop the containers:

```bash
docker compose down
```

The application and MySQL are configured as separate containers.

## Running Tests

Execute the test suite using the Maven Wrapper:

**Windows:**

```powershell
.\mvnw.cmd test
```

**Linux / macOS:**

```bash
./mvnw test
```

The project uses JUnit 5 and Mockito for unit testing of core service-layer functionality.

## AWS Deployment

The application was deployed on **Amazon Elastic Container Service (ECS)** using Docker containers.

- The Spring Boot application and MySQL database were deployed as separate containers.
- Docker was used to package the application.
- Amazon ECS was used to run the containerized services.
- The deployment environment is not kept running continuously to avoid ongoing AWS costs.

## Future Improvements

- Introduce a CI/CD pipeline.
- Improve API documentation with OpenAPI/Swagger.
- Strengthen production database configuration.
- Expand integration testing.
- Improve application monitoring and logging.

## Author

**Syed Sohaib**

- [GitHub](https://github.com/Syed-173)
- [LinkedIn](https://linkedin.com/in/ssohaib1)

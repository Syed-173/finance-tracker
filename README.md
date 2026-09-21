# Personal Finance Tracker

A backend REST API for managing personal finances, built with Java and Spring Boot. The application supports secure JWT authentication, expense and income tracking, budgeting, notifications, reports, and transaction management.

## Features

* User registration and JWT-based authentication
* Secure password hashing with BCrypt
* User-specific data isolation
* Expense CRUD operations
* Income CRUD operations
* Category management
* Monthly budgets
* Automatic budget warning and exceeded notifications
* Transaction tracking
* Monthly financial reports
* Scheduled budget alert processing
* RESTful APIs
* Unit testing with JUnit and Mockito
* Docker and Docker Compose support
* MySQL database

## Tech Stack

* **Java 21**
* **Spring Boot 4**
* **Spring Security**
* **JWT**
* **Spring Data JPA / Hibernate**
* **MySQL 8**
* **Maven**
* **JUnit 5**
* **Mockito**
* **Docker / Docker Compose**
* **Git / GitHub**

## Project Structure

```text
src/main/java/com/syed/finance_tracker
├── Controller
├── Service
├── Repository
├── Dto
├── Security
├── config
└── entity
```

## Authentication

The API uses JWT-based authentication.

Typical flow:

```text
Register
   ↓
Login
   ↓
JWT Token
   ↓
Authenticated API Requests
```

Protected endpoints require the JWT token in the request:

```text
Authorization: Bearer <token>
```

## Main API Areas

| Area              | Endpoint           |
| ----------------- | ------------------ |
| User Registration | `POST /users`      |
| Login             | `POST /auth/login` |
| Expenses          | `/expenses`        |
| Income            | `/income`          |
| Categories        | `/categories`      |
| Budgets           | `/budgets`         |
| Transactions      | `/transactions`    |
| Notifications     | `/notifications`   |
| Reports           | `/reports`         |

## Running Locally

### Prerequisites

* Java 21
* Maven
* MySQL 8
* Docker Desktop (optional)

### Configuration

Sensitive configuration is supplied through environment variables.

Example:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
JWT_SECRET
```

Do not commit `.env` or other files containing real credentials.

### Run with Maven

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## Running with Docker

Make sure Docker Desktop is running and configure the required environment variables in `.env`.

Then:

```powershell
docker compose up -d --build
```

Check the containers:

```powershell
docker compose ps
```

Stop the application:

```powershell
docker compose down
```

## AWS Deployment

The application was deployed on **Amazon ECS** using containerized services.

* Spring Boot application and MySQL were deployed as separate containers.
* Docker was used to build and package the application.
* ECS was used to run the containerized application.
* The deployment environment is not kept running continuously to avoid ongoing AWS costs.

## Testing

The project includes unit tests for the main service layers using JUnit 5 and Mockito.

Run the tests with:

```powershell
.\mvnw.cmd test
```

## Database

The application uses MySQL with Spring Data JPA and Hibernate.

For local development, the database schema is automatically updated using:

```text
spring.jpa.hibernate.ddl-auto=update
```

## Security

* Passwords are stored using BCrypt hashing.
* JWT authentication protects application endpoints.
* Users can access only their own financial data.
* Database credentials and JWT secrets are provided through environment variables.

## Future Improvements

* Production deployment on AWS
* CI/CD pipeline
* Improved API documentation with OpenAPI/Swagger
* Production database configuration
* Monitoring and logging improvements
* Additional integration tests

## Author

**Syed Sohaib**

B.E. Computer Science
Hyderabad, India

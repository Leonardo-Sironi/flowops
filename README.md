# FlowOps

![Java](https://img.shields.io/badge/Java-21+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)
![JPA](https://img.shields.io/badge/Spring%20Data-JPA-green)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-yellow)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-red)
![JUnit](https://img.shields.io/badge/JUnit-5-green)
![Mockito](https://img.shields.io/badge/Mockito-Testing-blue)
![Tests](https://img.shields.io/badge/Tests-35%20Passing-brightgreen)

FlowOps is a **Business Process Automation and Workflow Management Platform** developed with Java, Spring Boot, PostgreSQL, and modern backend development practices.

The project focuses on building a structured and maintainable backend application, applying object-oriented programming, REST API design, relational database persistence, automated testing, and layered architecture.

FlowOps is being developed incrementally, evolving from a pure Java application into a production-oriented backend system.

---

## Problem

Many companies still manage internal processes using spreadsheets, emails, messaging applications, and manual controls.

This can result in:

- Delayed tasks
- Forgotten responsibilities
- Duplicated work
- Lack of visibility over deadlines
- Difficulty tracking task ownership
- Inefficient workflow management

FlowOps aims to centralize these processes into a structured platform for task management, employee assignments, and business workflow automation.

---

## Current Features

The current version supports:

- Create tasks
- List tasks
- Find tasks by ID
- Assign employees to tasks
- Start tasks
- Complete tasks
- Cancel tasks
- Approve task reviews
- Reject task reviews
- Create employees
- Validate API requests
- Handle business and resource errors
- Persist data using PostgreSQL
- Manage database migrations with Flyway
- Execute automated tests

---

## Architecture

FlowOps follows a layered architecture with clear separation of responsibilities.

```text
HTTP Request
     |
     v
Controller
     |
     v
Service
     |
     v
Repository
     |
     v
Spring Data JPA
     |
     v
Hibernate
     |
     v
PostgreSQL
```

### Controller Layer

Responsible for receiving HTTP requests, validating input DTOs, and returning API responses.

### Service Layer

Coordinates application operations, business rules, and transactions.

### Domain Layer

Contains the main entities, enumerations, and business behavior.

### Repository Layer

Provides database access through Spring Data JPA repositories.

### DTO Layer

Controls the data received and returned by the API.

### Exception Handling

Centralizes error handling and maps application exceptions to appropriate HTTP responses.

---

## Technologies

### Backend

- Java 21
- Spring Boot 4.x
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Jakarta Validation

### Database

- PostgreSQL
- Flyway
- SQL

### Testing

- JUnit
- Mockito
- Spring MockMvc
- Spring Boot Test
- PostgreSQL integration testing

### Development Tools

- Maven
- Git
- GitHub
- IntelliJ IDEA
- pgAdmin

---

## Domain Model

### Task

Represents an activity managed by the platform.

Main attributes:

- ID
- Title
- Description
- Deadline
- Priority
- Current status
- Review status
- Assigned employee

### Employee

Represents an employee who can be assigned to tasks.

Main attributes:

- ID
- Name
- Email

### Business Rules

FlowOps implements rules such as:

- Tasks must have an assigned employee before being started.
- Only tasks in progress can be completed.
- Completed tasks enter a pending review state.
- Task reviews can be approved or rejected.
- Completed tasks cannot be canceled.
- Canceled or completed tasks cannot be reassigned.
- Overdue tasks are identified based on their deadlines and current status.

These rules are implemented in the domain model.

---

## Persistence

The initial version of FlowOps stored tasks only in application memory.

```text
TaskService
    |
    v
List<Task>
    |
    v
Application Memory
```

The current implementation uses persistent storage with PostgreSQL.

```text
TaskService
    |
    v
TaskRepository
    |
    v
Spring Data JPA
    |
    v
Hibernate
    |
    v
PostgreSQL
```

Spring Data JPA provides repository abstractions, while Hibernate handles object-relational mapping and entity persistence.

---

## Entity Relationships

A task can be assigned to one employee, while an employee can be responsible for multiple tasks.

This relationship is represented by `@ManyToOne`.

```text
Task 1 ----|
Task 2 ----|----> Employee
Task 3 ----|
```

The database uses a foreign key:

```text
task.employee_in_charge_id
             |
             v
         employee.id
```

This relationship maintains referential integrity between tasks and employees.

---

## REST API

### Task Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /tasks | Retrieve all tasks |
| GET | /tasks/{id} | Retrieve task by ID |
| POST | /tasks | Create a new task |
| PATCH | /tasks/{id}/assign/{employeeId} | Assign employee |
| PATCH | /tasks/{id}/start | Start task |
| PATCH | /tasks/{id}/complete | Complete task |
| PATCH | /tasks/{id}/approve | Approve review |
| PATCH | /tasks/{id}/reject | Reject review |
| PATCH | /tasks/{id}/cancel | Cancel task |

### Employee Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /employees | Create employee |

### Create Task Example

Request:

```http
POST /tasks
Content-Type: application/json
```

Body:

```json
{
  "title": "Implement automated tests",
  "description": "Create JUnit and Mockito tests",
  "deadline": "2026-12-20",
  "priority": "HIGH"
}
```

### Create Employee Example

```http
POST /employees
Content-Type: application/json
```

Body:

```json
{
  "name": "Example Employee",
  "email": "employee@example.com"
}
```

---

## Validation

FlowOps uses Jakarta Validation to validate incoming API requests.

Validation annotations include:

- `@NotBlank`
- `@NotNull`
- `@Email`
- `@Valid`

Invalid requests return HTTP `400 Bad Request`.

---

## Exception Handling

The application uses centralized exception handling through `@RestControllerAdvice`.

Examples:

| Exception | HTTP Status |
|-----------|-------------|
| TaskNotFoundException | 404 Not Found |
| EmployeeNotFoundException | 404 Not Found |
| InvalidTaskOperationException | 400 Bad Request |
| MethodArgumentNotValidException | 400 Bad Request |

This approach separates error handling from business logic.

---

## Transactions

FlowOps uses `@Transactional` for operations that modify existing persistent entities.

Hibernate tracks changes to managed entities through dirty checking.

```text
Start Transaction
       |
       v
Load Entity
       |
       v
Modify Entity
       |
       v
Hibernate Dirty Checking
       |
       v
Database UPDATE
       |
       v
Commit
```

This allows persistent entities to be updated without explicitly calling `save()` after every modification.

---

## Database Migrations

Flyway manages database schema evolution through versioned SQL migrations.

Current migrations:

```text
src/main/resources/db/migration/

V1__create_initial_schema.sql
V2__add_task_indexes.sql
```

### V1 - Initial Schema

Creates the main database tables:

- employee
- task

Includes primary keys, constraints, and the relationship between tasks and employees.

### V2 - Database Indexes

Adds indexes for:

- Assigned employee
- Task status
- Deadline

These indexes support common query patterns, with the usual storage and write-performance tradeoffs.

### Schema Validation

Hibernate validates the existing database schema instead of modifying it automatically.

```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false
```

Flyway is responsible for structural changes, while Hibernate manages entity persistence.

---

## Automated Testing

FlowOps includes automated tests covering business rules, application services, REST endpoints, and database persistence.

**Current result: 35 tests passing, with 0 failures and 0 errors.**

### Testing Technologies

- JUnit for test execution and assertions
- Mockito for mocking repository dependencies
- Spring MockMvc for testing HTTP endpoints
- Spring Boot Test for integration testing
- PostgreSQL for persistence verification

### Domain Tests

Domain tests verify important business rules, including:

- Initial task status
- Employee assignment
- Task initialization
- Task completion
- Task cancellation
- Review approval
- Review rejection
- Invalid business operations
- Overdue task detection

These tests verify domain behavior independently of the database.

### Service Tests

Service tests use Mockito to simulate repository behavior.

They verify operations such as:

- Retrieving tasks
- Finding tasks by ID
- Handling nonexistent tasks
- Starting tasks
- Assigning employees
- Handling nonexistent employees

This allows service behavior to be tested without connecting to PostgreSQL.

### Controller Tests

Controller tests use Spring MockMvc to verify HTTP behavior.

Covered operations include:

- GET requests
- POST requests
- PATCH requests
- Request validation
- Exception handling
- HTTP status codes

Examples of validated HTTP statuses:

```text
200 OK
201 Created
400 Bad Request
404 Not Found
```

### Integration Testing

An integration test verifies real database persistence.

The test:

1. Creates a new Task.
2. Saves it using TaskRepository.
3. Flushes pending changes to PostgreSQL.
4. Clears the persistence context.
5. Retrieves the Task from the database.
6. Verifies that the persisted data matches the expected values.

The integration test uses `@SpringBootTest` and `@Transactional`.

### Running Tests

From the project root:

Windows:

```powershell
.\mvnw.cmd clean test
```

Linux / macOS:

```bash
./mvnw clean test
```

The integration test requires a configured and accessible PostgreSQL database.

---

## Local Configuration

### Requirements

- Java 21 or later
- PostgreSQL
- Maven Wrapper (included in the project)

### Database

Create a PostgreSQL database named:

```text
flowops
```

Configure the application through environment variables.

| Variable | Description |
|----------|-------------|
| DB_URL | PostgreSQL connection URL |
| DB_USERNAME | Database username |
| DB_PASSWORD | Database password |

Default database URL:

```text
jdbc:postgresql://localhost:5432/flowops
```

Example application properties:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/flowops}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}
```

Database credentials must not be committed to version control.

After configuring the connection, start the application:

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
./mvnw spring-boot:run
```

Flyway automatically applies pending migrations during application startup.

---

## Project Evolution

FlowOps has evolved through multiple development stages.

```text
Java + Object-Oriented Programming
                 |
                 v
Spring Boot REST API
                 |
                 v
DTOs and Validation
                 |
                 v
Global Exception Handling
                 |
                 v
Spring Data JPA
                 |
                 v
Hibernate + PostgreSQL
                 |
                 v
Entity Relationships
                 |
                 v
Transactions
                 |
                 v
Flyway Migrations
                 |
                 v
Automated Testing
```

### Completed Stages

- Java and object-oriented programming
- Spring Boot REST API
- Layered architecture
- DTOs and validation
- Global exception handling
- PostgreSQL persistence
- Spring Data JPA and Hibernate
- Entity relationships
- Transaction management
- Flyway migrations
- Database indexes
- Automated unit tests
- Controller tests
- Persistence integration test

### Planned Evolution

- Spring Security and JWT authentication
- Role-based authorization
- Additional workflow capabilities
- Docker and Docker Compose
- Asynchronous messaging
- CI/CD with GitHub Actions
- Cloud deployment

---

## Learning Objectives

FlowOps is designed to reinforce practical backend development concepts, including:

- Object-oriented programming
- Software architecture
- REST API design
- Dependency injection
- Domain modeling
- Data validation
- Exception handling
- Relational databases
- Object-relational mapping
- Transactions
- Database migrations
- Automated testing
- Dependency mocking
- HTTP testing
- Integration testing

The project prioritizes practical implementation, maintainability, and incremental improvement.

---

## Author

**Leonardo Sironi**

GitHub:
https://github.com/Leonardo-Sironi

LinkedIn:
https://www.linkedin.com/in/leonardo-sironi/
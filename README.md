# FlowOps

![Java](https://img.shields.io/badge/Java-21+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)
![JPA](https://img.shields.io/badge/Spring%20Data-JPA-green)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-yellow)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-red)

FlowOps is a Business Process Automation and Workflow Management Platform built with Java and Spring Boot.

The project is being developed incrementally to apply backend development concepts in a real application, evolving from pure Java and object-oriented programming to a persistent REST API backed by PostgreSQL.

---

## Problem

Many teams still manage internal processes through spreadsheets, emails, messaging apps and manual controls.

This can result in:

- delayed tasks;
- forgotten responsibilities;
- duplicated work;
- lack of visibility over deadlines;
- difficulty tracking task ownership;
- poor visibility over workflow status.

FlowOps aims to centralize these processes in a structured backend application.

---

## Current Features

The current version supports:

- create tasks;
- list tasks;
- find tasks by ID;
- assign employees to tasks;
- start tasks;
- complete tasks;
- cancel tasks;
- approve task reviews;
- reject task reviews;
- create employees;
- validate API requests;
- handle business and resource errors;
- persist application data in PostgreSQL;
- version the database schema with Flyway.

---

## Architecture

FlowOps currently follows a layered architecture:

```text
HTTP Request
    ↓
Controller
    ↓
Service
    ↓
Repository
    ↓
Spring Data JPA / Hibernate
    ↓
PostgreSQL
```

### Responsibilities

**Controller**

Receives HTTP requests and exposes REST endpoints.

**Service**

Coordinates application operations, business flows and transactions.

**Domain**

Contains the main entities and business rules.

**Repository**

Provides database access through Spring Data JPA.

**DTO**

Controls the data received and returned by the API.

**Exception Handler**

Centralizes HTTP error handling.

---

## Technologies

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Flyway
- Jakarta Validation
- Maven
- Git
- GitHub

---

## Persistence

The first version of FlowOps stored tasks only in application memory using a Java `List`.

That meant all data was lost whenever the application restarted.

The project now uses PostgreSQL for persistent storage.

```text
Before

TaskService
    ↓
List<Task>
    ↓
Application memory
```

```text
Current

TaskService
    ↓
TaskRepository
    ↓
Spring Data JPA
    ↓
Hibernate
    ↓
PostgreSQL
```

Spring Data repositories provide database access while Hibernate handles the object-relational mapping between Java entities and database tables.

---

## Entities

### Task

A task contains information such as:

- title;
- description;
- deadline;
- priority;
- current status;
- review status;
- assigned employee.

### Employee

An employee can be assigned as responsible for tasks.

The relationship is modeled with JPA using `@ManyToOne`.

```java
@ManyToOne
private Employee employeeInCharge;
```

In the database, this relationship is represented through a foreign key:

```text
task.employee_in_charge_id
        ↓
employee.id
```

This allows multiple tasks to reference the same employee.

---

## REST API

### Tasks

```http
GET /tasks
```

Returns all tasks.

```http
GET /tasks/{id}
```

Returns a task by ID.

```http
POST /tasks
```

Creates a new task.

Example request:

```json
{
  "title": "Implement persistence",
  "description": "Integrate FlowOps with PostgreSQL",
  "deadline": "2026-10-01",
  "priority": "HIGH"
}
```

Assign an employee:

```http
PATCH /tasks/{taskId}/assign/{employeeId}
```

Start a task:

```http
PATCH /tasks/{id}/start
```

Complete a task:

```http
PATCH /tasks/{id}/complete
```

Approve a task review:

```http
PATCH /tasks/{id}/approve
```

Reject a task review:

```http
PATCH /tasks/{id}/reject
```

Cancel a task:

```http
PATCH /tasks/{id}/cancel
```

### Employees

```http
POST /employees
```

Creates a new employee.

Example request:

```json
{
  "name": "Example Employee",
  "email": "employee@example.com"
}
```

---

## Validation

FlowOps uses Jakarta Validation to validate API input using annotations such as `@NotBlank`, `@NotNull` and `@Email`.

Invalid requests are handled centrally and return HTTP `400 Bad Request`.

---

## Exception Handling

The application uses centralized exception handling with `@RestControllerAdvice`.

Current examples include:

- task not found → `404 Not Found`;
- employee not found → `404 Not Found`;
- invalid task operation → `400 Bad Request`;
- validation errors → `400 Bad Request`.

---

## Transactions

Operations that modify existing entities use `@Transactional`.

Example:

```java
@Transactional
public TaskResponse startTask(Long id) {
    Task task = findTaskById(id);

    task.startTask();

    return toResponse(task);
}
```

Because the entity is managed by Hibernate inside the transaction, Hibernate can detect changes automatically through its dirty checking mechanism.

This means an explicit `save()` is not required for every update to an already managed entity.

---

## Database Migrations

The database schema is versioned using Flyway.

Current migrations:

```text
V1__create_initial_schema.sql
V2__add_task_indexes.sql
```

Flyway is responsible for evolving the database structure.

Hibernate is configured to validate the schema instead of changing it automatically:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

The project also disables Open Session in View:

```properties
spring.jpa.open-in-view=false
```

This keeps persistence access more explicit and avoids database queries being performed during response rendering.

---

## Database Optimization

Indexes were added to fields expected to be frequently queried:

```text
employee_in_charge_id
current_status
deadline
```

These indexes prepare the database for common FlowOps operations such as:

- finding tasks assigned to an employee;
- filtering tasks by status;
- finding tasks based on deadlines.

---

## Project Evolution

FlowOps is being developed in stages.

```text
Java + Object-Oriented Programming
            ↓
Spring Boot REST API
            ↓
DTOs
            ↓
Validation
            ↓
Global Exception Handling
            ↓
Spring Data JPA
            ↓
Hibernate
            ↓
PostgreSQL
            ↓
Entity Relationships
            ↓
Transactions
            ↓
Flyway Migrations
```

### Planned Evolution

```text
Automated Tests
        ↓
Spring Security + JWT
        ↓
Docker
        ↓
Messaging
        ↓
CI/CD
        ↓
Cloud Deployment
```

---

## Learning Goals

FlowOps is also being used as a practical backend development project to reinforce concepts such as:

- object-oriented programming;
- REST API design;
- layered architecture;
- dependency injection;
- DTOs;
- validation;
- exception handling;
- relational databases;
- JPA;
- Hibernate;
- transactions;
- database migrations.

---

## Author

**Leonardo Sironi**

GitHub:  
https://github.com/Leonardo-Sironi

LinkedIn:  
https://www.linkedin.com/in/leonardo-sironi/
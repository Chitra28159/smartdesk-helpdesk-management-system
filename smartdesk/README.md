# SmartDesk – IT Helpdesk Management System

SmartDesk is a backend-focused IT Helpdesk Management System developed using Java 17, Spring Boot, and PostgreSQL.

The application is designed to streamline IT support operations through ticket creation, assignment, status tracking, and user management. It incorporates JWT-based authentication and Spring Security to protect application resources.

This project is being developed from scratch as a practical exploration of backend engineering, REST API development, database design, application security, and software development best practices.

**Project Status:** Actively under development.

---

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot 4.1.2 | Backend application framework |
| Spring Web MVC | REST API development |
| Spring Data JPA | Data persistence |
| Hibernate | ORM implementation |
| Spring Security | Authentication and security |
| JJWT 0.13.0 | JWT generation and validation |
| BCrypt | Password hashing |
| PostgreSQL | Relational database |
| Maven | Dependency management |
| Eclipse IDE | Development environment |
| Postman | API testing |

---

## Key Features

### 1. User Management
- User registration.
- Retrieve all users.
- Retrieve individual users.
- Update user information.
- Delete users.
- DTO-based request and response handling.
- Input validation.

### 2. Ticket Management
- Create support tickets.
- Retrieve tickets.
- Retrieve individual tickets.
- Assign tickets to users.
- Update ticket status.
- Update ticket priority.
- Filter tickets by status and priority.
- Pagination and sorting.

### 3. Authentication and Security
- Spring Security integration.
- Password hashing using BCrypt.
- User authentication.
- JWT generation and validation.
- Stateless, bearer-token-based authentication.
- Custom JWT authentication filter.
- Protected API resources.

### 4. Role-Based Access Control (In Progress)
- User and role entity relationship.
- Many-to-many user-role mapping.
- Dedicated role repository and service.
- Default USER and ADMIN role initialization.
- Role assignment and authorization integration under development.

---

## Application Architecture

SmartDesk follows a layered architecture to maintain separation of concerns and improve maintainability.

```text
             Client (Postman)
                    |
                    v
          Spring Security Filter Chain
                    |
                    v
             REST Controller
                    |
                    v
               Service Layer
              (Business Logic)
                    |
                    v
             Repository Layer
                    |
                    v
                PostgreSQL
```

### Layer Responsibilities

- **Controller Layer:** Handles incoming HTTP requests and returns appropriate responses.
- **Service Layer:** Contains business logic and application operations.
- **Repository Layer:** Handles database operations using Spring Data JPA.
- **Entity Layer:** Represents database entities and relationships.
- **DTO Layer:** Separates API request/response structures from persistence entities.
- **Configuration Layer:** Contains application and security configurations.

---

## REST API Documentation

Base URL:

```text
http://localhost:8080
```

### 1. Authentication APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/login` | Authenticate user and generate JWT |

**Login request**

```http
POST /api/auth/login
Content-Type: application/json
```

Request body depends on `LoginRequest`:

```json
{
  "email": "user@example.com",
  "password": "your-password"
}
```

Successful authentication returns an `AuthResponse` containing the authentication response and JWT.

The login endpoint is intended to be publicly accessible.

### 2. User Management APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/users` | Retrieve all users |
| GET | `/api/users/{id}` | Retrieve user by ID |
| POST | `/api/users` | Register a new user |
| PUT | `/api/users/{id}` | Update user details |
| DELETE | `/api/users/{id}` | Delete user |

**Register user**

```http
POST /api/users
Content-Type: application/json
```

Uses `UserRegistrationRequest` as the request DTO.

Successful registration returns:

- HTTP 201 Created.
- Newly created user response.
- Location header containing the URI of the created resource.

**Retrieve all users**

```http
GET /api/users
```

Returns a list of `UserResponse` objects.

**Retrieve user by ID**

```http
GET /api/users/1
```

**Update user**

```http
PUT /api/users/1
Content-Type: application/json
```

Uses `UserRequest` as the request DTO.

**Delete user**

```http
DELETE /api/users/1
```

Returns a success message after deletion.

### 3. Ticket Management APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/tickets` | Create a ticket |
| GET | `/api/tickets` | Retrieve and filter tickets |
| GET | `/api/tickets/{id}` | Retrieve ticket by ID |
| PATCH | `/api/tickets/{ticketId}/assign/{userId}` | Assign ticket to a user |
| PATCH | `/api/tickets/{ticketId}/status` | Update ticket status |
| PATCH | `/api/tickets/{ticketId}/priority` | Update ticket priority |

**Create ticket**

```http
POST /api/tickets
Content-Type: application/json
```

Uses `TicketRequest` as the request DTO.

Successful creation returns HTTP 201 Created, a `TicketResponse`, and a Location header.

**Retrieve tickets with filtering and pagination**

```http
GET /api/tickets
```

Supported query parameters:

| Parameter | Required | Default | Description |
|---|---|---|---|
| status | No | None | Filter by ticket status |
| priority | No | None | Filter by ticket priority |
| page | No | 0 | Page number |
| size | No | 10 | Number of records per page |
| sortBy | No | createdAt | Sorting field |
| direction | No | desc | Sort direction |

Example:

```http
GET /api/tickets?status=OPEN&priority=HIGH&page=0&size=10&sortBy=createdAt&direction=desc
```

The endpoint returns a paginated `Page<TicketResponse>`.

**Retrieve ticket by ID**

```http
GET /api/tickets/1
```

**Assign ticket to user**

```http
PATCH /api/tickets/1/assign/2
```

Assigns ticket ID 1 to user ID 2.

**Update ticket status**

```http
PATCH /api/tickets/1/status?status=IN_PROGRESS
```

**Update ticket priority**

```http
PATCH /api/tickets/1/priority?priority=HIGH
```

Note: The available status and priority values are defined by the application's `TicketStatus` and `TicketPriority` enums.

---

## Authentication and Security Testing

SmartDesk uses **Spring Security and JWT (JSON Web Token)** for securing protected REST API endpoints.

### Authentication Flow

The authentication process follows this sequence:

```text
Client
  |
  | POST /api/auth/login
  | Login credentials
  v
AuthController
  |
  v
AuthService
  |
  | Authenticate user
  | Validate password using BCrypt
  v
JWT Generation
  |
  | Return JWT
  v
Client
  |
  | Authorization: Bearer <JWT>
  v
JwtAuthenticationFilter
  |
  | Extract and validate JWT
  | Load user details
  | Establish SecurityContext
  v
Spring Security Authorization
  |
  | Check access rules
  v
Protected REST API
```

### Public Endpoints

The following endpoints are accessible without authentication:

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users` | Register a new user |
| POST | `/api/auth/login` | Authenticate and obtain a JWT |

All other requests require authentication under the current security configuration.

### Authentication Testing Scenarios

The following scenarios were explored during security implementation:

| Scenario | Authentication mechanism | Expected/observed behaviour |
|---|---|---|
| 1 | No credentials | HTTP 401 Unauthorized for protected endpoints |
| 2 | Basic Authentication | Not supported by the current security configuration; protected requests remain unauthenticated |
| 3 | Valid JWT Bearer token | Authentication succeeds and the request proceeds |
| 4 | Missing JWT | HTTP 401 Unauthorized for protected endpoints |
| 5 | Invalid or expired JWT | HTTP 401 Unauthorized |

**Understanding HTTP status codes:**

- **401 Unauthorized:** The request does not contain valid authentication credentials.
- **403 Forbidden:** The user is authenticated but does not have sufficient permissions to access the requested resource.

Basic Authentication was explored during the security implementation to understand the difference between authentication and authorization. JWT Bearer Authentication is the intended mechanism for SmartDesk.

### Security Implementation

- Passwords are securely hashed using BCrypt.
- JWT tokens are generated after successful login.
- A custom `JwtAuthenticationFilter` validates incoming Bearer tokens.
- `UserDetailsService` loads user information during token authentication.
- Authenticated user details are stored in the `SecurityContextHolder`.
- Stateless session management is configured using `SessionCreationPolicy.STATELESS`.
- CSRF protection is disabled for the stateless REST API.
- Registration and login endpoints are publicly accessible.
- All other endpoints require authentication.

### Role-Based Access Control (RBAC)

Role-based authorization is currently under development. The planned implementation will distinguish permissions between roles such as `USER` and `ADMIN`, restricting access to specific endpoints based on assigned roles.

## Database Design

PostgreSQL is used as the primary relational database.

Spring Data JPA and Hibernate handle object-relational mapping and persistence.

The application includes:

- User entity.
- Ticket entity.
- User-role relationship.
- User-role mapping table.

### User and Role Relationship

A many-to-many relationship is used between users and roles.

```text
       users
          |
          |
          v
   user_role_mapping
          |
          |
          v
      user_roles
```

The mapping table maintains the association between users and their assigned roles.

Default roles planned for the application:

- USER
- ADMIN

---

## Exception Handling and Validation

SmartDesk incorporates centralized exception handling and request validation.

Implemented capabilities include:

- DTO validation using Jakarta Validation.
- Custom resource-not-found exception.
- Global exception handling using `@RestControllerAdvice`.
- Consistent HTTP error responses.
- Handling of invalid request data.

---

## Getting Started

### Prerequisites

- JDK 17
- PostgreSQL
- Maven
- Git
- Postman (for API testing)

### 1. Clone the repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd smartdesk
```

### 2. Create the PostgreSQL database

Create a database named:

```sql
CREATE DATABASE smartdesk_db;
```

### 3. Configure environment variables

Configure the following environment variables:

```text
POSTGRES_DB_PASSWORD=your_database_password
JWT_SECRET=your_secure_jwt_secret
```

The JWT secret must meet the key-strength requirements of the configured signing algorithm.

### 4. Configure application.properties

The application uses the following configuration:

```properties
spring.application.name=smartdesk

spring.datasource.url=jdbc:postgresql://localhost:5432/smartdesk_db
spring.datasource.username=postgres
spring.datasource.password=${POSTGRES_DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

server.port=8080

jwt.secret=${JWT_SECRET}
jwt.expiration-ms=900000
```

Never commit actual database passwords or JWT secrets to the repository.

### 5. Build the project

```bash
mvn clean install
```

### 6. Run the application

```bash
mvn spring-boot:run
```

The application starts on:

```text
http://localhost:8080
```

---

## Development Roadmap

- [x] Spring Boot project initialization
- [x] PostgreSQL database integration
- [x] JPA and Hibernate configuration
- [x] User entity and repository
- [x] User CRUD APIs
- [x] DTO-based request and response handling
- [x] Request validation
- [x] Global exception handling
- [x] Ticket management APIs
- [x] Ticket assignment
- [x] Ticket status and priority management
- [x] Filtering, pagination, and sorting
- [x] Spring Security configuration
- [x] BCrypt password encoding
- [x] JWT authentication
- [x] Custom JWT authentication filter
- [x] UserRole entity and repository
- [x] Default role initialization implementation
- [ ] Complete role assignment and management
- [ ] Integrate roles with Spring Security authorities
- [ ] Implement role-based endpoint authorization
- [ ] Unit and integration testing
- [ ] API documentation using OpenAPI/Swagger
- [ ] Frontend integration
- [ ] Deployment

---

## Learning Objectives

SmartDesk is an ongoing hands-on backend development project focused on strengthening practical knowledge of:

- Core Java and object-oriented programming.
- Spring Boot and dependency injection.
- REST API design and implementation.
- Database design and ORM.
- Spring Data JPA and Hibernate.
- Authentication and authorization.
- JWT-based security.
- Exception handling and validation.
- Clean code and layered architecture.
- Backend development best practices.

The project is being developed incrementally, with an emphasis on understanding the concepts behind each implementation.

**Status:** Actively under development.

**Version:** 0.0.1-SNAPSHOT
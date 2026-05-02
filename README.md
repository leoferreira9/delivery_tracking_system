# Delivery Tracking System

Backend API developed with Java and Spring Boot for managing deliveries, customers, establishments and deliverers.

The project simulates a delivery tracking system, including user authentication with JWT, delivery status transitions, deliverer assignment, status history, validations, pagination, filtering and database migrations with Flyway.

## Technologies

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- MySQL
- Flyway Migration
- MapStruct
- Bean Validation
- Docker
- Docker Compose
- Swagger / OpenAPI
- Maven
- Lombok

## Main Features

- User registration and login with JWT authentication
- Customer management
- Establishment management
- Deliverer management
- Delivery creation and tracking
- Delivery status update with business rules
- Delivery status history
- Deliverer assignment to deliveries
- Delivery filtering by status and date range
- Pagination support
- DTO-based request and response structure
- Global exception handling
- Database versioning with Flyway
- Dockerized application and database setup

## Business Rules

### Delivery Status Flow

A delivery follows a controlled status flow:

```text
RECEIVED -> PREPARING -> IN_TRANSIT -> DELIVERED
```

A delivery may also be cancelled before completion:

```text
RECEIVED -> CANCELLED
PREPARING -> CANCELLED
```

Rules applied by the system:

- A delivery starts with the status `RECEIVED`.
- A delivery must pass through the expected status flow.
- A delivery must have a deliverer assigned before moving to `IN_TRANSIT`.
- Completed deliveries cannot be updated again.
- Cancelled deliveries cannot be updated again.
- Every status change is stored in the delivery status history.

## Authentication

The API uses JWT authentication.

Public endpoints:

```http
POST /v1/auth/register
POST /v1/auth/login
```

After login, the API returns a JWT token. Protected endpoints require the token in the `Authorization` header:

```http
Authorization: <your_token>
```

> Note: User registration is public in this version because the project is focused on studying authentication, JWT and backend API structure.

## API Documentation

After running the application, Swagger can be accessed at:

```text
http://localhost:8080/swagger-ui.html
```

or:

```text
http://localhost:8080/swagger-ui/index.html
```

## Main Endpoints

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/v1/auth/register` | Register a new user |
| POST | `/v1/auth/login` | Authenticate user and return JWT token |

### Customers

| Method | Endpoint | Description |
|---|---|---|
| POST | `/v1/customers` | Create a customer |
| GET | `/v1/customers` | List customers with pagination |
| GET | `/v1/customers/{id}` | Find customer by ID |
| PATCH | `/v1/customers/{id}` | Update customer data |
| DELETE | `/v1/customers/{id}` | Delete customer |

### Establishments

| Method | Endpoint | Description |
|---|---|---|
| POST | `/v1/establishments` | Create an establishment |
| GET | `/v1/establishments` | List establishments with pagination |
| GET | `/v1/establishments/{id}` | Find establishment by ID |
| PATCH | `/v1/establishments/{id}` | Update establishment data |
| DELETE | `/v1/establishments/{id}` | Delete establishment |

### Deliverers

| Method | Endpoint | Description |
|---|---|---|
| POST | `/v1/deliverers` | Create a deliverer |
| GET | `/v1/deliverers` | List deliverers with pagination |
| GET | `/v1/deliverers/{id}` | Find deliverer by ID |
| PATCH | `/v1/deliverers/{id}` | Update deliverer data |
| DELETE | `/v1/deliverers/{id}` | Delete deliverer |

### Deliveries

| Method | Endpoint | Description |
|---|---|---|
| POST | `/v1/deliveries` | Create a delivery |
| GET | `/v1/deliveries` | List deliveries with pagination and optional filters |
| GET | `/v1/deliveries/{id}` | Find delivery by ID |
| GET | `/v1/deliveries/tracking/{trackingCode}` | Find delivery by tracking code |
| PATCH | `/v1/deliveries/{id}/assign-deliverer` | Assign a deliverer to a delivery |
| PATCH | `/v1/deliveries/{id}/status` | Update delivery status |
| GET | `/v1/deliveries/{id}/history` | Get delivery status history |

## Delivery Filters

The delivery listing endpoint supports optional filters:

```http
GET /v1/deliveries?status=RECEIVED
GET /v1/deliveries?startDate=2026-04-01&endDate=2026-04-30
GET /v1/deliveries?status=IN_TRANSIT&startDate=2026-04-01&endDate=2026-04-30
```

Pagination example:

```http
GET /v1/deliveries?page=0&size=10
```

The maximum page size is configured as `30`.

## Running with Docker

### Requirements

- Docker
- Docker Compose

### 1. Create the `.env` file

Create a `.env` file in the project root based on `.env.example`:

```env
MYSQL_DATABASE=delivery_tracking_system
MYSQL_ROOT_PASSWORD=your_password_here
JWT_SECRET_KEY=your_jwt_secret_here
JWT_EXPIRATION=2
```

### 2. Start the application

```bash
docker compose up --build
```

The application will be available at:

```text
http://localhost:8080
```

### 3. Accessing MySQL from Workbench

The MySQL container is exposed on port `3307` on the host machine.

Use these settings in MySQL Workbench:

```text
Host: 127.0.0.1
Port: 3307
User: root
Password: same value used in MYSQL_ROOT_PASSWORD
Database: delivery_tracking_system
```

Important:

- Inside Docker, the application connects to MySQL using `mysql:3306`.
- From your machine, tools like MySQL Workbench connect using `127.0.0.1:3307`.
- The Docker database is separate from any local MySQL database running on port `3306`.

## Running Locally Without Docker

To run the project locally, you need a MySQL database created and the required environment variables configured.

Example datasource configuration used by the application:

```yaml
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/delivery_tracking_system?allowPublicKeyRetrieval=true&useSSL=false
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password_here
JWT_SECRET_KEY=your_jwt_secret_here
JWT_EXPIRATION=2
```

Then run:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

## Database Migrations

The database schema is managed with Flyway.

Migration files are located in:

```text
src/main/resources/db/migration
```

The project uses migrations to create and evolve the database structure, including tables, relationships and constraints.

## Project Structure

```text
src/main/java/com/leonardo/delivery_tracking_system
├── config
├── controller
├── dto
├── enums
├── exception
├── mapper
├── model
├── repository
├── security
├── service
├── specification
├── utils
└── validation
```

## Error Handling

The API uses a global exception handler to return standardized error responses.

Example structure:

```json
{
  "timestamp": "2026-04-30T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request data",
  "path": "/v1/customers"
}
```

Common status codes:

| Status | Meaning |
|---|---|
| 200 | Successful request |
| 201 | Resource created |
| 204 | Resource deleted successfully |
| 400 | Validation error or invalid request |
| 401 | Authentication required or invalid credentials |
| 403 | Access denied |
| 404 | Resource not found |
| 409 | Data conflict |
| 500 | Unexpected server error |

## Notes

This project was developed as a backend portfolio project, with focus on:

- Clean project structure
- Layered architecture
- DTO usage
- Business rules in the service layer
- JWT authentication
- Dockerized environment
- Database migrations
- API documentation

## Author

Developed by Leonardo.

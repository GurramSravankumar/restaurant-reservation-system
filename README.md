# Restaurant Reservation System - Backend (Spring Boot)

This repository contains the Spring Boot REST API for the Restaurant Reservation System. The API handles user authentication, tables administration, capacity-aware table matching, and reservation lifecycle management.

---

## 🛠️ Technology Stack

* **Java 21 & Spring Boot 3.5.10**
* **Spring Security & Stateless JWT Authentication**
* **Spring Data JPA & Hibernate**
* **MySQL Database**
* **Lombok** (Boilerplate reduction)

---

## 🚪 API Endpoints

### 🔐 Authentication (`/auth/**`)
* `POST /auth/register` - Create user or administrator account
* `POST /auth/login` - Authenticate credentials and receive a JWT token

### 🪑 Table Management (`/api/tables/**`) - *Admin Restricted*
* `GET /api/tables` - Fetch all tables
* `GET /api/tables/{id}` - Fetch single table details
* `POST /api/tables` - Add a new dining table *(Admin only)*
* `PUT /api/tables/{id}` - Update table details *(Admin only)*
* `DELETE /api/tables/{id}` - Delete a dining table *(Admin only)*

### 📅 Reservations (`/api/reservations/**`)
* `POST /api/reservations` - Book a reservation (uses best-fit matching & conflict prevention)
* `GET /api/reservations/my` - Fetch reservations for the logged-in customer
* `GET /api/reservations` - Fetch all reservations *(Admin only)*
* `GET /api/reservations/date` - Query reservations by date *(Admin only)*
* `DELETE /api/reservations/{id}` - Cancel a reservation (customers can cancel their own; admins can cancel any)

---

## 🚀 Setup & Execution

### 1. Database Setup
Ensure you have MySQL running. Create a schema named `restaurant_db`:
```sql
CREATE DATABASE restaurant_db;
```

### 2. Environment Variables
The application uses environment variables with local fallbacks configured in `src/main/resources/application.properties`:
* `DB_URL` - Database JDBC URL (Fallback: `jdbc:mysql://localhost:3306/restaurant_db`)
* `DB_USERNAME` - MySQL Username (Fallback: `root`)
* `DB_PASSWORD` - MySQL Password (Fallback: `939854`)
* `JWT_SECRET` - JWT Secret Key (Fallback: standard test key)
* `PORT` - Port to run the server on (Fallback: `8080`)

### 3. Build & Run
Run the standard maven wrapper or boot command from the root folder:
```bash
mvn spring-boot:run
```

The server automatically seeds a default administrator account and test seating tables on first run.

---

## 📁 Project Structure
```
src/main/java/com/sk/restaurant/
├── config/        # Security configurations, CORS policies, JWT Filters, Database Seeds
├── controller/    # REST API endpoints (Auth, Tables, Reservations)
├── dto/           # Data Transfer Objects for API requests/responses
├── entity/        # JPA Database Entities (User, Table, Reservation)
├── enums/         # Role types & Reservation Statuses
├── exception/     # Global exception handling & custom responses
├── repository/    # JPA SQL Repositories
└── service/       # Business logic (JWT Services, Seating availability & allocations)
```

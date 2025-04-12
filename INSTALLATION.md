# **MeepleConnect - Installation Guide**

## **Table of Contents**
1. Introduction
2. Prerequisites
3. Installation Steps
4. Test Users and Roles
5. Postman Collection
6. REST Endpoints

---

## **1. Introduction**

**MeepleConnect** is a web API specifically designed for board game stores. The system offers the following core functionalities:

- **Customers** can:
  - View and reserve board games for play sessions
  - Express interest in purchasing games
  - Manage their own reservations

- **Staff** can:
  - Manage the board game inventory
  - Schedule play sessions
  - Process reservations
  - Manage customer data

**Technologies Used:**
- **Backend:**
  - Java 17
  - Spring Boot (version 3.x)
  - Spring Security with JWT authentication
  - PostgreSQL (or other relational databases via JPA)
  - Lombok & MapStruct for code optimization

- **Testing & Development:**
  - Postman for API testing
  - Maven for dependency management
  - Git for version control

---

## **2. Prerequisites**

Ensure that the following tools are installed on your system:

- **Java 17** (or higher)
- **Maven** (for dependency management)
- **PostgreSQL** (or another relational database like MySQL)
- **Postman** (for API testing)
- **Git** (for cloning the repository)

---

## **3. Installation Steps**

### 📥 **1. Clone the repository:**
```bash
git clone https://github.com/RuthgerdeGraaf/MeepleConnect.git
cd MeepleConnect
```

### ⚙️ **2. Configure the database:**
1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE meepleconnect;
   ```
2. Update databasereferenties in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/meepleconnect
   spring.datasource.username=postgres
   spring.datasource.password=password
   ```

### 🛠️ **3. Install dependencies and run migrations:**
```bash
./mvn clean install
```

### 🚀 **4. Start the application:**
```bash
./mvn spring-boot:run
```

The web API will be available at:
```
http://localhost:8080
```

---

## **4. Test Users and Roles**

| **Username** | **Password** | **Role**    |
|--------------|--------------|-------------|
| admin        | admin123     | EMPLOYEE    |
| klant1       | klant123     | CUSTOMER    |

- **EMPLOYEE** → Can manage games, play sessions, and reservations.
- **CUSTOMER** → Can view games, make reservations, and express interest in purchasing.

---

## **5. Postman Collection**

The **Postman collection** contains all important API requests:

1. **Login Endpoint** → Generates JWT-token
2. **CRUD operations for Board Games**
3. **Manage Reservations**
4. **Schedule Play Sessions**

➡️ **Import the provided Postman collection ([MeepleConnect.postman_collection.json](./MeepleConnect.postman_collection.json)) in Postman.**

---

## **6. REST Endpoints**

### 🔑 **Authentication**

**POST** `/api/auth/login`
```json
{
  "username": "admin",
  "password": "admin123"
}
```
➡️ **Response:** JWT token

This is the endpoint used to authenticate users and generate a JWT token. The token is required for subsequent requests to protected endpoints.

---

### 🎲 **Board Game Management**

**GET** `/api/boardgames` → Retrieves all board games

**POST** `/api/boardgames` *(EMPLOYEE only)*
```json
{
  "name": "Catan",
  "genre": "Strategy",
  "players": 4,
  "price": 29.99
}
```

**PUT** `/api/boardgames/{id}` → Update a board game *(EMPLOYEE only)*

**DELETE** `/api/boardgames/{id}` → Delete a board game *(EMPLOYEE only)*

---

### 📅 **Reservations**

**POST** `/api/reservations` *(CUSTOMER only)*
```json
{
  "boardgameId": 1,
  "reservationDate": "2024-03-01"
}
```

**GET** `/api/reservations/customer/{customerId}` → Retrieve all reservations for a customer

**DELETE** `/api/reservations/{reservationId}` → Delete a reservation *(CUSTOMER for own reservations, EMPLOYEE for all)*

---

### 📄 **Endpoint Security:**

- **JWT Authentication** → Required for all endpoints except `/api/auth/login`.
- Once the user logs in and receives the JWT token, it must be included in the Authorization header for subsequent requests.

**Roles:**
- **EMPLOYEE** → Full CRUD capabilities (Board Games, Play Sessions, Reservations)
- **CUSTOMER** → Access only to their own reservations and limited CRUD capabilities

**JWT Authentication Process:**
1. **Login:** A user logs in using the `/api/auth/login` endpoint with valid credentials (username and password). If successful, the server responds with a JWT token.
2. **Token Usage:** The received JWT token must be included in the Authorization header as a Bearer token in all subsequent requests to protected endpoints.
3. **Role-based Access:**
   - **EMPLOYEE:** Can perform full CRUD operations on board games, schedule play sessions, and manage reservations
   - **CUSTOMER:** Can make reservations and view their own reservations. They cannot manage board games or schedule play sessions
4. **Request with JWT token:** Each protected endpoint checks the validity of the token and ensures the user has the appropriate role for the action

**Example of a protected request with JWT token:**

**POST** `/api/boardgames`
```json
{
  "name": "Catan",
  "genre": "Strategy",
  "players": 4,
  "price": 29.99
}
```

**Header:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.S7J5KHkl_-Cj0w8rJ0yHnp1lmUQd7-TtdR6qZ-hOocI4
```

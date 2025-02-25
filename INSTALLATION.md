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

**MeepleConnect** is a web API designed for board game stores, enabling customers to reserve board games for play sessions or express interest in purchasing. Staff can manage the game inventory, schedule play sessions, and handle customer reservations.

**Technologies Used:**
- **Java 17**
- **Spring Boot**
- **Spring Security**
- **JWT Authentication**
- **PostgreSQL** (or other relational databases via JPA)
- **Flyway** (for database migrations)
- **Lombok** & **MapStruct** (for code optimization)
- **Postman** (for API testing)

---

## **2. Prerequisites**

Ensure the following tools are installed on your system:

- **Java 17** (or higher)
- **Maven** (for dependency management)
- **PostgreSQL** (or another relational database like MySQL)
- **Postman** (for API testing)
- **Flyway** (automatically run via Maven)
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
2. Update database credentials in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/meepleconnect
   spring.datasource.username=postgres
   spring.datasource.password=password
   ```

### 🛠️ **3. Install dependencies and run migrations:**
```bash
./mvnw clean install
./mvnw flyway:migrate
```

### 🚀 **4. Start the application:**
```bash
./mvnw spring-boot:run
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

- **EMPLOYEE** → Can manage games, schedule sessions, and handle reservations.
- **CUSTOMER** → Can view games, make reservations, and express purchase interest.

---

## **5. Postman Collection**

The **Postman collection** includes all major API requests:

1. **Login Endpoint** → Generates JWT token
2. **CRUD Operations for Board Games**
3. **Manage Reservations**
4. **Schedule Play Sessions**

➡️ **Import the provided Postman collection ([MeepleConnect.postman_collection.json](./MeepleConnect.postman_collection.json)) into Postman.**

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

**PUT** `/api/boardgames/{id}` → Update a board game

**DELETE** `/api/boardgames/{id}` → Delete a board game

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

**DELETE** `/api/reservations/{reservationId}` → Delete a reservation

---

### 📄 **Endpoint Security:**
- **JWT Authentication** → Required for all endpoints except `/api/auth/login`.
- **Roles:**
  - **EMPLOYEE** → Full CRUD capabilities
  - **CUSTOMER** → Access only to their own reservations
# **MeepleConnect - Installation Guide**

## **Table of Contents**

1. Introduction
2. Prerequisites
3. Installation Steps
4. Test Users and Roles
5. Postman Collection
6. Complete REST Endpoints Documentation
7. Troubleshooting

---

## **1. Introduction**

**MeepleConnect** is a comprehensive web API designed for board game stores. The system offers advanced functionalities for both customers and staff:

- **Customers** can:

  - View and reserve board games for play sessions
  - Express interest in purchasing games
  - Manage their own reservations
  - Write reviews and ratings
  - Receive notifications

- **Staff** can:
  - Manage the board game inventory
  - Schedule play sessions
  - Process reservations
  - Manage customer data
  - View analytics and reports
  - Send notifications

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

1. **Install PostgreSQL** if not already installed
2. **Create a PostgreSQL database:**
   ```sql
   CREATE DATABASE meepleconnect;
   ```
3. **Update database credentials in `application.properties`:**

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/meepleconnect
   spring.datasource.username=postgres
   spring.datasource.password=YOUR_POSTGRES_PASSWORD
   ```

   **⚠️ IMPORTANT:** Replace `YOUR_POSTGRES_PASSWORD` with your actual PostgreSQL password!

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

### ✅ **5. Verify installation:**

Test if the API is running by visiting:

```
http://localhost:8080/api/health
```

You should see a JSON response with the API status.

---

## **4. Test Users and Roles**

The application automatically creates these users on first startup:

| **Username** | **Password**  | **Role**    | **Description**             |
| ------------ | ------------- | ----------- | --------------------------- |
| **Ruthger**  | `password123` | ADMIN, USER | Full access to all features |
| **Edwin**    | `password123` | USER        | Customer access only        |

- **ADMIN** → Full CRUD capabilities (Board Games, Users, Reservations, Analytics)
- **USER** → Can make reservations, view games, write reviews, manage own profile

---

## **5. Postman Collection**

The **complete Postman collection** contains all API endpoints:

1. **Authentication** → Login and registration
2. **Users** → Complete user management
3. **Boardgames** → Full CRUD operations
4. **Reservations** → Reservation management
5. **Publishers** → Publisher management
6. **Statistics** → Dashboard and analytics
7. **Search** → Advanced search functionality
8. **Notifications** → Notification system
9. **Reviews** → Review and rating system
10. **Analytics** → Business intelligence
11. **Health** → API monitoring
12. **Files** → File upload/download

➡️ **Import the provided Postman collection ([MeepleConnect_API_Collection.json](./MeepleConnect_API_Collection.json)) in Postman.**

**Setup Postman:**

1. Import the collection
2. Set the `baseUrl` variable to `http://localhost:8080`
3. Use the login requests to get a JWT token
4. The token will automatically be used in subsequent requests

---

## **6. Complete REST Endpoints Documentation**

### 🔑 **Authentication**

**POST** `/api/auth/login`

```json
{
  "username": "Ruthger",
  "password": "password123"
}
```

➡️ **Response:** JWT token

### 👥 **User Management**

**GET** `/api/users` → Get all users _(ADMIN only)_
**GET** `/api/users/{id}` → Get user by ID
**POST** `/api/users/register` → Register new user
**PUT** `/api/users/{id}` → Update user _(ADMIN only)_
**DELETE** `/api/users/{id}` → Delete user _(ADMIN only)_

### 🎲 **Board Game Management**

**GET** `/api/boardgames` → Get all board games (with optional filters)
**GET** `/api/boardgames/{id}` → Get board game by ID
**POST** `/api/boardgames` → Add new board game _(ADMIN only)_
**PUT** `/api/boardgames/{id}` → Update board game _(ADMIN only)_
**DELETE** `/api/boardgames/{id}` → Delete board game _(ADMIN only)_

### 📅 **Reservations**

**GET** `/api/reservations` → Get all reservations _(ADMIN only)_
**GET** `/api/reservations/customer/{customerId}` → Get reservations by customer
**GET** `/api/reservations/boardgame/{boardgameId}` → Get reservations by board game
**POST** `/api/reservations` → Create new reservation
**DELETE** `/api/reservations/{id}` → Cancel reservation

### 🏢 **Publishers**

**GET** `/api/publishers` → Get all publishers
**GET** `/api/publishers/{id}` → Get publisher by ID
**GET** `/api/publishers/country/{country}` → Get publishers by country
**POST** `/api/publishers` → Add new publisher _(ADMIN only)_
**PUT** `/api/publishers/{id}` → Update publisher _(ADMIN only)_
**DELETE** `/api/publishers/{id}` → Delete publisher _(ADMIN only)_

### 📊 **Statistics**

**GET** `/api/statistics/dashboard` → Get dashboard statistics
**GET** `/api/statistics/boardgames/popular` → Get popular board games
**GET** `/api/statistics/reservations/monthly` → Get monthly reservations

### 🔍 **Search**

**POST** `/api/search/boardgames` → Search board games with JSON criteria
**POST** `/api/search/boardgames/advanced` → Advanced search with multiple filters

### 🔔 **Notifications**

**GET** `/api/notifications` → Get all notifications
**GET** `/api/notifications/{id}` → Get notification by ID
**POST** `/api/notifications` → Create new notification
**PUT** `/api/notifications/{id}/read` → Mark notification as read
**DELETE** `/api/notifications/{id}` → Delete notification
**GET** `/api/notifications/unread/count` → Get unread count
**POST** `/api/notifications/broadcast` → Send broadcast notification _(ADMIN only)_

### ⭐ **Reviews**

**GET** `/api/reviews` → Get all reviews
**GET** `/api/reviews/{id}` → Get review by ID
**GET** `/api/reviews/boardgame/{boardgameId}` → Get reviews by board game
**POST** `/api/reviews` → Create new review
**PUT** `/api/reviews/{id}` → Update review
**DELETE** `/api/reviews/{id}` → Delete review
**GET** `/api/reviews/boardgame/{boardgameId}/average-rating` → Get average rating
**POST** `/api/reviews/{id}/helpful` → Mark review as helpful

### 📈 **Analytics**

**GET** `/api/analytics/revenue/forecast` → Get revenue forecast
**GET** `/api/analytics/boardgames/performance` → Get board game performance
**GET** `/api/analytics/customer/insights` → Get customer insights
**GET** `/api/analytics/trends/seasonal` → Get seasonal trends

### 🏥 **Health & Monitoring**

**GET** `/api/health` → Get API health status
**GET** `/api/health/ping` → Ping test
**GET** `/api/health/ready` → Readiness check

### 📁 **File Management**

**POST** `/api/files/upload` → Upload file _(ADMIN only)_
**GET** `/api/files/{filename}` → Download file

### 🎮 **Fun & Easter Eggs**

**GET** `/api/fun/teapot` → I'm a teapot response (418 status)
**GET** `/api/fun/boardgames/418` → Special teapot board game

---

## **7. Troubleshooting**

### **Database Connection Issues:**

- Verify PostgreSQL is running
- Check database credentials in `application.properties`
- Ensure database `meepleconnect` exists

### **Port Already in Use:**

- Change port in `application.properties`:
  ```properties
  server.port=8081
  ```

### **JWT Token Issues:**

- Ensure you're using the correct login credentials
- Check that the Authorization header format is: `Bearer <token>`

### **Permission Denied:**

- Verify user has the correct role for the endpoint
- Check JWT token is valid and not expired

### **Common Error Codes:**

- **401 Unauthorized** → Invalid or missing JWT token
- **403 Forbidden** → Insufficient permissions
- **404 Not Found** → Resource doesn't exist
- **400 Bad Request** → Invalid request data
- **418 I'm a Teapot** → Easter egg response

---

## **🎯 Quick Start Guide**

1. **Setup Database:** Create PostgreSQL database and update credentials
2. **Start Application:** Run `./mvn spring-boot:run`
3. **Import Postman Collection:** Import the provided JSON file
4. **Login:** Use Ruthger/password123 to get JWT token
5. **Test Endpoints:** Start with `/api/health` and `/api/boardgames`

**API Base URL:** `http://localhost:8080`

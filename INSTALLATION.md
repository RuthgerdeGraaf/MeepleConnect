# MeepleConnect – Full Installation Guide

## Table of Contents

1. About MeepleConnect
2. Requirements
3. Step-by-Step Installation
4. Test Users
5. Testing the API with Postman
6. API Overview
7. Troubleshooting
8. Quick Start Summary

---

## 1. About MeepleConnect

**MeepleConnect** is a local web application (API) built for board game stores. You can run the software locally to test its functionality.

### What does this API do?

- **Customers** can:
  - View and reserve games
  - Write and rate reviews
  - Show interest in purchases
  - Receive notifications

- **Staff** can:
  - Manage games
  - Schedule game sessions
  - Manage customer data
  - View reports and analytics

---

## 2. Requirements

Make sure the following software is installed:

| Software       | Purpose                            | Download                                |
|----------------|------------------------------------|-----------------------------------------|
| **Java 17+**   | Runs the application               | [Adoptium](https://adoptium.net)        |
| **Maven**      | Dependency management              | [Maven](https://maven.apache.org/install.html) |
| **PostgreSQL** | Database engine                    | [PostgreSQL](https://www.postgresql.org/download/) |
| **Postman**    | API testing interface              | [Postman](https://www.postman.com/downloads/) |
| **Git**        | Code version control               | [Git](https://git-scm.com/)             |

---

## 3. Step-by-Step Installation

### Step 1: Clone the repository

Open your terminal/command prompt:

```bash
git clone https://github.com/RuthgerdeGraaf/MeepleConnect.git
cd MeepleConnect
```

---

### Step 2: Create the database

1. Open PostgreSQL (via pgAdmin or terminal)
2. Create a new empty database:

   ```sql
   CREATE DATABASE meepleconnect;
   ```

---

### Step 3: Configure database settings

Open the file `src/main/resources/application.properties` and set your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/meepleconnect
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

Replace `YOUR_PASSWORD` with your actual PostgreSQL password.

---

### Step 4: Install dependencies

In the terminal:

```bash
./mvn clean install
```

---

### Step 5: Start the application

Run the application with:

```bash
./mvn spring-boot:run
```

The API will be available at:

```
http://localhost:8080
```

---

### Step 6: Test if it’s working

Open your browser and go to:

```
http://localhost:8080/api/health
```

You should see a JSON message like:

```json
{ "status": "UP" }
```

---

## 4. Test Users

On first run, two users are created automatically:

| Username | Password     | Roles        |
|----------|--------------|--------------|
| Ruthger  | password123  | ADMIN, USER  |
| Edwin    | password123  | USER         |

- **ADMIN**: Full access to all features
- **USER**: Can reserve games, view games, write reviews

---

## 5. Testing the API with Postman

1. Open Postman
2. Import the provided collection:  
   `MeepleConnect_API_Collection.json`
3. Set `baseUrl` variable to `http://localhost:8080`
4. Use the login request to get a JWT token
5. Other requests will automatically use the token

---

## 6. API Overview

Here’s a simplified list of the available API endpoints:

| Feature        | Sample Endpoint                          |
|----------------|-------------------------------------------|
| Authentication | `POST /api/auth/login`                   |
| Users          | `GET /api/users` (admin only)            |
| Board Games    | `GET /api/boardgames`                    |
| Reservations   | `POST /api/reservations`                 |
| Reviews        | `GET /api/reviews/boardgame/{id}`        |
| Stats          | `GET /api/statistics/dashboard`          |
| Notifications  | `GET /api/notifications`                 |
| Files          | `POST /api/files/upload` (admin only)    |

Full routes and usage examples are available in the Postman collection.

---

## 7. Troubleshooting

| Problem                  | Solution                                             |
|--------------------------|------------------------------------------------------|
| **DB not working**       | Check if PostgreSQL is running and credentials match |
| **Port already in use**  | Change port in `application.properties`: `server.port=8081` |
| **JWT issues**           | Re-login and use `Authorization: Bearer` header     |
| **403 or 401 errors**    | Check roles and token validity                      |

---

## 8. Quick Start Summary

1. Clone the repository
2. Install PostgreSQL and create the database
3. Edit `application.properties` with your credentials
4. Run `./mvn clean install` then `./mvn spring-boot:run`
5. Test at `/api/health`
6. Login with Ruthger / password123
7. Use Postman to test API endpoints

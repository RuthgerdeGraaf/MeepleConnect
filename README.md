# 🧩 MeepleConnect - Comprehensive Board Game Store API

[![GitHub Repo](https://img.shields.io/badge/GitHub-Repository-blue)](https://github.com/RuthgerdeGraaf/MeepleConnect)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13+-blue)](https://www.postgresql.org/)

[Here](https://github.com/RuthgerdeGraaf/MeepleConnect) you can find the GitHub repository.

## 📚 Table of Contents

- [📖 About The Project](#about-the-project)
- [⚙️ Features](#features)
- [🛠️ Built With](#built-with)
- [🚀 Quick Start](#quick-start)
- [📊 API Overview](#api-overview)
- [🔐 Authentication & Users](#authentication--users)
- [📖 Documentation](#documentation)
- [👤 Author](#author)

<br>

<img src="src/main/resources/assets/mcLogo.png" alt="MeepleConnect Logo" width="200"/>

---

## 📖 About The Project

**MeepleConnect** is a comprehensive, production-ready web API designed to revolutionize board game store operations. This modern REST API provides advanced functionalities for both **customers** and **store staff**, creating a seamless experience for managing board game reservations, inventory, and customer interactions.

The API follows REST principles, implements proper security with JWT authentication, and provides extensive business intelligence features for data-driven decision making.

---

## ⚙️ Features

### 🎮 **Core Functionality**

✅ **Complete Board Game Management**  
→ Full CRUD operations for board games with advanced filtering and search capabilities

✅ **Advanced Reservation System**  
→ Customers can reserve board games with flexible scheduling and participant management

✅ **Comprehensive User Management**  
→ Role-based user system with automatic profile creation and management

✅ **Publisher Management**  
→ Complete publisher database with country-based filtering and management

### 🔐 **Security & Authentication**

✅ **JWT-Based Authentication**  
→ Secure token-based authentication with role-based access control

✅ **Role-Based Authorization**  
→ ADMIN and USER roles with appropriate permissions for each endpoint

✅ **Automatic User Initialization**  
→ Pre-configured users for immediate testing and development

### 📊 **Business Intelligence**

✅ **Advanced Analytics**  
→ Revenue forecasting, customer insights, and seasonal trend analysis

✅ **Real-Time Statistics**  
→ Dashboard statistics, popular games tracking, and monthly reports

✅ **Performance Metrics**  
→ Board game performance analysis and utilization tracking

### 🔍 **Search & Discovery**

✅ **Advanced Search Functionality**  
→ JSON-based search with multiple criteria and filters

✅ **Smart Filtering**  
→ Filter by genre, availability, player count, and price range

### 🔔 **Communication & Engagement**

✅ **Notification System**  
→ Real-time notifications with priority levels and broadcast capabilities

✅ **Review & Rating System**  
→ Customer reviews with helpful voting and average rating calculations

### 🏥 **Monitoring & Health**

✅ **API Health Monitoring**  
→ Comprehensive health checks and readiness monitoring

✅ **File Management**  
→ Secure file upload and download capabilities

### 🎯 **Developer Experience**

✅ **Complete REST Compliance**  
→ Proper HTTP status codes, location headers, and RESTful design

✅ **Comprehensive Documentation**  
→ Complete Postman collection and detailed installation guide

✅ **Error Handling**  
→ Professional error responses with appropriate HTTP status codes

---

## 🛠️ Built With

| **Technology**           | **Purpose**                    | **Version** |
| ------------------------ | ------------------------------ | ----------- |
| 🟨 **Java**              | Core programming language      | 17+         |
| ⚡ **Spring Boot**       | Backend framework              | 3.x         |
| ��️ **Spring Security**  | Authentication & Authorization | 3.x         |
| 📊 **PostgreSQL**        | Relational database            | 13+         |
| 🔀 **MapStruct**         | Object mapping (DTOs)          | Latest      |
| ⚡ **Lombok**            | Reduces boilerplate code       | Latest      |
| 🧪 **JUnit & Mockito**   | Unit and integration testing   | Latest      |
| 🧱 **Maven**             | Dependency management          | Latest      |
| 🔐 **JWT**               | Token-based authentication     | Latest      |

---

## 🚀 Quick Start

### **Prerequisites**

- Java 17 or higher
- PostgreSQL 13 or higher
- Maven 3.6 or higher

### **Installation**

1. **Clone the repository:**

   ```bash
   git clone https://github.com/RuthgerdeGraaf/MeepleConnect.git
   cd MeepleConnect
   ```

2. **Configure database:**

   ```sql
   CREATE DATABASE meepleconnect;
   ```

   Update `application.properties` with your PostgreSQL credentials.

3. **Start the application:**

   ```bash
   ./mvn spring-boot:run
   ```

4. **Verify installation:**
   ```bash
   curl http://localhost:8080/api/health
   ```

### **Test Users**

The application automatically creates these users on first startup:

| **Username** | **Password**  | **Role**    | **Access Level** |
| ------------ | ------------- | ----------- | ---------------- |
| **Ruthger**  | `password123` | ADMIN, USER | Full access      |
| **Edwin**    | `password123` | USER        | Customer access  |

---

## 📊 API Overview

### **Controllers & Endpoints**

| **Controller**     | **Endpoints** | **Description**          |
| ------------------ | ------------- | ------------------------ |
| **Authentication** | 3 endpoints   | Login and registration   |
| **Users**          | 5 endpoints   | Complete user management |
| **Boardgames**     | 7 endpoints   | Full CRUD operations     |
| **Reservations**   | 5 endpoints   | Reservation management   |
| **Publishers**     | 6 endpoints   | Publisher management     |
| **Statistics**     | 3 endpoints   | Dashboard analytics      |
| **Search**         | 2 endpoints   | Advanced search          |
| **Notifications**  | 7 endpoints   | Notification system      |
| **Reviews**        | 8 endpoints   | Review & rating system   |
| **Analytics**      | 4 endpoints   | Business intelligence    |
| **Health**         | 3 endpoints   | API monitoring           |
| **Files**          | 2 endpoints   | File management          |

### **Total: 60+ Endpoints**

### **Key Features**

- ✅ **REST Compliant** - Proper HTTP methods and status codes
- ✅ **Location Headers** - All POST requests return 201 with location
- ✅ **JSON Body Support** - Complex queries use JSON instead of parameters
- ✅ **Role-Based Access** - ADMIN and USER permissions
- ✅ **Comprehensive Error Handling** - Professional error responses
- ✅ **Health Monitoring** - API status and readiness checks

---

## 🔐 Authentication & Users

### **JWT Authentication Flow**

1. **Login** → POST `/api/auth/login` with credentials
2. **Receive Token** → JWT token for subsequent requests
3. **Use Token** → Include in Authorization header: `Bearer <token>`

### **Role-Based Access**

- **ADMIN** → Full CRUD capabilities across all resources
- **USER** → Customer operations (reservations, reviews, profile management)

### **Automatic User Initialization**

- Users are automatically created on first startup
- No manual database setup required
- Secure password hashing with BCrypt
- Persistent data across application restarts

---

## 📖 Documentation

### **Installation Guide**

📖 **[Complete Installation Guide](./INSTALLATION.md)**  
→ Step-by-step setup instructions with troubleshooting

### **API Testing**

📋 **[Postman Collection](./MeepleConnect_API_Collection.json)**  
→ Complete collection with 60+ endpoints ready for testing

### **API Documentation**

### **Health Checks**

🏥 **API Health** → `http://localhost:8080/api/health`

---

## 🎯 **Project Highlights**

### **Professional Standards**

- ✅ **REST Compliance** - Follows all REST principles
- ✅ **Security Best Practices** - JWT authentication with role-based access
- ✅ **Error Handling** - Comprehensive exception handling
- ✅ **Documentation** - Complete installation and API documentation
- ✅ **Testing Ready** - Full Postman collection for testing

### **Advanced Features**

- ✅ **Business Intelligence** - Analytics and reporting capabilities
- ✅ **User Engagement** - Reviews, notifications, and feedback systems
- ✅ **Search & Discovery** - Advanced search with multiple criteria
- ✅ **Monitoring** - Health checks and API status monitoring
- ✅ **File Management** - Secure file upload and download

### **Developer Experience**

- ✅ **Easy Setup** - Automated user initialization
- ✅ **Complete Documentation** - Installation guide and API docs
- ✅ **Testing Tools** - Ready-to-use Postman collection
- ✅ **Error Handling** - Clear error messages and status codes

---

## 👤 Author

**Developed by [Ruthger de Graaf](https://github.com/RuthgerdeGraaf)**  
Fullstack Development student at [NOVI Hogeschool](https://www.novi.nl/)

---

## 📞 **Support & Resources**

- 📖 **[Installation Guide](./INSTALLATION.md)** - Complete setup instructions
- 📋 **[Postman Collection](./MeepleConnect_API_Collection.json)** - API testing collection
- 🐛 **[GitHub Issues](https://github.com/RuthgerdeGraaf/MeepleConnect/issues)** - Report bugs or request features
- 📚 **[GitHub Repository](https://github.com/RuthgerdeGraaf/MeepleConnect)** - Source code and documentation

---

**🎉 Ready to revolutionize your board game store operations with MeepleConnect!**

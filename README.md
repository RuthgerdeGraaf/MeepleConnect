# 🧩 MeepleConnect - Web API for Boardgames

[![GitHub Repo](https://img.shields.io/badge/GitHub-Repository-blue)](https://github.com/RuthgerdeGraaf/MeepleConnect)

[Here](https://github.com/RuthgerdeGraaf/MeepleConnect) you can find the GitHub repository.

## 📚 Table of Contents

- [📖 About The Project](#about-the-project)
- [⚙️ Features](#features)
- [🛠️ Built With](#built-with)
- [👤 Author](#author)

<br>

<img src="src/main/resources/assets/mcLogo.png" alt="MeepleConnect Logo" width="200"/>

---

## 📖 About The Project

**MeepleConnect** is a modern web API designed to assist board game stores in managing their daily operations more efficiently. It streamlines workflows for both **customers** and **store staff**. Customers can explore the game catalog, make reservations for play sessions, and express interest in purchases, while staff can manage the game inventory, schedule sessions, and process customer reservations.

---

## ⚙️ Features

✅ **Game Reservation Management**  
→ Customers can reserve board games for play sessions. A customer profile is created upon the first reservation, allowing feedback and preferences to be added later.

✅ **Customer Profile Management**  
→ Customers can create and manage their profiles, view reservations, and track purchase interests.

✅ **Authentication & Authorization**  
→ JWT-based security ensures that users can only perform actions within their roles.

✅ **Game Inventory Management**  
→ Staff can add, edit, and remove board games from the inventory and manage customer data.

✅ **Session Scheduling**  
→ Staff can schedule play sessions, including setting time and date, and upload PDF instructions for the sessions.

✅ **Order & Purchase Interest Management**  
→ Customers can express purchase interest, and staff can manage and track these interests.

✅ **Session & Order Tracking**  
→ Staff can track all sessions, including player details, session time, and participant count.

---

## 🛠️ Built With

| **Technology**     | **Purpose**                   |
|--------------------|-------------------------------|
| 🟨 **Java 17**      | Core programming language     |
| ⚡ **Spring Boot**  | Backend framework             |
| 🛡️ **Spring Security** | Authentication & Authorization |
| 📊 **PostgreSQL**   | Relational database           |
| 📋 **Flyway**       | Database migrations           |
| 🔀 **MapStruct**    | Object mapping (DTOs)         |
| 📖 **Swagger (OpenAPI)** | API documentation       |
| 🧪 **JUnit & Mockito** | Unit and integration testing |
| 🧱 **Maven**        | Dependency management         |

---

## 👤 Author

Developed by [**Ruthger de Graaf**](https://github.com/RuthgerdeGraaf),  
Fullstack Development student at [**NOVI Hogeschool**](https://www.novi.nl/).

---

💡 **Tip:**  
Check out the [**Installation Guide**](./INSTALLATION.md) for a detailed walkthrough on setting up the project, or use the [**Postman Collection**](./MeepleConnect.postman_collection.json) to explore the API endpoints directly.

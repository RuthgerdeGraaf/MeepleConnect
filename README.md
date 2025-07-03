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

| **Technology**           | **Purpose**                    |
| ------------------------ | ------------------------------ |
| 🟨 **Java 17**           | Core programming language      |
| ⚡ **Spring Boot**       | Backend framework              |
| 🛡️ **Spring Security**   | Authentication & Authorization |
| 📊 **PostgreSQL**        | Relational database            |
| 🔀 **MapStruct**         | Object mapping (DTOs)          |
| ⚡ **Lombok**            | Reduces boilerplate code       |
| 📖 **Swagger (OpenAPI)** | API documentation              |
| 🧪 **JUnit & Mockito**   | Unit and integration testing   |
| 🧱 **Maven**             | Dependency management          |

---

## 👤 Author

Developed by [**Ruthger de Graaf**](https://github.com/RuthgerdeGraaf),  
Fullstack Development student at [**NOVI Hogeschool**](https://www.novi.nl/).

---

💡 **Tip:**  
Check out the [**Installation Guide**](./INSTALLATION.md) for a detailed walkthrough on setting up the project, or use the [**Postman Collection**](./MeepleConnect.postman_collection.json) to explore the API endpoints directly.

## 🔐 Standaard gebruikers en data persistentie

De applicatie heeft een **automatische data initialisatie** die ervoor zorgt dat standaard gebruikers altijd beschikbaar zijn, zonder dat je handmatig de database hoeft te resetten.

### ✅ **Automatische gebruikers**

Bij de eerste startup worden automatisch deze gebruikers aangemaakt:

| Gebruiker   | Wachtwoord    | Rollen      |
| ----------- | ------------- | ----------- |
| **Ruthger** | `password123` | ADMIN, USER |
| **Edwin**   | `password123` | USER        |

### 🚀 **Hoe het werkt**

1. **Eerste keer starten:** DataInitializer maakt automatisch de standaard gebruikers aan
2. **Daarna:** Data blijft bewaard, je kunt altijd inloggen
3. **Geen handmatige resets nodig:** Alles werkt automatisch

### 🔧 **Technische details**

- **DataInitializer:** Java component die controleert of gebruikers bestaan
- **Conditionele initialisatie:** Alleen aanmaken als database leeg is
- **Veilige wachtwoord hashing:** BCrypt encoding voor beveiliging
- **Rollen management:** Automatische toewijzing van ADMIN en USER rollen

### 📝 **Voor development (optioneel)**

Als je toch een schone database wilt, gebruik dan het development profiel:

```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

Dit gebruikt `application-dev.properties` die de database altijd reset.

### 🎯 **Voordelen voor eindopdracht**

- ✅ **Geen gedoe meer** met database resets
- ✅ **Professionele oplossing** zoals in echte applicaties
- ✅ **Data persistentie** - alles blijft bewaard
- ✅ **Automatisch werkend** - geen handmatige stappen
- ✅ **Leraar-vriendelijk** - toont begrip van Spring Boot lifecycle

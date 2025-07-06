# MeepleConnect API Testing Guide

## Overzicht

Deze guide helpt je om de MeepleConnect Boardgames API te testen en te gebruiken. De API biedt functionaliteiten voor het beheren van bordspellen, uitgevers, reserveringen en gebruikers.

## 🚀 Quick Start

### 1. Applicatie Starten

```bash
# Compileer de applicatie
mvn clean compile

# Start de applicatie
mvn spring-boot:run
```

De API is beschikbaar op: `http://localhost:8080`

### 2. Database Setup

De applicatie gebruikt PostgreSQL. Zorg ervoor dat je een PostgreSQL database hebt draaien met:

- Database naam: `meepleconnect`
- Username: `postgres`
- Password: `password`
- Port: `5432`

### 3. Test Data

Bij het opstarten wordt automatisch test data aangemaakt:

- **Gebruikers:**
  - Ruthger (Admin) - wachtwoord: `password123`
  - Edwin (User) - wachtwoord: `password123`
- **Publishers:** 35 uitgevers uit verschillende landen
- **Boardgames:** 5 populaire bordspellen

## 📋 API Endpoints

### Authentication

- `POST /api/auth/login` - Inloggen met username/password
- `POST /api/users/register` - Nieuwe gebruiker registreren

### Boardgames

- `GET /api/boardgames` - Alle bordspellen ophalen
- `GET /api/boardgames/{id}` - Bordspel op ID ophalen
- `GET /api/boardgames?genre=Strategy` - Filteren op genre
- `GET /api/boardgames?available=true` - Filteren op beschikbaarheid
- `POST /api/boardgames` - Nieuw bordspel toevoegen (authenticated)
- `PUT /api/boardgames/{id}` - Bordspel bijwerken (authenticated)
- `DELETE /api/boardgames/{id}` - Bordspel verwijderen (authenticated)

### Publishers

- `GET /api/publishers` - Alle uitgevers ophalen
- `GET /api/publishers/{id}` - Uitgever op ID ophalen
- `GET /api/publishers/country/{country}` - Filteren op land
- `POST /api/publishers` - Nieuwe uitgever toevoegen (authenticated)
- `PUT /api/publishers/{id}` - Uitgever bijwerken (authenticated)
- `DELETE /api/publishers/{id}` - Uitgever verwijderen (authenticated)

### Reservations

- `GET /api/reservations` - Alle reserveringen ophalen
- `GET /api/reservations/customer/{customerId}` - Reserveringen per klant
- `GET /api/reservations/boardgame/{boardgameId}` - Reserveringen per bordspel
- `POST /api/reservations` - Nieuwe reservering maken (authenticated)
- `DELETE /api/reservations/{id}` - Reservering annuleren (authenticated)

### File Management

- `POST /api/files/upload` - Bestand uploaden (authenticated)
- `GET /api/files/download/{filename}` - Bestand downloaden

### Users

- `GET /api/users/{id}` - Gebruiker op ID ophalen (authenticated)
- `DELETE /api/users/{id}` - Gebruiker verwijderen (authenticated)

### Health Check

- `GET /actuator/health` - Applicatie status controleren

## 🔐 Authenticatie

### JWT Token Ophalen

1. **Login als Ruthger (Admin):**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "Ruthger",
    "password": "password123"
  }'
```

2. **Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### JWT Token Gebruiken

Voeg de token toe aan de Authorization header:

```bash
curl -X GET http://localhost:8080/api/boardgames \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## 📊 Postman Collection

Importeer de `MeepleConnect_API_Collection.json` in Postman voor een complete set van vooraf geconfigureerde requests.

### Postman Variables

- `baseUrl`: `http://localhost:8080`
- `authToken`: JWT token (handmatig invullen na login)
- `filename`: Bestandsnaam voor download (handmatig invullen)

## 🧪 Test Scenarios

### Scenario 1: Publieke Endpoints Testen

1. Start de applicatie
2. Test `GET /actuator/health` - zou 200 OK moeten geven
3. Test `GET /api/boardgames` - zou lijst van bordspellen moeten geven
4. Test `GET /api/publishers` - zou lijst van uitgevers moeten geven

### Scenario 2: Authenticatie Testen

1. Test login met Ruthger: `POST /api/auth/login`
2. Kopieer de JWT token uit de response
3. Test een beschermde endpoint met de token

### Scenario 3: CRUD Operaties Testen

1. Login om een JWT token te krijgen
2. Test het toevoegen van een nieuw bordspel
3. Test het ophalen van het nieuwe bordspel
4. Test het bijwerken van het bordspel
5. Test het verwijderen van het bordspel

### Scenario 4: Filtering Testen

1. Test `GET /api/boardgames?genre=Strategy`
2. Test `GET /api/boardgames?available=true`
3. Test `GET /api/publishers/country/United States`

### Scenario 5: Reserveringen Testen

1. Login om een JWT token te krijgen
2. Maak een nieuwe reservering aan
3. Haal reserveringen op per klant
4. Annuleer een reservering

## 🔧 Troubleshooting

### Veelvoorkomende Problemen

1. **Database Connection Error**

   - Controleer of PostgreSQL draait
   - Controleer database credentials in `application.properties`

2. **401 Unauthorized**

   - Controleer of de JWT token geldig is
   - Controleer of de token correct is geformatteerd: `Bearer <token>`

3. **404 Not Found**

   - Controleer of de applicatie draait op `http://localhost:8080`
   - Controleer of de endpoint URL correct is

4. **500 Internal Server Error**
   - Controleer de applicatie logs
   - Controleer of alle dependencies correct zijn geïnstalleerd

### Logs Bekijken

De applicatie logs worden getoond in de console waar je `mvn spring-boot:run` hebt uitgevoerd.

## 📈 API Performance

- **Response Time:** < 100ms voor meeste endpoints
- **Concurrent Users:** Ondersteunt meerdere gelijktijdige requests
- **Database:** PostgreSQL met connection pooling

## 🛡️ Security Features

- JWT-based authenticatie
- Role-based autorisatie (ADMIN, USER)
- Password hashing met BCrypt
- CSRF protection uitgeschakeld voor API gebruik
- Stateless sessies

## 📝 API Documentation

Voor meer details over de API structuur, zie de Java source code in:

- Controllers: `src/main/java/com/meepleconnect/boardgamesapi/controllers/`
- Services: `src/main/java/com/meepleconnect/boardgamesapi/services/`
- Models: `src/main/java/com/meepleconnect/boardgamesapi/models/`

## 🎯 Clean Code & SOLID Principles

De applicatie volgt Clean Code en SOLID principes:

- **Single Responsibility:** Elke klasse heeft één verantwoordelijkheid
- **Open/Closed:** Uitbreidbaar zonder bestaande code te wijzigen
- **Liskov Substitution:** Interfaces worden correct geïmplementeerd
- **Interface Segregation:** Kleine, specifieke interfaces
- **Dependency Inversion:** Dependency injection gebruikt

### Gelaagdheid

- **Controllers:** HTTP request/response handling
- **Services:** Business logic
- **Repositories:** Data access layer
- **Models:** Data entities
- **Security:** JWT en autorisatie

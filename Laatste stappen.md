# 📋 Laatste Stappen - MeepleConnect API

## 🚨 **PRIORITEIT 1: Security Fix**

### Probleem Geïdentificeerd

- **Edwin (USER role)** kan momenteel boardgames toevoegen via POST `/api/boardgames`
- Dit zou alleen beschikbaar moeten zijn voor **ADMIN** roles
- Alle CRUD operaties op boardgames moeten ADMIN-only zijn

### 🔧 **CONCRETE ACTIE VEREIST**

#### 1. SecurityConfig.java Aanpassen

**Bestand:** `src/main/java/com/meepleconnect/boardgamesapi/security/SecurityConfig.java`

**Toevoegen aan `authorizeHttpRequests` sectie:**

```java
// Boardgames CRUD - alleen ADMIN
.requestMatchers(HttpMethod.POST, "/api/boardgames/**").hasRole("ADMIN")
.requestMatchers(HttpMethod.PUT, "/api/boardgames/**").hasRole("ADMIN")
.requestMatchers(HttpMethod.DELETE, "/api/boardgames/**").hasRole("ADMIN")

// Publishers CRUD - alleen ADMIN
.requestMatchers(HttpMethod.POST, "/api/publishers/**").hasRole("ADMIN")
.requestMatchers(HttpMethod.PUT, "/api/publishers/**").hasRole("ADMIN")
.requestMatchers(HttpMethod.DELETE, "/api/publishers/**").hasRole("ADMIN")
```

#### 2. Test de Fix

```bash
# 1. Start API opnieuw op
mvn spring-boot:run

# 2. Login als Edwin (USER)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "Edwin", "password": "password123"}'

# 3. Test POST boardgame (zou 403 Forbidden moeten geven)
curl -X POST http://localhost:8080/api/boardgames \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer [edwin_token]" \
  -d '{"name": "Test", "genre": "Test", "minPlayers": 1, "maxPlayers": 2, "price": 10.99, "available": true, "publisherId": 1}'

# Expected result: {"timestamp":"...","status":403,"error":"Forbidden",...}
```

#### 3. Verifieer ADMIN Still Works

```bash
# 1. Login als Ruthger (ADMIN)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "Ruthger", "password": "password123"}'

# 2. Test POST boardgame (zou moeten werken)
curl -X POST http://localhost:8080/api/boardgames \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer [ruthger_token]" \
  -d '{"name": "Admin Test", "genre": "Test", "minPlayers": 1, "maxPlayers": 2, "price": 10.99, "available": true, "publisherId": 1}'

# Expected result: {"id":...,"name":"Admin Test",...}
```

---

## 📈 **OPTIONELE VERBETERINGEN**

### 4. Extra Security Verification

- [ ] Test alle CRUD operaties met beide roles
- [ ] Verifieer reservations permissions
- [ ] Check user management endpoints

### 5. Data Cleanup

- [ ] Verwijder test boardgames (ID 41, 42) toegevoegd tijdens testing
- [ ] Reset database naar clean state indien gewenst

### 6. Final Testing

- [ ] Run integration tests: `mvn test`
- [ ] Test complete Postman collection
- [ ] Verifieer alle endpoints in documentation

---

## ⏱️ **TIJDSINSCHATTING**

| Taak                       | Geschatte Tijd    |
| -------------------------- | ----------------- |
| Security fix implementeren | 5-10 minuten      |
| Testing van fix            | 10-15 minuten     |
| Cleanup (optioneel)        | 5-10 minuten      |
| **TOTAAL**                 | **20-35 minuten** |

---

## 🎯 **RESULTAAT**

Na deze fix:

- **Huidige score:** 8.5/10
- **Na security fix:** 9.5/10
- **Perfect role-based access control**
- **Production-ready API**

---

## ✅ **WAT AL PERFECT IS**

- ✅ JWT authenticatie en authorization basis
- ✅ Complete CRUD functionaliteit
- ✅ Excellent data modeling
- ✅ Professional error handling
- ✅ Search en filtering capabilities
- ✅ Statistics dashboard
- ✅ Health monitoring
- ✅ Comprehensive documentation
- ✅ Easter eggs voor extra punten!

**Je API is al zeer professioneel - deze ene security fix maakt het compleet! 🏆**

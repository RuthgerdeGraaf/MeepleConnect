# Authenticatie en Autorisatie Testen

## Probleem Opgelost

Het probleem was dat je zowel method-level security (met `@PreAuthorize` annotaties) als URL-based security had geconfigureerd, maar method security was niet geactiveerd. Dit veroorzaakte conflicten.

## Wijzigingen

1. **Method Security Geactiveerd**: `@EnableMethodSecurity` toegevoegd aan `SecurityConfig`
2. **Rollen Uitgebreid**: `EMPLOYEE` en `CUSTOMER` rollen toegevoegd aan de `Role` enum
3. **URL Security Vereenvoudigd**: Conflicterende URL-based security verwijderd
4. **Test Gebruikers Toegevoegd**: Gebruikers voor alle rollen toegevoegd aan `data.sql`

## Test Gebruikers

| Username  | Password | Role     | Beschrijving     |
| --------- | -------- | -------- | ---------------- |
| Ruthger   | admin123 | ADMIN    | Beheerder        |
| Jeroen    | user123  | USER     | Gewone gebruiker |
| employee1 | admin123 | EMPLOYEE | Medewerker       |
| customer1 | admin123 | CUSTOMER | Klant            |

## Testen van Authenticatie

### 1. Login Testen

```bash
# Login als employee
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"employee1","password":"admin123"}'

# Login als customer
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"customer1","password":"admin123"}'
```

### 2. Geautoriseerde Endpoints Testen

#### Employee Endpoints (vereisen EMPLOYEE rol)

```bash
# JWT token ophalen
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"employee1","password":"admin123"}' | jq -r '.jwtToken')

# Boardgame toevoegen (alleen EMPLOYEE)
curl -X POST http://localhost:8080/api/boardgames \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Game","genre":"Strategy","minPlayers":2,"maxPlayers":4}'

# File uploaden (alleen EMPLOYEE)
curl -X POST http://localhost:8080/api/files/upload \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@testfile.txt"
```

#### Customer Endpoints (vereisen CUSTOMER rol)

```bash
# JWT token ophalen
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"customer1","password":"admin123"}' | jq -r '.jwtToken')

# Reservering maken (alleen CUSTOMER)
curl -X POST "http://localhost:8080/api/reservations?customerId=1&boardgameId=1&reservationDate=2024-12-25&participantCount=4" \
  -H "Authorization: Bearer $TOKEN"
```

### 3. Toegang Verzegeld Testen

```bash
# Proberen boardgame toe te voegen als CUSTOMER (moet falen)
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"customer1","password":"admin123"}' | jq -r '.jwtToken')

curl -X POST http://localhost:8080/api/boardgames \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Game","genre":"Strategy","minPlayers":2,"maxPlayers":4}'
# Moet 403 Forbidden retourneren
```

## Security Configuratie

### Method Security (Aanbevolen)

- Gebruik `@PreAuthorize` annotaties in controllers
- Meer flexibel en type-safe
- Makkelijker te onderhouden

### URL Security (Basis)

- Configureer in `SecurityConfig`
- Voor algemene toegangsregels
- Minder flexibel

## Best Practices

1. **Gebruik Method Security** voor endpoint-specifieke autorisatie
2. **Gebruik URL Security** alleen voor algemene toegangsregels
3. **Vermijd conflicten** door niet beide te gebruiken voor dezelfde endpoints
4. **Test altijd** met verschillende rollen
5. **Gebruik betekenisvolle rollen** (EMPLOYEE, CUSTOMER, ADMIN)

## Troubleshooting

Als je nog steeds problemen hebt:

1. Controleer of de applicatie opnieuw is opgestart
2. Controleer of de database is bijgewerkt met nieuwe rollen
3. Controleer de logs voor security-gerelateerde fouten
4. Test met Postman of een andere API client

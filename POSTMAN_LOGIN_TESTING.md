# Postman Login Testing Guide

## Prerequisites

- Postman installed and running
- Your Spring Boot application running on `http://localhost:8080`
- Test users available in the database

## Step 1: Start Your Application

First, make sure your Spring Boot application is running:

```bash
mvn spring-boot:run
```

Your API should be available at `http://localhost:8080`

## Step 2: Create a New Collection

1. Open Postman
2. Click "New" → "Collection"
3. Name it "MeepleConnect API Tests"
4. Click "Create"

## Step 3: Test User Registration (Optional)

If you want to create a new test user:

### Create Registration Request

1. Click "Add request" in your collection
2. Name it "Register User"
3. Set method to `POST`
4. Set URL to `http://localhost:8080/api/users/register`
5. Go to "Headers" tab and add:
   - Key: `Content-Type`
   - Value: `application/json`
6. Go to "Body" tab, select "raw" and "JSON", then add:

```json
{
  "username": "testuser",
  "password": "password123",
  "role": "USER"
}
```

7. Click "Send"
8. You should get a 200 OK response with the created user

## Step 4: Test Login with Valid Credentials

### Create Login Request

1. Click "Add request" in your collection
2. Name it "Login - Valid Credentials"
3. Set method to `POST`
4. Set URL to `http://localhost:8080/api/auth/login`
5. Go to "Headers" tab and add:
   - Key: `Content-Type`
   - Value: `application/json`
6. Go to "Body" tab, select "raw" and "JSON", then add:

```json
{
  "username": "testuser",
  "password": "password123"
}
```

7. Click "Send"

### Expected Response

You should get a 200 OK response with a JWT token:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## Step 5: Test Login with Invalid Credentials

### Create Invalid Login Request

1. Click "Add request" in your collection
2. Name it "Login - Invalid Credentials"
3. Set method to `POST`
4. Set URL to `http://localhost:8080/api/auth/login`
5. Go to "Headers" tab and add:
   - Key: `Content-Type`
   - Value: `application/json`
6. Go to "Body" tab, select "raw" and "JSON", then add:

```json
{
  "username": "employee1",
  "password": "wrongpassword"
}
```

7. Click "Send"

### Expected Response

You should get a 401 Unauthorized response.

## Step 6: Test Protected Endpoints with JWT Token

### Create Protected Request

1. Click "Add request" in your collection
2. Name it "Get All Boardgames (Protected)"
3. Set method to `GET`
4. Set URL to `http://localhost:8080/api/boardgames`
5. Go to "Headers" tab and add:
   - Key: `Authorization`
   - Value: `Bearer YOUR_JWT_TOKEN_HERE` (replace with the token from step 4)
6. Click "Send"

### Expected Response

You should get a 200 OK response with the list of boardgames.

## Step 7: Test Role-Based Access

### Test Employee-Only Endpoint

1. First, login as an employee to get a token:

```json
{
  "username": "employee1",
  "password": "admin123"
}
```

2. Create a new request to test employee-only access:
   - Name: "Add Boardgame (Employee Only)"
   - Method: `POST`
   - URL: `http://localhost:8080/api/boardgames`
   - Headers:
     - `Content-Type`: `application/json`
     - `Authorization`: `Bearer YOUR_EMPLOYEE_TOKEN`
   - Body:

```json
{
  "name": "Test Game",
  "genre": "Strategy",
  "minPlayers": 2,
  "maxPlayers": 4,
  "available": true,
  "price": 29.99
}
```

3. Click "Send" - should return 201 Created

### Test Customer Access to Employee Endpoint

1. Login as a customer:

```json
{
  "username": "customer1",
  "password": "admin123"
}
```

2. Try the same boardgame creation request with the customer token
3. Click "Send" - should return 403 Forbidden

## Available Test Users

| Username    | Password   | Role     | Description              |
| ----------- | ---------- | -------- | ------------------------ |
| `employee1` | `admin123` | EMPLOYEE | Can access all endpoints |
| `customer1` | `admin123` | CUSTOMER | Limited access           |
| `Ruthger`   | `admin123` | ADMIN    | Full access              |
| `Jeroen`    | `user123`  | USER     | Basic access             |

## Step 8: Test Different Roles

### Test Customer-Specific Endpoint

1. Login as customer1 to get a token
2. Create a reservation (customer-only endpoint):
   - Method: `POST`
   - URL: `http://localhost:8080/api/reservations?customerId=1&boardgameId=1&reservationDate=2024-12-25&participantCount=4`
   - Headers: `Authorization: Bearer YOUR_CUSTOMER_TOKEN`
3. Click "Send" - should return 200 OK

### Test Employee Access to Customer Endpoint

1. Login as employee1 to get a token
2. Try the same reservation request with employee token
3. Click "Send" - should return 403 Forbidden

## Troubleshooting

### Common Issues:

1. **401 Unauthorized**: Check username/password
2. **403 Forbidden**: User doesn't have required role
3. **500 Internal Server Error**: Check if application is running
4. **Connection refused**: Make sure your app is running on port 8080

### Tips:

- Always copy the JWT token exactly as received
- Include "Bearer " prefix in Authorization header
- Check the response body for detailed error messages
- Use the test users provided in the table above

## Next Steps

Once login is working, you can test other endpoints:

- File upload/download (EMPLOYEE only)
- User management (ADMIN only)
- Reservation management (CUSTOMER/EMPLOYEE)
- Boardgame CRUD operations (EMPLOYEE only)

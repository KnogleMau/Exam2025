# Trip Planning Api
This is a simple project, where you can see a trip, and then the guide to the trip, you will also be able to see what you should bring on your trip, so you will also get a packing list, if you dont have the things on the packing list, you will be provided with link and price to the diffrent items, which you should bring along.


## Tech
- Build with Java -- Version Amazon Corretto 17
- Maven
- Javalin(Web Framework)
- Postgres
- JPA/Hibernate


## How to run the project
1. Clone the project from my github
2. This is the link you should use when cloning the project https://github.com/KnogleMau/Mock-Exam.git
3. When the project is cloned, you start the Application in Main.java
4. For the project to work correctly you should make a config.properties with these env in the
   -  SECRET_KEY=("Need to be 32 long something like this (ASD123WE3123SD123SDADS321HJB23P) Just make sure its 32 characters long")
   -  ISSUER=("Your name")
   -  TOKEN_EXPIRE_TIME=1800000 (Keep the Token Expire time to what it is, it should me 30 min before the time runs out)
   -  DB_NAME=(Databasen name)
   -  DB_USERNAME=(Your Username)
   -  DB_PASSWORD=(Your password)
5. Once you have your databasen running and the enviroments in your config.properties, you should be able to run the project


## API Endpoints

Base URL:  
**http://localhost:7007/api**

> All protected Routes require a valid **JWT Bearer Token** in the `Authorization` header.
>
> **Roles:**
> -  **ADMIN** – full access (create, update, delete)
> -  **USER** – read-only access to trips and guides

---

### Security Endpoints

| Method | Endpoint | Description | Requires Token | Allowed Roles |
|---------|-----------|--------------|----------------|----------------|
| **POST** | `/auth/register` | Register a new user | ❌ | Public |
| **POST** | `/auth/login` | Log in and receive a JWT token | ❌ | Public |
| **GET** | `/protected/user_demo` | Test Routes for users | ✅ | USER |
| **GET** | `/protected/admin_demo` | Test Routes for admins | ✅ | ADMIN |

---

### Trip Endpoints

| Method | Endpoint | Description | Requires Token | Allowed Roles |
|---------|-----------|--------------|----------------|----------------|
| **GET** | `/trips` | Get all trips or filter by `?category=CITY` | ✅ | USER, ADMIN |
| **GET** | `/trips/{id}` | Get a specific trip by ID | ✅ | USER, ADMIN |
| **GET** | `/trips/guides/totalprice` | Get total price for all guides | ✅ | USER, ADMIN |
| **GET** | `/trips/{id}/packing/weight` | Get total packing weight for a trip | ✅ | USER, ADMIN |
| **POST** | `/trips` | Create a new trip | ✅ | ADMIN |
| **PUT** | `/trips/{id}` | Update an existing trip | ✅ | ADMIN |
| **PUT** | `/trips/{tripId}/guides/{guideId}` | Add a guide to a trip | ✅ | ADMIN |
| **DELETE** | `/trips/{id}` | Delete a trip | ✅ | ADMIN |

---

### Guide Endpoints

| Method | Endpoint | Description | Requires Token | Allowed Roles |
|---------|-----------|--------------|----------------|----------------|
| **GET** | `/guides` | Retrieve all guides | ✅ | USER, ADMIN |

### Json Format

```json
When you want to POST/UPDATE a trip you need the correct format to do it.

Trip Json format
{
"name": "Copenhagen",
"startTime": "2026-04-20T09:00",
"endTime": "2026-04-20T17:00",
"location": "Denmark",
"price": 1200,
"category": "CITY"
}


If you want to register a new user or login this is the format you will use.
Register/Login Json format
{
    "username": "user",
    "password": "pass12345"
}
# Candidate Matcher Application
This project is a backend system for a recruitment platform helping companies match candidates to relevant skills and technologies.


## Tech
- Build with Java -- Version Amazon Corretto 17
- Maven
- Javalin(Web Framework)
- Postgres
- JPA/Hibernate


## How to run the project
1. Clone the project from my github
2. This is the link you should use when cloning the project https://github.com/KnogleMau/Exam2025.git
3. When the project is cloned, you start the Application in Main.java
4. For the project to work correctly you should make a config.properties with these env in the
   -  SECRET_KEY=("Need to be 32 long something like this (ASD123WE3123SD123SDADS321HJB23P) Just make sure its 32 characters long")
   -  ISSUER=("Your name")
   -  TOKEN_EXPIRE_TIME=1800000 (Keep the Token Expire time to what it is, it should me 30 min before the time runs out)
   -  DB_NAME=(Databasen name)
   -  DB_USERNAME=(Your Username)
   -  DB_PASSWORD=(Your password)
5. Once you have your databasen running and the enviroments in your config.properties, you should be able to run the project


## User Stories

User Stories implementet
- US 1 Implementet
- US 2 Implementet
- US 3 Implementet
- US 4 Implementet
- US 5 Implementet
- US 6 Implementet (But i have changed the route so its just GET /candidates/top-by-popularity, which you also can se in my dokumentation)
- US 7 Implementet (I have implementet alot of the test, but i am still missing some of the tests)
- US 8 Implementet (But my tests dont verify secure access behavior, because i didnt have the time, to setup the login, you can enable the security in the test and watch them all fail because of the missing token, you can also test it in my dev.http)




## API Endpoints

Base URL:  
**http://localhost:7007/api**

> All protected Routes require a valid **JWT Bearer Token** in the `Authorization` header.
>
> **Roles:**
> -  **ADMIN** – full access (create, update, delete)
> -  **USER** – read-only access to Candidates and Skills

---

### Security Endpoints

| Method | Endpoint | Description | Requires Token | Allowed Roles |
|---------|-----------|--------------|----------------|----------------|
| **POST** | `/auth/register` | Register a new user | ❌ | Public |
| **POST** | `/auth/login` | Log in and receive a JWT token | ❌ | Public |
| **GET** | `/protected/user_demo` | Test Routes for users | ✅ | USER |
| **GET** | `/protected/admin_demo` | Test Routes for admins | ✅ | ADMIN |

---

### Candidate Endpoints

| Method | Endpoint | Description | Requires Token | Allowed Roles |
|---------|-----------|--------------|----------------|----------------|
| **GET** | `/candidates` | Get all candidates or filter by `?category=FRAMEWORK` | ✅ | USER, ADMIN |
| **GET** | `/candidates/{id}` | Get a specific candidate by ID (includes enriched skill data) | ✅ | USER, ADMIN |
| **GET** | `/candidates/top-by-popularity` | Get candidates ranked by average popularity score | ✅ | USER, ADMIN |
| **POST** | `/candidates` | Create a new candidate | ✅ | ADMIN |
| **PUT** | `/candidates/{id}` | Update an existing candidate | ✅ | ADMIN |
| **PUT** | `/candidates/{candidateId}/skills/{skillId}` | Add a skill to a candidate | ✅ | ADMIN |
| **DELETE** | `/candidates/{id}` | Delete a candidate | ✅ | ADMIN |

---

### Skill Endpoints

| Method | Endpoint | Description | Requires Token | Allowed Roles |
|---------|-----------|--------------|----------------|----------------|
| **GET** | `/skill` | Retrieve all skills | ✅ | USER, ADMIN |
| **POST** | `/skill` | Create a new skill | ✅ | ADMIN |

### Json Format

```json
When you want to POST/UPDATE a Candidate you need the correct format to do it.

Candidat Json format
{
"name": "Rasmus",
"phoneNumber": "91919191",
"educationBackground":"Ek"
}


If you want to register a new user or login this is the format you will use.
Register/Login Json format
{
    "username": "user",
    "password": "pass12345"
}

# Expense Manager – Backend (Spring Boot)

Spring Boot (3.5.x) + Java 21 + Postgres API for managing expenses with categories and tags, optimized for Angular integration.

---

## ✨ Features
- CRUD for Expenses
- Filtering by date range, category, amount, and tags
- Pagination & sorting
- OpenAPI (Swagger UI)
- Flyway migrations (optional)

---

## 🧱 Tech Stack
- **Java** 21
- **Spring Boot** 3.5.x
- **Spring Web**, **Spring Data JPA**, **Validation**, **Lombok**
- **PostgreSQL**
- **Gradle**

---

## 📦 Prerequisites
- JDK 21
- Gradle Wrapper
- PostgreSQL 15+

---

## 🗄️ Database
```sql
CREATE DATABASE expense_manager;
CREATE USER expense_user WITH ENCRYPTED PASSWORD 'expense_pass';
GRANT ALL PRIVILEGES ON DATABASE expense_manager TO expense_user;
```

---

## ⚙️ Configuration
`src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/expense_manager
    username: expense_user
    password: expense_pass
  jpa:
    hibernate:
      ddl-auto: update
    open-in-view: false
  jackson:
    default-property-inclusion: non_null

server:
  port: 8080

app:
  cors:
    allowed-origins: "http://localhost:4200"
```

---

## 🧰 Build & Run
```bash
./gradlew clean build
./gradlew bootRun
```

---

## 🧭 REST Endpoints
**Base URL:** `/api`

### Expenses
- GET `/api/expenses`
- GET `/api/expenses/{id}`
- POST `/api/expenses`
- PUT `/api/expenses/{id}`
- DELETE `/api/expenses/{id}`

Example Payload:
```json
{
  "description": "Dinner at Spice Garden",
  "amount": 850.00,
  "currency": "INR",
  "occurredOn": "2025-11-01",
  "category": "FOOD",
  "tags": ["date-night", "restaurant"],
  "notes": "10% coupon applied"
}
```

---

## 🔍 Swagger
Add dependency:
```kts
implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
```
Then visit: `http://localhost:8080/swagger-ui.html`

---

## 🐳 Docker
```yaml
version: "3.9"
services:
  db:
    image: postgres:16
    environment:
      POSTGRES_DB: expense_manager
      POSTGRES_USER: expense_user
      POSTGRES_PASSWORD: expense_pass
    ports:
      - "5432:5432"
volumes:
  pgdata:
```

---

## 🤝 Run Backend
1. Start Postgres
2. Run backend: `./gradlew bootRun`
3. Visit [http://localhost:8080](http://localhost:8080)


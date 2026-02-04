# Bank API Backend

A Spring Boot REST API for managing bank accounts with authentication & authorization.

## Features
- User registration & login (JWT)
- Role-based access (ADMIN / CUSTOMER)
- Refresh token support
- Account operations: credit, debit, transfer
- Admin dashboard: logs, transactions, user management
- API documentation via Swagger/OpenAPI

## How to Run
1. Clone repo: `git clone ...`
2. Set your `.env` or `application.properties` with DB and JWT secrets
3. Run: `mvn spring-boot:run`
4. Open Swagger UI: `http://localhost:8080/swagger-ui.html`

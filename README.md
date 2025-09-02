# Commerce API - Product & Order Management System

A RESTful web application demonstrating professional Java development practices using Spring Boot with hexagonal architecture, CQRS, and automated code quality controls.

## 🏗️ Architecture

**Hexagonal Architecture (Ports & Adapters)** with **CQRS** pattern:

```
src/main/java/cz/rohlik/commerce/
├── domain/                 # Core business logic
│   ├── common/            # Base entities, repositories
│   └── model/             # Domain models by context
├── application/           # Use cases & business workflows
│   ├── common/           # CQRS commands & queries
│   └── module/           # Application modules
├── adapter/              # External interfaces
│   ├── input/rest/       # REST controllers
│   └── output/           # Database adapters
└── infrastructure/       # Configuration & cross-cutting
    ├── config/           # App configuration
    └── exception/        # Global error handling
```

## 🚀 Quick Start

### Prerequisites
- **Java 21+**
- **Gradle 8+**

### Run Application
```bash
./gradlew bootRun
```

### Build & Test
```bash
./gradlew build                    # Build + run all checks
./gradlew test                     # Run tests only
./gradlew editorconfigCheck        # Check code formatting
./gradlew editorconfigFormat       # Auto-fix formatting
```

### Access Points
- **Application**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:commercedb`
  - Username: `sa`, Password: `password`
- **Health Check**: http://localhost:8080/actuator/health

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.5
- **Language**: Java 21
- **Database**: H2 (in-memory)
- **Migration**: Flyway
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **API Documentation**: OpenAPI 3 (Swagger)
- **Build Tool**: Gradle 8
- **Code Quality**: EditorConfig with automated checks

## 🎯 Code Quality

### Formatting Standards
- **Indentation**: 4 spaces (Java), 2 spaces (YAML/JSON/SQL)
- **Line Length**: 130 characters max
- **Encoding**: UTF-8 with Unix line endings (LF)
- **Imports**: No wildcards, organized by type
- **Whitespace**: No trailing spaces, final newline required

## 🧪 Testing

### Test Structure
```
src/test/java/cz/rohlik/commerce/
├── domain/               # Domain unit tests
├── application/          # Application layer tests
├── adapter/             # Controller & persistence tests
├── integration/         # Full integration tests
├── ControllerTest.java  # Base class for controller tests
├── IntegrationTest.java # Base class for integration tests
└── TestUtils.java       # Test utilities
```

---


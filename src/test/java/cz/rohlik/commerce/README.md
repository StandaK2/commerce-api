# Test Infrastructure

This directory contains a simplified, focused test infrastructure for the Commerce API application, optimized for H2 database testing and clean, maintainable test code.

## Test Structure

```
src/test/java/cz/rohlik/commerce/
├── IntegrationTest.java             # Base class for integration tests (H2)
├── ControllerTest.java              # Base class for controller tests
├── IntegrationTestDataHelper.java   # Helper for creating test data
├── TestUtils.java                   # Utility methods for testing
└── CommerceApplicationTest.java     # Main application integration test
```

## Test Types

### 1. Unit Tests
- **Purpose**: Testing isolated components with no external dependencies
- **Speed**: Very fast execution
- **Database**: No database required
- **Dependencies**: Mocked using Mockito
- **Usage**: Standard JUnit 5 tests with `@ExtendWith(MockitoExtension.class)`

### 2. Integration Tests
- **Purpose**: Testing components with real database and Spring context
- **Speed**: Moderate execution time
- **Database**: H2 in-memory database
- **Base Class**: `IntegrationTest`
- **Features**:
  - Full Spring Boot context loading
  - Real database transactions
  - Automatic cleanup between tests
  - Test profile configuration

### 3. Controller Tests
- **Purpose**: Testing REST endpoints in isolation
- **Speed**: Fast execution
- **Database**: No real database (mocked services)
- **Base Class**: `ControllerTest`
- **Features**:
  - MockMvc for HTTP testing
  - Mocked command/query buses
  - JSON serialization testing
  - Authentication testing support

## Base Classes

### IntegrationTest
```java
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
public abstract class IntegrationTest {
    // H2 database setup and cleanup utilities
}
```

### ControllerTest
```java
@WebMvcTest
@ActiveProfiles("test")
public abstract class ControllerTest {
    @Autowired protected MockMvc mockMvc;
    @MockBean protected CommandBus commandBus;
    @MockBean protected QueryBus queryBus;
}
```

## Test Utilities

### TestUtils.java
- MockMvc helper methods for common HTTP operations
- UUID generation for predictable test data
- JSON assertion utilities

### IntegrationTestDataHelper.java
- Factory methods for creating test entities
- Database seeding utilities
- Test data cleanup methods

## Configuration

### H2 Database
```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:commercedb-test
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.baseline-on-migrate=true
```

**Key Benefits:**
- **Fast**: In-memory database for quick test execution
- **Isolated**: Each test gets a clean database state
- **Realistic**: Uses same schema as production via Flyway
- **Perfect for**: Commerce API development and CI/CD pipelines

## Usage Examples

### Unit Test
```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock private ProductRepository repository;
    @InjectMocks private ProductService service;

    @Test void shouldCreateProduct() { /* test logic */ }
}
```

### Integration Test
```java
class ProductIntegrationTest extends IntegrationTest {
    @Autowired private ProductRepository repository;

    @Test void shouldPersistProduct() { /* test logic */ }
}
```

### Controller Test
```java
class ProductControllerTest extends ControllerTest {
    @Test void shouldCreateProduct() throws Exception {
        mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{"name": "Test Product"}"""))
            .andExpect(status().isCreated());
    }
}
```

## Design Philosophy

1. **Simplicity Over Complexity**: H2 instead of TestContainers for faster feedback
2. **Isolation**: Each test runs independently with clean state
3. **Maintainability**: Clear base classes and utilities reduce boilerplate
4. **Performance**: Optimized for quick development cycles
5. **Perfect for Commerce API**: Focused on core functionality without complexity

## Future Extensions

While simplified for the Commerce API, this infrastructure can be easily extended:
- Add TestContainers for PostgreSQL integration tests
- Implement custom test slices for specific layers
- Add performance testing utilities
- Integrate with test reporting tools

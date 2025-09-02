package cz.rohlik.commerce;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import cz.rohlik.commerce.application.common.command.CommandBus;
import cz.rohlik.commerce.application.common.query.QueryBus;

/**
 * Base class for integration tests.
 * Use for testing components with their dependencies.
 * Database interaction via H2 in-memory database.
 * Spring context loading with real dependencies.
 *

 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected IntegrationTestDataHelper testDataHelper;

    @Autowired
    protected QueryBus queryBus;

    @Autowired
    protected CommandBus commandBus;

    @BeforeEach
    void setUpIntegrationTest() {
        // Override in subclasses if needed
    }

    @AfterEach
    void cleanUpIntegrationTest() {
        // Clean database after each test
        cleanDatabase();
    }

    /**
     * Cleans all tables except Flyway schema history.
     * Optimized for an H2 database.
     */
    private void cleanDatabase() {
        // Disable foreign key checks for cleanup
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        // Get all table names except flyway_schema_history
        var tableNames = jdbcTemplate.queryForList(
            "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES " +
            "WHERE TABLE_SCHEMA = 'PUBLIC' AND TABLE_NAME != 'flyway_schema_history'",
            String.class
        );

        // Truncate all tables
        for (String tableName : tableNames) {
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName + " RESTART IDENTITY");
        }

        // Re-enable foreign key checks
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}

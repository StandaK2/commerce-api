package cz.rohlik.commerce;

import cz.rohlik.commerce.application.common.command.CommandBus;
import cz.rohlik.commerce.application.common.query.QueryBus;
import cz.rohlik.commerce.application.module.product.ProductFinderService;
import cz.rohlik.commerce.domain.model.order.OrderCreateService;
import cz.rohlik.commerce.domain.model.order.OrderRepository;
import cz.rohlik.commerce.domain.model.orderitem.OrderItemCreateService;
import cz.rohlik.commerce.domain.model.orderitem.OrderItemRepository;
import cz.rohlik.commerce.domain.model.product.ProductCreateService;
import cz.rohlik.commerce.domain.model.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for integration tests. Use for testing components with their dependencies. Database
 * interaction via H2 in-memory database. Spring context loading with real dependencies.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTest {

    @Autowired protected JdbcTemplate jdbcTemplate;

    @Autowired protected IntegrationTestDataHelper testDataHelper;

    @Autowired protected QueryBus queryBus;

    @Autowired protected CommandBus commandBus;

    @Autowired protected ProductFinderService productFinderService;

    @Autowired protected ProductCreateService productCreateService;

    @Autowired protected OrderCreateService orderCreateService;

    @Autowired protected OrderItemCreateService orderItemCreateService;

    @Autowired protected ProductRepository productRepository;

    @Autowired protected OrderRepository orderRepository;

    @Autowired protected OrderItemRepository orderItemRepository;

    @BeforeEach
    void setUpIntegrationTest() {
        // Override in subclasses if needed
    }

    @AfterEach
    void cleanUpIntegrationTest() {
        cleanDatabase();
    }

    /** Cleans all tables except Flyway schema history. Optimized for an H2 database. */
    private void cleanDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        var tableNames =
                jdbcTemplate.queryForList(
                        "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES "
                                + "WHERE TABLE_SCHEMA = 'PUBLIC' AND TABLE_NAME != 'flyway_schema_history'",
                        String.class);

        for (String tableName : tableNames) {
            jdbcTemplate.execute("TRUNCATE TABLE \"" + tableName + "\" RESTART IDENTITY");
        }

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}

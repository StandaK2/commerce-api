package cz.rohlik.commerce;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Helper class for creating test data in integration tests.
 * Currently minimal - will be extended when domain entities are implemented.
 */
@Component
@Transactional
public class IntegrationTestDataHelper {

    // TODO: Add factory methods for creating test entities when domain layer is implemented
    // For example:
    // public Product createDefaultProduct() { ... }
    // public Order createDefaultOrder() { ... }
}

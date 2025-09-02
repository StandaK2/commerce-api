package cz.rohlik.commerce;

import cz.rohlik.commerce.application.common.command.CommandBus;
import cz.rohlik.commerce.application.common.query.QueryBus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Base class for controller tests. Use for testing REST endpoints in isolation. MockMvc for
 * simulating HTTP requests. Command/Query buses are mocked.
 */
@WebMvcTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public abstract class ControllerTest {

    @Autowired protected MockMvc mockMvc;

    @Mock protected CommandBus commandBus;

    @Mock protected QueryBus queryBus;

    @TestConfiguration
    static class ControllerTestConfig {
        @Bean
        @Primary
        public CommandBus commandBus() {
            return Mockito.mock(CommandBus.class);
        }

        @Bean
        @Primary
        public QueryBus queryBus() {
            return Mockito.mock(QueryBus.class);
        }
    }

    @BeforeEach
    void setUpControllerTest() {
        // Override in subclasses if needed
    }

    @AfterEach
    void cleanUpControllerTest() {
        // Verify all mocks and reset them
        Mockito.verifyNoMoreInteractions(commandBus, queryBus);
        Mockito.reset(commandBus, queryBus);
    }

    /**
     * Helper method to create JSON content for request bodies.
     *
     * @param content The JSON content as string
     * @return Formatted JSON content
     */
    protected String jsonContent(String content) {
        return content.trim();
    }
}

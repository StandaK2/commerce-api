package cz.rohlik.commerce;

import cz.rohlik.commerce.application.common.command.CommandBus;
import cz.rohlik.commerce.application.common.query.QueryBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ActiveProfiles("test")
public abstract class ControllerTest {

    @Autowired protected MockMvc mockMvc;

    @MockitoBean protected CommandBus commandBus;

    @MockitoBean protected QueryBus queryBus;
}

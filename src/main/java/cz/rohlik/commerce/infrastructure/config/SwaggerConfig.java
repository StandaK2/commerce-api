package cz.rohlik.commerce.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Swagger/OpenAPI configuration for the Commerce API application. */
@Configuration
@ConditionalOnProperty(
        name = "springdoc.api-docs.enabled",
        havingValue = "true",
        matchIfMissing = true)
public class SwaggerConfig {

    @Bean
    public OpenAPI openApiConfiguration() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Commerce API")
                                .version("1.0.0")
                                .description("Product and Order Management API for Commerce System")
                                .contact(
                                        new Contact()
                                                .email("stanislav.koumar@gmail.com")
                                                .name("Stanislav Koumar")))
                .addServersItem(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder().group("commerce-api").pathsToMatch("/api/**").build();
    }
}

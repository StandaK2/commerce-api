package cz.rohlik.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the Commerce API application. Demonstrates hexagonal architecture with
 * CQRS patterns for product and order management.
 */
@SpringBootApplication
@EnableScheduling
public class CommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceApplication.class, args);
    }
}

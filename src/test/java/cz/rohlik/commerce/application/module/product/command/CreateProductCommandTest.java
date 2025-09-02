package cz.rohlik.commerce.application.module.product.command;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateProductCommandTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldPassValidationForValidCommand() {
        var command = new CreateProductCommand("Valid Product", new BigDecimal("99.99"), 50);

        Set<ConstraintViolation<CreateProductCommand>> violations = validator.validate(command);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationForBlankName() {
        var command = new CreateProductCommand("", new BigDecimal("99.99"), 50);

        Set<ConstraintViolation<CreateProductCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("name");
    }

    @Test
    void shouldFailValidationForNegativePrice() {
        var command = new CreateProductCommand("Valid Product", new BigDecimal("-1.00"), 50);

        Set<ConstraintViolation<CreateProductCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("price");
    }

    @Test
    void shouldFailValidationForNegativeStock() {
        var command = new CreateProductCommand("Valid Product", new BigDecimal("99.99"), -1);

        Set<ConstraintViolation<CreateProductCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString())
                .contains("stockQuantity");
    }
}

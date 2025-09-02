package cz.rohlik.commerce.application.module.product.command;

import static org.assertj.core.api.Assertions.assertThat;

import cz.rohlik.commerce.TestUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateProductCommandTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldPassValidationForValidCommand() {
        var command =
                new UpdateProductCommand(
                        TestUtils.createUuidFromNumber(1),
                        "Valid Product",
                        new BigDecimal("99.99"),
                        50);

        Set<ConstraintViolation<UpdateProductCommand>> violations = validator.validate(command);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationForNullProductId() {
        var command = new UpdateProductCommand(null, "Valid Product", new BigDecimal("99.99"), 50);

        Set<ConstraintViolation<UpdateProductCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("productId");
    }

    @Test
    void shouldFailValidationForBlankName() {
        var command =
                new UpdateProductCommand(
                        TestUtils.createUuidFromNumber(1), "", new BigDecimal("99.99"), 50);

        Set<ConstraintViolation<UpdateProductCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("name");
    }

    @Test
    void shouldFailValidationForNegativePrice() {
        var command =
                new UpdateProductCommand(
                        TestUtils.createUuidFromNumber(1),
                        "Valid Product",
                        new BigDecimal("-1.00"),
                        50);

        Set<ConstraintViolation<UpdateProductCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("price");
    }

    @Test
    void shouldFailValidationForNegativeStock() {
        var command =
                new UpdateProductCommand(
                        TestUtils.createUuidFromNumber(1),
                        "Valid Product",
                        new BigDecimal("99.99"),
                        -1);

        Set<ConstraintViolation<UpdateProductCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString())
                .contains("stockQuantity");
    }
}

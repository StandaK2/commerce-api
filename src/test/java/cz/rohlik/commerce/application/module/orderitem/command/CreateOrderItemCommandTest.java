package cz.rohlik.commerce.application.module.orderitem.command;

import static org.assertj.core.api.Assertions.assertThat;

import cz.rohlik.commerce.TestUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateOrderItemCommandTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldPassValidationForValidCommand() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var productId = TestUtils.createUuidFromNumber(2);
        var command = new CreateOrderItemCommand(orderId, productId, 5);

        Set<ConstraintViolation<CreateOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationForNullOrderId() {
        var productId = TestUtils.createUuidFromNumber(2);
        var command = new CreateOrderItemCommand(null, productId, 5);

        Set<ConstraintViolation<CreateOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("orderId");
    }

    @Test
    void shouldFailValidationForNullProductId() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var command = new CreateOrderItemCommand(orderId, null, 5);

        Set<ConstraintViolation<CreateOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("productId");
    }

    @Test
    void shouldFailValidationForNullQuantity() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var productId = TestUtils.createUuidFromNumber(2);
        var command = new CreateOrderItemCommand(orderId, productId, null);

        Set<ConstraintViolation<CreateOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("quantity");
    }

    @Test
    void shouldFailValidationForZeroQuantity() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var productId = TestUtils.createUuidFromNumber(2);
        var command = new CreateOrderItemCommand(orderId, productId, 0);

        Set<ConstraintViolation<CreateOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("quantity");
    }

    @Test
    void shouldFailValidationForNegativeQuantity() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var productId = TestUtils.createUuidFromNumber(2);
        var command = new CreateOrderItemCommand(orderId, productId, -1);

        Set<ConstraintViolation<CreateOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("quantity");
    }
}

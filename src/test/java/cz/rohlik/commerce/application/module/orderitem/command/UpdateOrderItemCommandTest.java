package cz.rohlik.commerce.application.module.orderitem.command;

import static org.assertj.core.api.Assertions.assertThat;

import cz.rohlik.commerce.TestUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateOrderItemCommandTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldPassValidationForValidCommand() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var orderItemId = TestUtils.createUuidFromNumber(2);
        var command = new UpdateOrderItemCommand(orderId, orderItemId, 10);

        Set<ConstraintViolation<UpdateOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationForNullOrderId() {
        var orderItemId = TestUtils.createUuidFromNumber(1);
        var command = new UpdateOrderItemCommand(null, orderItemId, 10);

        Set<ConstraintViolation<UpdateOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("orderId");
    }

    @Test
    void shouldFailValidationForNullOrderItemId() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var command = new UpdateOrderItemCommand(orderId, null, 10);

        Set<ConstraintViolation<UpdateOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString())
                .contains("orderItemId");
    }

    @Test
    void shouldFailValidationForNullQuantity() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var orderItemId = TestUtils.createUuidFromNumber(2);
        var command = new UpdateOrderItemCommand(orderId, orderItemId, null);

        Set<ConstraintViolation<UpdateOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("quantity");
    }

    @Test
    void shouldFailValidationForZeroQuantity() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var orderItemId = TestUtils.createUuidFromNumber(2);
        var command = new UpdateOrderItemCommand(orderId, orderItemId, 0);

        Set<ConstraintViolation<UpdateOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("quantity");
    }

    @Test
    void shouldFailValidationForNegativeQuantity() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var orderItemId = TestUtils.createUuidFromNumber(2);
        var command = new UpdateOrderItemCommand(orderId, orderItemId, -1);

        Set<ConstraintViolation<UpdateOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("quantity");
    }
}

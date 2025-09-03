package cz.rohlik.commerce.application.module.orderitem.command;

import static org.assertj.core.api.Assertions.assertThat;

import cz.rohlik.commerce.TestUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteOrderItemCommandTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldPassValidationForValidCommand() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var orderItemId = TestUtils.createUuidFromNumber(2);
        var command = new DeleteOrderItemCommand(orderId, orderItemId);

        Set<ConstraintViolation<DeleteOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationForNullOrderId() {
        var orderItemId = TestUtils.createUuidFromNumber(1);
        var command = new DeleteOrderItemCommand(null, orderItemId);

        Set<ConstraintViolation<DeleteOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("orderId");
    }

    @Test
    void shouldFailValidationForNullOrderItemId() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var command = new DeleteOrderItemCommand(orderId, null);

        Set<ConstraintViolation<DeleteOrderItemCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString())
                .contains("orderItemId");
    }
}

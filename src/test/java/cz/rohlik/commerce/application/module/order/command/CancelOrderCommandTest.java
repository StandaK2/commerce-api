package cz.rohlik.commerce.application.module.order.command;

import static org.assertj.core.api.Assertions.assertThat;

import cz.rohlik.commerce.TestUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CancelOrderCommandTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldPassValidationForValidCommand() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var command = new CancelOrderCommand(orderId);

        Set<ConstraintViolation<CancelOrderCommand>> violations = validator.validate(command);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationForNullOrderId() {
        var command = new CancelOrderCommand(null);

        Set<ConstraintViolation<CancelOrderCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("orderId");
    }
}

package cz.rohlik.commerce.application.module.order.command;

import static org.assertj.core.api.Assertions.assertThat;

import cz.rohlik.commerce.TestUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PayOrderCommandTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldPassValidationForValidCommand() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var command = new PayOrderCommand(orderId);

        Set<ConstraintViolation<PayOrderCommand>> violations = validator.validate(command);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationForNullOrderId() {
        var command = new PayOrderCommand(null);

        Set<ConstraintViolation<PayOrderCommand>> violations = validator.validate(command);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).contains("orderId");
    }
}

package cz.rohlik.commerce.domain.model.order;

import static org.junit.jupiter.api.Assertions.*;

import cz.rohlik.commerce.domain.model.order.constant.OrderStatus;
import cz.rohlik.commerce.domain.model.order.exception.InvalidOrderStateException;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void shouldCreateOrderWithCorrectInitialState() {
        var order = new Order();

        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
        assertFalse(order.isExpired(30));
    }

    @Test
    void shouldMarkAsPaidWhenCreated() {
        var order = new Order();

        order.markAsPaid();

        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenPayingNonCreatedOrder() {
        var order = new Order();
        order.cancel();

        assertThrows(InvalidOrderStateException.class, order::markAsPaid);
    }

    @Test
    void shouldCancelWhenCreated() {
        var order = new Order();

        order.cancel();

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenCancellingPaidOrder() {
        var order = new Order();
        order.markAsPaid();

        assertThrows(InvalidOrderStateException.class, order::cancel);
    }

    @Test
    void shouldThrowExceptionWhenCancellingExpiredOrder() {
        var order = new Order();
        order.markAsExpired();

        assertThrows(InvalidOrderStateException.class, order::cancel);
    }

    @Test
    void shouldMarkAsExpiredWhenCreated() {
        var order = new Order();

        order.markAsExpired();

        assertEquals(OrderStatus.EXPIRED, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenExpiringPaidOrder() {
        var order = new Order();
        order.markAsPaid();

        assertThrows(InvalidOrderStateException.class, order::markAsExpired);
    }

    @Test
    void shouldCheckExpirationCorrectly() {
        var order = new Order();

        assertFalse(order.isExpired(30));
        assertTrue(order.isExpired(0));
    }
}

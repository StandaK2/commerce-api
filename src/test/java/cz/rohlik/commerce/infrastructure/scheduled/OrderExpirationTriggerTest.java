package cz.rohlik.commerce.infrastructure.scheduled;

import static org.mockito.Mockito.verify;

import cz.rohlik.commerce.application.module.order.OrderExpirationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderExpirationTriggerTest {

    @Mock private OrderExpirationService orderExpirationService;

    @InjectMocks private OrderExpirationTrigger orderExpirationTrigger;

    @Test
    void shouldCallOrderExpirationService() {
        // When
        orderExpirationTrigger.trigger();

        // Then
        verify(orderExpirationService).expireCreatedOrders();
    }
}

package com.sofkau.auth.infrastructure.event.handlers;

import com.sofkau.auth.application.ports.input.auth.Logout;
import com.sofkau.auth.domain.events.customer.CustomerDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerEventHandler {
    private final Logout logout;

    @RabbitListener(queues = "${rabbitmq.queue.delete}")
    public void handleCustomerDeleted(CustomerDeletedEvent event) {
        long customerId = Long.parseLong(event.getResourceId());
        logout.logout(customerId);
    }
}

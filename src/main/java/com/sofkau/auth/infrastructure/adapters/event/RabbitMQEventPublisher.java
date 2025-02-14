package com.sofkau.auth.infrastructure.adapters.event;

import com.sofkau.auth.application.ports.output.EventPublisher;
import com.sofkau.auth.domain.events.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQEventPublisher implements EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.user-topic}")
    private String topic;

    @Override
    public void publish(Event event) {
        rabbitTemplate.convertAndSend(topic, event.route(), event);
    }
}

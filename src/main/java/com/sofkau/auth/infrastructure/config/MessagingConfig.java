package com.sofkau.auth.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.sofkau.auth.constants.EventRouteConstants.CUSTOMER_DELETED;

@Configuration
public class MessagingConfig {
    @Bean
    public TopicExchange exchange(@Value("${rabbitmq.exchange.user-topic}") String exchangeName) {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue deleteQueue(@Value("${rabbitmq.queue.delete}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue userQueue(@Value("${rabbitmq.queue.user}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding deleteBinding(TopicExchange exchange, Queue deleteQueue) {
        return BindingBuilder.bind(deleteQueue).to(exchange).with(CUSTOMER_DELETED);
    }

    @Bean
    public Binding userBinding(TopicExchange exchange, Queue userQueue) {
        return BindingBuilder.bind(userQueue).to(exchange).with("customer.#");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter converter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}

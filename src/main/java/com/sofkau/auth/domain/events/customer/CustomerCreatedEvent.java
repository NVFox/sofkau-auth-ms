package com.sofkau.auth.domain.events.customer;

import com.sofkau.auth.constants.EventRouteConstants;
import com.sofkau.auth.domain.events.Event;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.sofkau.auth.constants.EventResourceConstants.CUSTOMER_RESOURCE;

@SuperBuilder
@NoArgsConstructor
public class CustomerCreatedEvent extends Event {
    public static CustomerCreatedEvent successful(long customerId) {
        String message = "Se creó el usuario con id " + customerId;

        return CustomerCreatedEvent.builder()
                .resource(CUSTOMER_RESOURCE)
                .resourceId(String.valueOf(customerId))
                .message(message)
                .build();
    }

    public static CustomerCreatedEvent failed(String email) {
        String message = "No se pudo crear el usuario con email " + email;

        return CustomerCreatedEvent.builder()
                .resource(CUSTOMER_RESOURCE)
                .resourceId(null)
                .message(message)
                .state(false)
                .build();
    }

    @Override
    public String route() {
        return EventRouteConstants.CUSTOMER_CREATED;
    }
}

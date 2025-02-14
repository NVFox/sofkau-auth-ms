package com.sofkau.auth.domain.events.customer;

import com.sofkau.auth.constants.EventRouteConstants;
import com.sofkau.auth.domain.events.Event;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.sofkau.auth.constants.EventResourceConstants.CUSTOMER_RESOURCE;

@SuperBuilder
@NoArgsConstructor
public class CustomerAuthenticatedEvent extends Event {
    public static CustomerAuthenticatedEvent successful(long customerId, String token) {
        String message = "Se autenticó el usuario con id " + customerId + " y token " + token;

        return CustomerAuthenticatedEvent.builder()
                .resource(CUSTOMER_RESOURCE)
                .resourceId(String.valueOf(customerId))
                .message(message)
                .build();
    }

    public static CustomerAuthenticatedEvent failed(String email) {
        String message = "No se pudo autenticar el usuario con email " + email;

        return CustomerAuthenticatedEvent.builder()
                .resource(CUSTOMER_RESOURCE)
                .resourceId(String.valueOf(email))
                .message(message)
                .state(false)
                .build();
    }

    @Override
    public String route() {
        return EventRouteConstants.CUSTOMER_AUTHENTICATED;
    }
}

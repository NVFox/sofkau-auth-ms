package com.sofkau.auth.domain.events.customer;

import com.sofkau.auth.constants.EventRouteConstants;
import com.sofkau.auth.domain.events.Event;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.sofkau.auth.constants.EventResourceConstants.CUSTOMER_RESOURCE;

@SuperBuilder
@NoArgsConstructor
public class CustomerUpdatedEvent extends Event {
    public static CustomerUpdatedEvent successful(long customerId) {
        String message = "Se actualizó el usuario con id " + customerId;

        return CustomerUpdatedEvent.builder()
                .resource(CUSTOMER_RESOURCE)
                .resourceId(String.valueOf(customerId))
                .message(message)
                .build();
    }

    public static CustomerUpdatedEvent failed(long customerId) {
        String message = "No se pudo actualizar el usuario con id " + customerId;

        return CustomerUpdatedEvent.builder()
                .resource(CUSTOMER_RESOURCE)
                .resourceId(String.valueOf(customerId))
                .message(message)
                .state(false)
                .build();
    }

    @Override
    public String route() {
        return EventRouteConstants.CUSTOMER_UPDATED;
    }
}

package com.sofkau.auth.domain.events.customer;

import com.sofkau.auth.constants.EventRouteConstants;
import com.sofkau.auth.domain.events.Event;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.sofkau.auth.constants.EventResourceConstants.CUSTOMER_RESOURCE;

@SuperBuilder
@NoArgsConstructor
public class CustomerDeletedEvent extends Event {
    public static CustomerDeletedEvent successful(long customerId) {
        String message = "Se eliminó el usuario con id " + customerId;

        return CustomerDeletedEvent.builder()
                .resource(CUSTOMER_RESOURCE)
                .resourceId(String.valueOf(customerId))
                .message(message)
                .build();
    }

    public static CustomerDeletedEvent failed(long customerId) {
        String message = "No se pudo eliminar el usuario con id " + customerId;

        return CustomerDeletedEvent.builder()
                .resource(CUSTOMER_RESOURCE)
                .resourceId(String.valueOf(customerId))
                .message(message)
                .state(false)
                .build();
    }

    @Override
    public String route() {
        return EventRouteConstants.CUSTOMER_DELETED;
    }
}

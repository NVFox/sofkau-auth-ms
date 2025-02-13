package com.sofkau.auth.domain.events.customer;

import com.sofkau.auth.domain.events.Event;

import static com.sofkau.auth.constants.CustomerMessageConstants.*;

public class CustomerCreatedEvent extends Event {
    public CustomerCreatedEvent(long customerId) {
        super(CUSTOMER_RESOURCE, String.valueOf(customerId), message(customerId));
    }

    private static String message(long customerId) {
        return "Se realizó la creación del usuario con id " + customerId;
    }
}

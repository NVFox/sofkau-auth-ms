package com.sofkau.auth.domain.events.customer;

import com.sofkau.auth.domain.events.Event;

import static com.sofkau.auth.constants.CustomerMessageConstants.CUSTOMER_RESOURCE;

public class CustomerDeletedEvent extends Event {
    public CustomerDeletedEvent(long customerId) {
        super(CUSTOMER_RESOURCE, String.valueOf(customerId), message(customerId));
    }

    private static String message(long customerId) {
        return "Se realizó la eliminación del usuario con id " + customerId;
    }
}

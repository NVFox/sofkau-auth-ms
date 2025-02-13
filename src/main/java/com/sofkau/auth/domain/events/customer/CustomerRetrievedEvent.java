package com.sofkau.auth.domain.events.customer;

import com.sofkau.auth.domain.events.Event;

import static com.sofkau.auth.constants.CustomerMessageConstants.CUSTOMER_RESOURCE;

public class CustomerRetrievedEvent extends Event {
    public CustomerRetrievedEvent(long customerId) {
        super(CUSTOMER_RESOURCE, String.valueOf(customerId), message(customerId));
    }

    private static String message(long customerId) {
        return "Se consultó el usuario con id " + customerId;
    }
}

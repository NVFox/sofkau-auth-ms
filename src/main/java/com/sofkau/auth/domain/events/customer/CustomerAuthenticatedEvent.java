package com.sofkau.auth.domain.events.customer;

import com.sofkau.auth.domain.events.Event;

import static com.sofkau.auth.constants.CustomerMessageConstants.CUSTOMER_RESOURCE;

public class CustomerAuthenticatedEvent extends Event {
    public CustomerAuthenticatedEvent(long customerId, String token) {
        super(CUSTOMER_RESOURCE, String.valueOf(customerId), message(customerId, token));
    }

    private static String message(long customerId, String token) {
        return "Se realizó la autenticación exitosa del usuario con id " + customerId +
                ", con el token "  + token;
    }
}

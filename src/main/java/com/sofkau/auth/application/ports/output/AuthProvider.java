package com.sofkau.auth.application.ports.output;

import com.sofkau.auth.domain.entities.Customer;

public interface AuthProvider {
    void authorized(Customer customer, Runnable toMake);
}

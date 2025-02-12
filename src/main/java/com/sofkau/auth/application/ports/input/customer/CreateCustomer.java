package com.sofkau.auth.application.ports.input.customer;

import com.sofkau.auth.application.dtos.CreateCustomerRequest;
import com.sofkau.auth.application.dtos.GetCustomerResponse;

public interface CreateCustomer {
    GetCustomerResponse create(CreateCustomerRequest createCustomerRequest);
}

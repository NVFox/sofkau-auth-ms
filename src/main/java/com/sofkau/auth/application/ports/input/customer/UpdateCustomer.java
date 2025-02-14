package com.sofkau.auth.application.ports.input.customer;

import com.sofkau.auth.application.dtos.GetCustomerResponse;
import com.sofkau.auth.application.dtos.UpdateCustomerRequest;

public interface UpdateCustomer {
    GetCustomerResponse update(long id, UpdateCustomerRequest updateCustomerRequest);
}

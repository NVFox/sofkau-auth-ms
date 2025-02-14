package com.sofkau.auth.application.ports.input.customer;

import com.sofkau.auth.application.dtos.GetCustomerResponse;

public interface GetCustomer {
    GetCustomerResponse get(long id);
}

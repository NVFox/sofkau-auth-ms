package com.sofkau.auth.domain.services;

import com.sofkau.auth.domain.entities.Customer;

import java.util.function.UnaryOperator;

public interface CustomerService {
    Customer createCustomer(Customer customer);

    Customer getCustomer(long id);

    Customer updateCustomer(long id, UnaryOperator<Customer> update);

    void deleteCustomer(long id);
}

package com.sofkau.auth.application.usecases.customer;

import com.sofkau.auth.application.dtos.CreateCustomerRequest;
import com.sofkau.auth.application.dtos.GetCustomerResponse;
import com.sofkau.auth.application.mappers.CustomerMapper;
import com.sofkau.auth.application.ports.input.customer.CreateCustomer;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCustomerUseCase implements CreateCustomer {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public GetCustomerResponse create(CreateCustomerRequest createCustomerRequest) {
        Customer customer = customerMapper.toConsumer(createCustomerRequest);

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        return customerMapper.toCustomerResponse(customerService.createCustomer(customer));
    }
}

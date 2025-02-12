package com.sofkau.auth.application.usecases.customer;

import com.sofkau.auth.application.dtos.GetCustomerResponse;
import com.sofkau.auth.application.dtos.UpdateCustomerRequest;
import com.sofkau.auth.application.mappers.CustomerMapper;
import com.sofkau.auth.application.ports.input.customer.UpdateCustomer;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class UpdateCustomerUseCase implements UpdateCustomer {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public GetCustomerResponse update(long id, UpdateCustomerRequest updateCustomerRequest) {
        UnaryOperator<Customer> updateWithMapper = customer -> customerMapper
                .updateCustomerWith(customer, updateCustomerRequest);

        return customerMapper.toCustomerResponse(customerService.updateCustomer(id, updateWithMapper));
    }
}

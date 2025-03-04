package com.sofkau.auth.application.usecases.customer;

import com.sofkau.auth.application.dtos.GetCustomerResponse;
import com.sofkau.auth.application.dtos.UpdateCustomerRequest;
import com.sofkau.auth.application.mappers.CustomerMapper;
import com.sofkau.auth.application.ports.input.customer.UpdateCustomer;
import com.sofkau.auth.application.ports.output.AuthHandler;
import com.sofkau.auth.application.ports.output.EventPublisher;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.events.customer.CustomerUpdatedEvent;
import com.sofkau.auth.domain.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class UpdateCustomerUseCase implements UpdateCustomer {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;
    private final AuthHandler<Long> authHandler;

    public GetCustomerResponse update(long id, UpdateCustomerRequest updateCustomerRequest) {
        try {
            return authHandler.authorized(id, () -> {
                UnaryOperator<Customer> updateWithMapper = customer -> this
                        .updateCustomerWith(customer, updateCustomerRequest);

                Customer updated = customerService
                        .updateCustomer(id, updateWithMapper);

                eventPublisher
                        .publish(CustomerUpdatedEvent.successful(updated.getId()));

                return customerMapper.toCustomerResponse(updated);
            });
        } catch (Exception e) {
            eventPublisher
                    .publish(CustomerUpdatedEvent.failed(id));

            throw e;
        }
    }

    private Customer updateCustomerWith(Customer customer, UpdateCustomerRequest updateCustomerRequest) {
        Customer updatedCustomer = customerMapper
                .updateCustomerWith(customer, updateCustomerRequest);

        if (updateCustomerRequest.password() != null)
            updatedCustomer.setPassword(passwordEncoder.encode(updateCustomerRequest.password()));

        return updatedCustomer;
    }
}

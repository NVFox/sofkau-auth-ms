package com.sofkau.auth.application.usecases.customer;

import com.sofkau.auth.application.dtos.CreateCustomerRequest;
import com.sofkau.auth.application.dtos.GetCustomerResponse;
import com.sofkau.auth.application.mappers.CustomerMapper;
import com.sofkau.auth.application.ports.input.customer.CreateCustomer;
import com.sofkau.auth.application.ports.output.EventPublisher;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.events.customer.CustomerCreatedEvent;
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
    private final EventPublisher eventPublisher;

    @Override
    public GetCustomerResponse create(CreateCustomerRequest createCustomerRequest) {
        try {
            Customer customer = customerMapper.toConsumer(createCustomerRequest).toBuilder()
                    .password(passwordEncoder.encode(createCustomerRequest.password()))
                    .build();

            Customer created = customerService
                    .createCustomer(customer);

            eventPublisher
                    .publish(CustomerCreatedEvent.successful(created.getId()));

            return customerMapper.toCustomerResponse(created);
        } catch (Exception e) {
            eventPublisher
                    .publish(CustomerCreatedEvent.failed(createCustomerRequest.email()));

            throw e;
        }
    }
}

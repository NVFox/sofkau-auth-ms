package com.sofkau.auth.application.usecases.customer;

import com.sofkau.auth.application.dtos.GetCustomerResponse;
import com.sofkau.auth.application.mappers.CustomerMapper;
import com.sofkau.auth.application.ports.input.customer.GetCustomer;
import com.sofkau.auth.application.ports.output.EventPublisher;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.events.customer.CustomerRetrievedEvent;
import com.sofkau.auth.domain.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCustomerUseCase implements GetCustomer {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final EventPublisher eventPublisher;

    @Override
    public GetCustomerResponse get(long id) {
        Customer customer = customerService.getCustomer(id);

        eventPublisher
                .publish(new CustomerRetrievedEvent(id));

        return customerMapper.toCustomerResponse(customer);
    }
}

package com.sofkau.auth.application.usecases.customer;

import com.sofkau.auth.application.dtos.GetCustomerResponse;
import com.sofkau.auth.application.mappers.CustomerMapper;
import com.sofkau.auth.application.ports.input.customer.GetCustomer;
import com.sofkau.auth.application.ports.output.AuthHandler;
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
    private final AuthHandler<Long> authHandler;

    @Override
    public GetCustomerResponse get(long id) {
        try {
            return authHandler.authorized(id, () -> {
                Customer customer = customerService.getCustomer(id);

                eventPublisher
                        .publish(CustomerRetrievedEvent.successful(customer.getId()));

                return customerMapper.toCustomerResponse(customer);
            });
        } catch (Exception e) {
            eventPublisher
                    .publish(CustomerRetrievedEvent.failed(id));

            throw e;
        }
    }
}

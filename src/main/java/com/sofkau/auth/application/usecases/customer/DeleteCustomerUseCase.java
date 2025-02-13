package com.sofkau.auth.application.usecases.customer;

import com.sofkau.auth.application.ports.input.customer.DeleteCustomer;
import com.sofkau.auth.application.ports.output.EventPublisher;
import com.sofkau.auth.domain.events.customer.CustomerDeletedEvent;
import com.sofkau.auth.domain.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCustomerUseCase implements DeleteCustomer {
    private final CustomerService customerService;
    private final EventPublisher eventPublisher;

    @Override
    public void delete(long id) {
        customerService.deleteCustomer(id);

        eventPublisher
                .publish(new CustomerDeletedEvent(id));
    }
}

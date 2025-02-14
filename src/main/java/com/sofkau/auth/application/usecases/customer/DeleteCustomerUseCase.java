package com.sofkau.auth.application.usecases.customer;

import com.sofkau.auth.application.ports.input.customer.DeleteCustomer;
import com.sofkau.auth.application.ports.output.AuthHandler;
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
    private final AuthHandler<Long> authHandler;

    @Override
    public void delete(long id) {
        try {
            authHandler.authorized(id, () -> {
                customerService.deleteCustomer(id);

                eventPublisher
                        .publish(CustomerDeletedEvent.successful(id));
            });
        } catch (Exception e) {
            eventPublisher
                    .publish(CustomerDeletedEvent.failed(id));

            throw e;
        }
    }
}

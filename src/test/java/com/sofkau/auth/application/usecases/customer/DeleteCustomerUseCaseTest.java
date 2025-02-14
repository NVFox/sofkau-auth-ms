package com.sofkau.auth.application.usecases.customer;

import com.sofkau.auth.application.ports.output.AuthHandler;
import com.sofkau.auth.application.ports.output.EventPublisher;
import com.sofkau.auth.domain.events.customer.CustomerDeletedEvent;
import com.sofkau.auth.domain.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeleteCustomerUseCaseTest {
    @Mock
    private CustomerService customerService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private AuthHandler<Long> authHandler;

    @InjectMocks
    private DeleteCustomerUseCase deleteCustomerUseCase;

    @Test
    void deleteCustomer_WhenCustomerIsAuthorized_ShouldDeleteCustomer() {
        long id = 1L;

        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(1);
            runnable.run();
            return null;
        }).when(authHandler).authorized(eq(id), any(Runnable.class));

        deleteCustomerUseCase.delete(id);

        verify(customerService, times(1))
                .deleteCustomer(id);

        verify(eventPublisher, times(1))
                .publish(any(CustomerDeletedEvent.class));
    }

    @Test
    void deleteCustomer_WhenCustomerIsNotAuthorized_ShouldThrowException() {
        long id = 1L;

        doThrow(new RuntimeException())
                .when(authHandler).authorized(eq(id), any(Runnable.class));

        assertThrows(RuntimeException.class, () -> deleteCustomerUseCase.delete(id));

        verify(customerService, never())
                .deleteCustomer(id);

        verify(eventPublisher, times(1))
                .publish(any(CustomerDeletedEvent.class));
    }
}

package com.sofkau.auth.application.usecases.customer;

import com.sofkau.auth.application.dtos.GetCustomerResponse;
import com.sofkau.auth.application.mappers.CustomerMapper;
import com.sofkau.auth.application.ports.output.AuthHandler;
import com.sofkau.auth.application.ports.output.EventPublisher;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.events.customer.CustomerRetrievedEvent;
import com.sofkau.auth.domain.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetCustomerUseCaseTest {
    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private AuthHandler<Long> authHandler;

    @InjectMocks
    private GetCustomerUseCase getCustomerUseCase;

    @Test
    @SuppressWarnings("unchecked")
    void get_WhenCustomerIsAuthorized_ShouldReturnCustomer() {
        GetCustomerResponse response = new GetCustomerResponse(
                1L,
                "",
                "",
                "",
                ""
        );

        when(authHandler.authorized(anyLong(), any(Supplier.class)))
                .then(invocation -> {
                    Supplier<GetCustomerResponse> supplier = invocation.getArgument(1);
                    return supplier.get();
                });

        when(customerService.getCustomer(anyLong()))
                .thenReturn(Customer.builder().id(1L).build());

        when(customerMapper.toCustomerResponse(any()))
                .thenReturn(response);

        assertNotNull(getCustomerUseCase.get(1L));

        verify(eventPublisher, times(1))
                .publish(any(CustomerRetrievedEvent.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void get_WhenCustomerIsNotAuthorized_ShouldThrowException() {
        when(authHandler.authorized(anyLong(), any(Supplier.class)))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> getCustomerUseCase.get(1L));

        verify(eventPublisher, times(1))
                .publish(any(CustomerRetrievedEvent.class));
    }
}

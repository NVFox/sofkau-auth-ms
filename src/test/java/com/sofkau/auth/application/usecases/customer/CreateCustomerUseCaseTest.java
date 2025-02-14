package com.sofkau.auth.application.usecases.customer;

import com.sofkau.auth.application.dtos.CreateCustomerRequest;
import com.sofkau.auth.application.dtos.GetCustomerResponse;
import com.sofkau.auth.application.mappers.CustomerMapper;
import com.sofkau.auth.application.ports.output.EventPublisher;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.events.customer.CustomerCreatedEvent;
import com.sofkau.auth.domain.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseTest {
    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private CreateCustomerUseCase createCustomerUseCase;

    private final String testEmail = "test@mail.com";

    private final String testPassword = "p4ssw0rd";

    @Test
    void create_WhenCustomerIsCreated_ThenReturnCreatedCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .build();

        CreateCustomerRequest request = new CreateCustomerRequest(
                "",
                testEmail,
                "",
                testPassword
        );

        GetCustomerResponse response = new GetCustomerResponse(
                1L,
                "",
                testEmail,
                "",
                testPassword
        );

        when(customerMapper.toConsumer(any()))
                .thenReturn(customer);

        when(passwordEncoder.encode(any()))
                .thenReturn(testPassword);

        when(customerService.createCustomer(any()))
                .thenReturn(customer);

        when(customerMapper.toCustomerResponse(any()))
                .thenReturn(response);

        GetCustomerResponse created = createCustomerUseCase.create(request);

        assertNotNull(created);
        assertNotNull(created.password());
        assertEquals(testEmail, created.email());

        verify(eventPublisher, times(1))
                .publish(any(CustomerCreatedEvent.class));

    }

    @Test
    void create_WhenCustomerIsNotCreated_ThenThrowException() {
        CreateCustomerRequest request = new CreateCustomerRequest(
                "",
                testEmail,
                "",
                testPassword
        );

        when(customerMapper.toConsumer(any()))
                .thenReturn(Customer.builder()
                        .email(testEmail)
                        .build());

        when(passwordEncoder.encode(any()))
                .thenReturn(testPassword);

        when(customerService.createCustomer(any()))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> createCustomerUseCase
                .create(request));

        verify(eventPublisher, times(1))
                .publish(any(CustomerCreatedEvent.class));
    }
}

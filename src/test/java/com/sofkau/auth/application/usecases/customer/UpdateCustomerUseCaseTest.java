package com.sofkau.auth.application.usecases.customer;

import com.sofkau.auth.application.dtos.GetCustomerResponse;
import com.sofkau.auth.application.dtos.UpdateCustomerRequest;
import com.sofkau.auth.application.mappers.CustomerMapper;
import com.sofkau.auth.application.ports.output.AuthHandler;
import com.sofkau.auth.application.ports.output.EventPublisher;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerUseCaseTest {
    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private AuthHandler<Long> authHandler;

    @InjectMocks
    private UpdateCustomerUseCase updateCustomerUseCase;

    @Test
    @SuppressWarnings("unchecked")
    void update_WhenCustomerIsAuthorized_ThenReturnCustomerResponse() {
        String testName = "testname";

        Customer customer = new Customer();

        Customer updatedCustomer = Customer.builder()
                .id(1L)
                .name(testName)
                .build();

        UpdateCustomerRequest request = new UpdateCustomerRequest(
                testName,
                null,
                null,
                null
        );

        GetCustomerResponse response = new GetCustomerResponse(
                1L,
                testName,
                "",
                "",
                ""
        );

        when(authHandler.authorized(anyLong(), any(Supplier.class))).then(invocation -> {
            Supplier<GetCustomerResponse> supplier = invocation.getArgument(1);
            return supplier.get();
        });

        when(customerService.updateCustomer(anyLong(), any()))
                .then(invocation -> {
                    UnaryOperator<Customer> unaryOperator = invocation.getArgument(1);
                    return unaryOperator.apply(customer);
                });

        when(customerMapper.updateCustomerWith(any(), any()))
                .thenReturn(updatedCustomer);

        when(customerMapper.toCustomerResponse(any()))
                .thenReturn(response);

        GetCustomerResponse result = updateCustomerUseCase.update(1L, request);

        assertNotNull(result);
        assertEquals(testName, result.name());

        verify(passwordEncoder, never())
                .encode(anyString());

        verify(eventPublisher, times(1))
                .publish(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void update_WhenCustomerUpdatesPassword_ThenReturnCustomerResponse() {
        String testPassword = "testpassword";

        Customer customer = new Customer();

        Customer updatedCustomer = Customer.builder()
                .id(1L)
                .password(testPassword)
                .build();

        UpdateCustomerRequest request = new UpdateCustomerRequest(
                null,
                null,
                null,
                testPassword
        );

        GetCustomerResponse response = new GetCustomerResponse(
                1L,
                "",
                "",
                "",
                testPassword
        );

        when(authHandler.authorized(anyLong(), any(Supplier.class))).then(invocation -> {
            Supplier<GetCustomerResponse> supplier = invocation.getArgument(1);
            return supplier.get();
        });

        when(customerService.updateCustomer(anyLong(), any()))
                .then(invocation -> {
                    UnaryOperator<Customer> unaryOperator = invocation.getArgument(1);
                    return unaryOperator.apply(customer);
                });

        when(customerMapper.updateCustomerWith(any(), any()))
                .thenReturn(updatedCustomer);

        when(passwordEncoder.encode(anyString()))
                .thenReturn(testPassword);

        when(customerMapper.toCustomerResponse(any()))
                .thenReturn(response);

        GetCustomerResponse result = updateCustomerUseCase.update(1L, request);

        assertNotNull(result);
        assertEquals(testPassword, result.password());

        verify(passwordEncoder, times(1))
                .encode(anyString());

        verify(eventPublisher, times(1))
                .publish(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void update_WhenCustomerIsNotAuthorized_ThenThrowException() {
        UpdateCustomerRequest request = new UpdateCustomerRequest(
                "testname",
                null,
                null,
                null
        );

        when(authHandler.authorized(anyLong(), any(Supplier.class)))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> updateCustomerUseCase.update(1L, request));

        verify(eventPublisher, times(1))
                .publish(any());
    }
}

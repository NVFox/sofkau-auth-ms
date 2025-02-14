package com.sofkau.auth.application.services;

import com.sofkau.auth.application.exceptions.AlreadyExistsException;
import com.sofkau.auth.application.exceptions.NotFoundException;
import com.sofkau.auth.application.repositories.CustomerRepository;
import com.sofkau.auth.domain.entities.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.UnaryOperator;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private final String testEmail = "test@mail.com";

    @Test
    void createCustomer_WhenCustomerDoesNotExist_ShouldReturnCustomer() {
        when(customerRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(customerRepository.save(any()))
                .thenReturn(new Customer());

        Customer customer = customerService.createCustomer(Customer.builder()
                .email(testEmail)
                .build());

        assertNotNull(customer);

        verify(customerRepository, times(1))
                .save(any(Customer.class));
    }

    @Test
    void createCustomer_WhenCustomerExistsByEmail_ShouldThrowException() {
        Customer customer = Customer.builder()
                .email(testEmail)
                .build();

        when(customerRepository.existsByEmail(anyString()))
                .thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> customerService
                .createCustomer(customer));

        verify(customerRepository, never())
                .save(any(Customer.class));
    }

    @Test
    void getCustomer_WhenCustomerExists_ShouldReturnCustomer() {
        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Customer()));

        Customer customer = assertDoesNotThrow(() -> customerService
                .getCustomer(1L));

        assertNotNull(customer);
    }

    @Test
    void getCustomer_WhenCustomerDoesNotExist_ShouldThrowException() {
        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService
                .getCustomer(1L));
    }

    @Test
    void getCustomerByEmail_WhenCustomerExists_ShouldReturnCustomer() {
        when(customerRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(new Customer()));

        Customer customer = assertDoesNotThrow(() -> customerService
                .getCustomerByEmail(testEmail));

        assertNotNull(customer);
    }

    @Test
    void getCustomerByEmail_WhenCustomerDoesNotExist_ShouldThrowException() {
        when(customerRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService
                .getCustomerByEmail(testEmail));
    }

    @Test
    void updateCustomer_WhenCustomerExists_ShouldReturnCustomer() {
        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Customer()));

        when(customerRepository.save(any()))
                .thenReturn(new Customer());

        Customer customer = assertDoesNotThrow(() -> customerService
                .updateCustomer(1L, UnaryOperator.identity()));

        assertNotNull(customer);

        verify(customerRepository, times(1))
                .save(any(Customer.class));
    }

    @Test
    void updateCustomer_WhenCustomerDoesNotExist_ShouldThrowException() {
        when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        UnaryOperator<Customer> update = UnaryOperator.identity();

        assertThrows(NotFoundException.class, () -> customerService
                .updateCustomer(1L, update));

        verify(customerRepository, never())
                .save(any(Customer.class));
    }

    @Test
    void deleteCustomer_WhenCustomerExists_ShouldDeleteCustomer() {
        when(customerRepository.existsById(anyLong()))
                .thenReturn(true);

        doNothing().when(customerRepository)
                .deleteById(anyLong());

        assertDoesNotThrow(() -> customerService
                .deleteCustomer(1L));

        verify(customerRepository, times(1))
                .deleteById(anyLong());
    }

    @Test
    void deleteCustomer_WhenCustomerDoesNotExist_ShouldThrowException() {
        when(customerRepository.existsById(anyLong()))
                .thenReturn(false);

        assertThrows(NotFoundException.class, () -> customerService
                .deleteCustomer(1L));

        verify(customerRepository, never())
                .deleteById(anyLong());
    }
}

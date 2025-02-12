package com.sofkau.auth.application.services;

import com.sofkau.auth.application.exceptions.NotFoundException;
import com.sofkau.auth.application.repositories.CustomerRepository;
import com.sofkau.auth.domain.entities.Customer;
import com.sofkau.auth.domain.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.UnaryOperator;

import static com.sofkau.auth.constants.CustomerMessageConstants.*;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail()))
            throw new NotFoundException(CUSTOMER_ALREADY_EXISTS);

        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND));
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND));
    }

    @Override
    public Customer updateCustomer(long id, UnaryOperator<Customer> update) {
        return customerRepository.findById(id)
                .map(update.andThen(customerRepository::save))
                .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND));
    }

    @Override
    public void deleteCustomer(long id) {
        if (!customerRepository.existsById(id))
            throw new NotFoundException(CUSTOMER_NOT_FOUND);

        customerRepository.deleteById(id);
    }
}

package com.sofkau.auth.infrastructure.http.controllers;

import com.sofkau.auth.application.dtos.CreateCustomerRequest;
import com.sofkau.auth.application.dtos.GetCustomerResponse;
import com.sofkau.auth.application.dtos.UpdateCustomerRequest;
import com.sofkau.auth.application.ports.input.customer.CreateCustomer;
import com.sofkau.auth.application.ports.input.customer.DeleteCustomer;
import com.sofkau.auth.application.ports.input.customer.GetCustomer;
import com.sofkau.auth.application.ports.input.customer.UpdateCustomer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final GetCustomer getCustomer;
    private final CreateCustomer createCustomer;
    private final UpdateCustomer updateCustomer;
    private final DeleteCustomer deleteCustomer;

    @GetMapping("/{id}")
    public GetCustomerResponse getCustomer(@PathVariable long id) {
        return getCustomer.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GetCustomerResponse createCustomer(@RequestBody @Valid CreateCustomerRequest createCustomerRequest) {
        return createCustomer.create(createCustomerRequest);
    }

    @PutMapping("/{id}")
    public GetCustomerResponse updateCustomer(@PathVariable long id,
                                              @RequestBody @Valid UpdateCustomerRequest updateCustomerRequest) {
        return updateCustomer.update(id, updateCustomerRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable long id) {
        deleteCustomer.delete(id);
    }
}

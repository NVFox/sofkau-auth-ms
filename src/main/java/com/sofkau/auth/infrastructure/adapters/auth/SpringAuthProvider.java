package com.sofkau.auth.infrastructure.adapters.auth;

import com.sofkau.auth.application.ports.output.AuthProvider;
import com.sofkau.auth.domain.entities.Customer;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.sofkau.auth.constants.CustomerMessageConstants.CUSTOMER_NOT_AUTHORIZED;

@Service
public class SpringAuthProvider implements AuthProvider {
    @Override
    public void authorized(Customer customer, Runnable toMake) {
        if (!isCustomerAuthorized(customer))
            throw new AccessDeniedException(CUSTOMER_NOT_AUTHORIZED);

        toMake.run();
    }

    private boolean isCustomerAuthorized(Customer customer) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(auth.getPrincipal(), customer.getId());
    }
}

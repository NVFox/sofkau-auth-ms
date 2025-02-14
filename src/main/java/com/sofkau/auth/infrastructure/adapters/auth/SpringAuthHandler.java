package com.sofkau.auth.infrastructure.adapters.auth;

import com.sofkau.auth.application.ports.output.AuthHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Supplier;

import static com.sofkau.auth.constants.CustomerMessageConstants.CUSTOMER_NOT_AUTHORIZED;

@Service
public class SpringAuthHandler implements AuthHandler<Long> {
    @Override
    public void authorized(Long customerId, Runnable toMake) {
        if (!isCustomerAuthorized(customerId))
            throw new AccessDeniedException(CUSTOMER_NOT_AUTHORIZED);

        toMake.run();
    }

    @Override
    public <T> T authorized(Long authKey, Supplier<T> toMake) {
        if (!isCustomerAuthorized(authKey))
            throw new AccessDeniedException(CUSTOMER_NOT_AUTHORIZED);

        return toMake.get();
    }

    private boolean isCustomerAuthorized(long customerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(auth.getPrincipal(), customerId);
    }
}

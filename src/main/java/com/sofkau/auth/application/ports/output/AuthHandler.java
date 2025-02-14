package com.sofkau.auth.application.ports.output;

import java.util.function.Supplier;

public interface AuthHandler<K> {
    void authorized(K authKey, Runnable toMake);
    <T> T authorized(K authKey, Supplier<T> toMake);
}

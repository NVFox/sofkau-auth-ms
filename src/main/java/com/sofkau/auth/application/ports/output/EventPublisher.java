package com.sofkau.auth.application.ports.output;

import com.sofkau.auth.domain.events.Event;

public interface EventPublisher {
    void publish(Event event);
}

package com.sofkau.auth.domain.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class Event {
    @JsonProperty("recurso")
    protected String resource;

    @JsonProperty("idEntidad")
    protected String resourceId;

    @JsonProperty("mensaje")
    protected String message;

    @JsonProperty("estado")
    @Builder.Default
    protected boolean state = true;

    @JsonProperty("fecha")
    @Builder.Default
    protected LocalDateTime date = LocalDateTime.now();

    public abstract String route();
}

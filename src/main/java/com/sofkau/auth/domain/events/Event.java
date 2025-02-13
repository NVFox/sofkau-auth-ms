package com.sofkau.auth.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String recurso;
    private String idEntidad;
    private String mensaje;

    @Builder.Default
    public LocalDateTime fecha = LocalDateTime.now();

    public Event(String recurso, String idEntidad, String mensaje) {
        this.recurso = recurso;
        this.idEntidad = idEntidad;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }
}

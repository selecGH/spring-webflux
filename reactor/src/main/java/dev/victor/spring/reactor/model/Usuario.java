package dev.victor.spring.reactor.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Usuario {

    private String nombre;
    private String apellido;

}

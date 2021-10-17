package dev.victor.spring.reactor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioComentarios {

    private Usuario usuario;
    private Comentarios comentarios;

}

package dev.victor.spring.reactor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Comentarios {

    private List<String> comentarios;

    public Comentarios() {
        comentarios = new ArrayList<>();
    }

    public void addComentario(String comentario) {
        comentarios.add(comentario);
    }

}

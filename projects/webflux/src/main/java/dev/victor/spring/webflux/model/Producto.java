package dev.victor.spring.webflux.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "productos")
@Data
@NoArgsConstructor
public class Producto {

    @Id
    private String id;
    private String nombre;
    private Double precio;
    @CreatedDate
    private Instant createdAt;

    public Producto(String nombre, Double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

}

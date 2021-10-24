package dev.victor.spring.webflux.repository;

import dev.victor.spring.webflux.model.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductoRepository extends ReactiveMongoRepository<Producto, String> {

}

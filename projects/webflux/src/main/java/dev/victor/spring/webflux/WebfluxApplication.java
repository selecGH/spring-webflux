package dev.victor.spring.webflux;

import dev.victor.spring.webflux.model.Producto;
import dev.victor.spring.webflux.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

@SpringBootApplication
@EnableReactiveMongoAuditing
@Slf4j
public class WebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(WebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		reactiveMongoTemplate.dropCollection("productos")
			.subscribe();
		Flux.just(
				new Producto("Producto 1", 123.5),
				new Producto("Producto 2", 1253.5),
				new Producto("Producto 3", 1235.5),
				new Producto("Producto 4", 1215d),
				new Producto("Producto 5", 123573.4),
				new Producto("Producto 6", 1.693456),
				new Producto("Producto 7", 98443.3),
				new Producto("Producto 8", 683458.34)
				)
				.flatMap(producto -> productoRepository.save(producto))
				.subscribe(producto -> log.info(producto.getId()));
	}
}

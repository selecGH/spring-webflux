package dev.victor.spring.reactor;

import dev.victor.spring.reactor.model.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.List;

@SpringBootApplication
@Slf4j
public class ReactorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<Usuario> nombres = Flux
				.just("Victor Apellido1", "Pedro Apellido2", "Juan Apellido3", "Diego Apellido4", "Bárbara Apellido5", "Bruno Apellido6")
				.doOnNext(element -> {
					if (element.isEmpty()) {
						throw new RuntimeException("Elemento vacío");
					}
					System.out.println(element);
				})
				.filter(elemento -> elemento.startsWith("B"))
				.map(elemento -> Usuario.builder().nombre(elemento.split(" ")[0]).apellido(elemento.split(" ")[1]).build());
		nombres.subscribe(e -> log.info(e.toString()),
				error -> log.error(error.getMessage()),
				() -> log.info("Ha finalizado la ejecución del observable con éxito"));
	}

}

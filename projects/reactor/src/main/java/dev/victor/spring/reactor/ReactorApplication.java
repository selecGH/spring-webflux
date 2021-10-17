package dev.victor.spring.reactor;

import dev.victor.spring.reactor.model.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootApplication
@Slf4j
public class ReactorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		convertirString();
	}

	public void ejemploIterable() {
		List<String> usuarios = List.of("Victor Apellido1", "Pedro Apellido2", "Juan Apellido3",
				"Diego Apellido4", "Bárbara Apellido5", "Bruno Apellido6");

		Flux<Usuario> nombres = Flux.fromIterable(usuarios)
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

	public void ejemploFlatMap() {
		List<String> usuarios = List.of("Victor Apellido1", "Pedro Apellido2", "Juan Apellido3",
				"Diego Apellido4", "Bárbara Apellido5", "Bruno Apellido6");

		Flux.fromIterable(usuarios)
				.map(elemento -> Usuario.builder().nombre(elemento.split(" ")[0]).apellido(elemento.split(" ")[1]).build())
				.flatMap(elemento ->
						{
							if (elemento.getNombre().startsWith("B")) {
								return 	Mono.just(elemento);
							} else {
								return Mono.empty();
							}
						})
				.subscribe(u -> log.info(u.toString()));
	}

	public void convertirString() {

		List<Usuario> usuarios = List.of(
				new Usuario("Víctor", "Apellido1"),
				new Usuario("Pedro", "Apellido2"),
				new Usuario("Juan", "Apellido3"),
				new Usuario("Diego", "Apellido4"),
				new Usuario("Bárbara", "Apellido5"),
				new Usuario("Bruno", "Apellido6"));

		Flux.fromIterable(usuarios)
				.map(elemento -> elemento.getNombre()
						.concat(" ")
						.concat(elemento.getApellido()))
				.flatMap(elemento ->
				{
					if (elemento.startsWith("B")) {
						return 	Mono.just(elemento);
					} else {
						return Mono.empty();
					}
				})
				.subscribe(log::info);
	}

}

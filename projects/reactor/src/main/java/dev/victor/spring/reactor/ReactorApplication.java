package dev.victor.spring.reactor;

import dev.victor.spring.reactor.model.Comentarios;
import dev.victor.spring.reactor.model.Usuario;
import dev.victor.spring.reactor.model.UsuarioComentarios;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
public class ReactorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ejemploIntervalInfinito();
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

	public void ejemploCollectList() {

		List<Usuario> usuarios = List.of(
				new Usuario("Víctor", "Apellido1"),
				new Usuario("Pedro", "Apellido2"),
				new Usuario("Juan", "Apellido3"),
				new Usuario("Diego", "Apellido4"),
				new Usuario("Bárbara", "Apellido5"),
				new Usuario("Bruno", "Apellido6"));

		Flux.fromIterable(usuarios)
				.collectList()
				.subscribe(lista -> log.info(lista.toString()));
	}

	public void ejemploUsuarioComentariosFlatMap() {
		Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("Jhon", "Doe"));
		Mono<Comentarios> comentariosMono = Mono.fromCallable(() -> new Comentarios(List.of("Comentario", "Comentario2")));

		usuarioMono
				.flatMap(u -> comentariosMono.map(c -> new UsuarioComentarios(u, c)))
				.subscribe(uc -> log.info(uc.toString()));
	}

	public void ejemploUsuarioComentariosZipWith() {
		Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("Jhon", "Doe"));
		Mono<Comentarios> comentariosMono = Mono.fromCallable(() -> new Comentarios(List.of("Comentario", "Comentario2")));

		usuarioMono
				// .zipWith(comentariosMono, (u, c) -> new UsuarioComentarios(u, c)) // Alternative
				.zipWith(comentariosMono, UsuarioComentarios::new)
				.subscribe(uc -> log.info(uc.toString()));
	}

	public void ejemploUsuarioComentariosZipWithForma2() {
		Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("Jhon", "Doe"));
		Mono<Comentarios> comentariosMono = Mono.fromCallable(() -> new Comentarios(List.of("Comentario", "Comentario2")));

		usuarioMono
				.zipWith(comentariosMono)
				.map(tuple -> {
					Usuario u = tuple.getT1();
					Comentarios c = tuple.getT2();
					return new UsuarioComentarios(u, c);
				})
				.subscribe(uc -> log.info(uc.toString()));
	}

	public void ejemploRangeZipWith() {
		Flux.just(1,2,3,4)
				.map(i -> i*2)
				.zipWith(Flux.range(0, 4), (uno, dos) -> String.format("Primer Flux: %d, Segundo Flux: %d", uno, dos))
				.subscribe(log::info);
	}

	public void ejemploInterval() {
		Flux<Integer> rangos = Flux.range(1, 12);
		Flux<Long> delay = Flux.interval(Duration.ofSeconds(1));

		rangos.zipWith(delay, (ra, re) -> ra)
				.doOnNext(i -> log.info(i.toString()))
				.blockLast();
	}

	public void ejemploIntervalDelayElements() throws InterruptedException {
		Flux<Integer> rangos = Flux.range(1, 12);

		rangos
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(i -> log.info(i.toString()))
				.subscribe();

		Thread.sleep(13000);
	}

	public void ejemploIntervalInfinito() throws InterruptedException {

		CountDownLatch latch = new CountDownLatch(1);

		Flux.interval(Duration.ofSeconds(1))
				.doOnTerminate(latch::countDown)
				.flatMap(i -> {
					if (i >= 5) {
						return Flux.error(new InterruptedException("Solo hasta 5"));
					}
					return Flux.just(i);
				})
				.map(i -> "Hola " + i)
				.retry(2)
				.subscribe(s -> log.info(s), e -> log.error(e.getMessage()));

		latch.await();
	}

}

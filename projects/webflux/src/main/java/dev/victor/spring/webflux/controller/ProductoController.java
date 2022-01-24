package dev.victor.spring.webflux.controller;

import dev.victor.spring.webflux.model.Producto;
import dev.victor.spring.webflux.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductoController {

    private final ProductoRepository productoRepository;

    @GetMapping
    public String getAllProducts(Model model) {
        Flux<Producto> productos = productoRepository.findAll().map(product -> {
            product.setNombre(product.getNombre().toUpperCase());
            return product;
        });
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Lista de productos");
        LocalDateTime.parse("StringFecha", DateTimeFormatter.ofPattern("DD/MM/YYYY"));
        
        return "listar";
    }

}

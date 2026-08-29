package com.lab.apis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab.apis.model.Producto;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

	private final List<Producto> productos = new ArrayList<>();
	private long siguienteId = 6;

	public ProductoController() {
		productos.add(new Producto(1L, "Laptop", 6500.00, "Tecnología"));
		productos.add(new Producto(2L, "Mouse", 150.00, "Tecnología"));
		productos.add(new Producto(3L, "Cuaderno", 25.50, "Papelería"));
		productos.add(new Producto(4L, "Mochila", 275.00, "Accesorios"));
		productos.add(new Producto(5L, "Botella", 80.00, "Hogar"));
	}

	@GetMapping
	public List<Producto> obtenerTodos() {
		return productos;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
		return buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
		producto.setId(siguienteId++);
		productos.add(producto);
		return ResponseEntity.status(HttpStatus.CREATED).body(producto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Producto> actualizarCompleto(@PathVariable Long id,
			@RequestBody Producto datosProducto) {
		Optional<Producto> productoEncontrado = buscarPorId(id);

		if (productoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Producto producto = productoEncontrado.get();
		producto.setId(id);
		producto.setNombre(datosProducto.getNombre());
		producto.setPrecio(datosProducto.getPrecio());
		producto.setCategoria(datosProducto.getCategoria());

		return ResponseEntity.ok(producto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Producto> actualizarParcial(@PathVariable Long id,
			@RequestBody Producto datosProducto) {
		Optional<Producto> productoEncontrado = buscarPorId(id);

		if (productoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Producto producto = productoEncontrado.get();

		if (datosProducto.getNombre() != null) {
			producto.setNombre(datosProducto.getNombre());
		}
		if (datosProducto.getPrecio() != null) {
			producto.setPrecio(datosProducto.getPrecio());
		}
		if (datosProducto.getCategoria() != null) {
			producto.setCategoria(datosProducto.getCategoria());
		}

		return ResponseEntity.ok(producto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Optional<Producto> productoEncontrado = buscarPorId(id);

		if (productoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		productos.remove(productoEncontrado.get());
		return ResponseEntity.noContent().build();
	}

	private Optional<Producto> buscarPorId(Long id) {
		return productos.stream()
				.filter(producto -> producto.getId().equals(id))
				.findFirst();
	}
}

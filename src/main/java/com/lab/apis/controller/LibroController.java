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

import com.lab.apis.model.Libro;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

	private final List<Libro> libros = new ArrayList<>();
	private long siguienteId = 6;

	public LibroController() {
		libros.add(new Libro(1L, "Cien anos de soledad", "Gabriel Garcia Marquez", "Realismo magico", 125.00));
		libros.add(new Libro(2L, "Don Quijote de la Mancha", "Miguel de Cervantes", "Novela", 150.00));
		libros.add(new Libro(3L, "El principito", "Antoine de Saint-Exupery", "Fabula", 75.00));
		libros.add(new Libro(4L, "1984", "George Orwell", "Ciencia ficcion", 95.00));
		libros.add(new Libro(5L, "Orgullo y prejuicio", "Jane Austen", "Romance", 110.00));
	}

	@GetMapping
	public List<Libro> obtenerTodos() {
		return libros;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
		return buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Libro> crear(@RequestBody Libro libro) {
		libro.setId(siguienteId++);
		libros.add(libro);
		return ResponseEntity.status(HttpStatus.CREATED).body(libro);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Libro> actualizarCompleto(@PathVariable Long id,
			@RequestBody Libro datosLibro) {
		Optional<Libro> libroEncontrado = buscarPorId(id);

		if (libroEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Libro libro = libroEncontrado.get();
		libro.setId(id);
		libro.setTitulo(datosLibro.getTitulo());
		libro.setAutor(datosLibro.getAutor());
		libro.setGenero(datosLibro.getGenero());
		libro.setPrecio(datosLibro.getPrecio());

		return ResponseEntity.ok(libro);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Libro> actualizarParcial(@PathVariable Long id,
			@RequestBody Libro datosLibro) {
		Optional<Libro> libroEncontrado = buscarPorId(id);

		if (libroEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Libro libro = libroEncontrado.get();

		if (datosLibro.getTitulo() != null) {
			libro.setTitulo(datosLibro.getTitulo());
		}
		if (datosLibro.getAutor() != null) {
			libro.setAutor(datosLibro.getAutor());
		}
		if (datosLibro.getGenero() != null) {
			libro.setGenero(datosLibro.getGenero());
		}
		if (datosLibro.getPrecio() != null) {
			libro.setPrecio(datosLibro.getPrecio());
		}

		return ResponseEntity.ok(libro);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Optional<Libro> libroEncontrado = buscarPorId(id);

		if (libroEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		libros.remove(libroEncontrado.get());
		return ResponseEntity.noContent().build();
	}

	private Optional<Libro> buscarPorId(Long id) {
		return libros.stream()
				.filter(libro -> libro.getId().equals(id))
				.findFirst();
	}
}

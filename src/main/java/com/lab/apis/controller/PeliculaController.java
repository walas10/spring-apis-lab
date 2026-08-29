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

import com.lab.apis.model.Pelicula;

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {

	private final List<Pelicula> peliculas = new ArrayList<>();
	private long siguienteId = 6;

	public PeliculaController() {
		peliculas.add(new Pelicula(1L, "El padrino", "Francis Ford Coppola", "Drama", 1972));
		peliculas.add(new Pelicula(2L, "Interestelar", "Christopher Nolan", "Ciencia ficcion", 2014));
		peliculas.add(new Pelicula(3L, "Parasitos", "Bong Joon-ho", "Suspenso", 2019));
		peliculas.add(new Pelicula(4L, "Gladiador", "Ridley Scott", "Accion", 2000));
		peliculas.add(new Pelicula(5L, "Coco", "Lee Unkrich", "Animacion", 2017));
	}

	@GetMapping
	public List<Pelicula> obtenerTodas() {
		return peliculas;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pelicula> obtenerPorId(@PathVariable Long id) {
		return buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Pelicula> crear(@RequestBody Pelicula pelicula) {
		pelicula.setId(siguienteId++);
		peliculas.add(pelicula);
		return ResponseEntity.status(HttpStatus.CREATED).body(pelicula);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Pelicula> actualizarCompleta(@PathVariable Long id,
			@RequestBody Pelicula datosPelicula) {
		Optional<Pelicula> peliculaEncontrada = buscarPorId(id);

		if (peliculaEncontrada.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pelicula pelicula = peliculaEncontrada.get();
		pelicula.setId(id);
		pelicula.setTitulo(datosPelicula.getTitulo());
		pelicula.setDirector(datosPelicula.getDirector());
		pelicula.setGenero(datosPelicula.getGenero());
		pelicula.setAnio(datosPelicula.getAnio());

		return ResponseEntity.ok(pelicula);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Pelicula> actualizarParcial(@PathVariable Long id,
			@RequestBody Pelicula datosPelicula) {
		Optional<Pelicula> peliculaEncontrada = buscarPorId(id);

		if (peliculaEncontrada.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pelicula pelicula = peliculaEncontrada.get();

		if (datosPelicula.getTitulo() != null) {
			pelicula.setTitulo(datosPelicula.getTitulo());
		}
		if (datosPelicula.getDirector() != null) {
			pelicula.setDirector(datosPelicula.getDirector());
		}
		if (datosPelicula.getGenero() != null) {
			pelicula.setGenero(datosPelicula.getGenero());
		}
		if (datosPelicula.getAnio() != null) {
			pelicula.setAnio(datosPelicula.getAnio());
		}

		return ResponseEntity.ok(pelicula);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Optional<Pelicula> peliculaEncontrada = buscarPorId(id);

		if (peliculaEncontrada.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		peliculas.remove(peliculaEncontrada.get());
		return ResponseEntity.noContent().build();
	}

	private Optional<Pelicula> buscarPorId(Long id) {
		return peliculas.stream()
				.filter(pelicula -> pelicula.getId().equals(id))
				.findFirst();
	}
}

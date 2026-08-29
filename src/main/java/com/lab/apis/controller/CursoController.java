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

import com.lab.apis.model.Curso;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

	private final List<Curso> cursos = new ArrayList<>();
	private long siguienteId = 6;

	public CursoController() {
		cursos.add(new Curso(1L, "Programacion I", "Fundamentos de programacion", 4, "Presencial"));
		cursos.add(new Curso(2L, "Bases de Datos", "Diseno y gestion de bases de datos", 4, "Presencial"));
		cursos.add(new Curso(3L, "Desarrollo Web", "Creacion de aplicaciones web", 3, "Virtual"));
		cursos.add(new Curso(4L, "Redes de Computadoras", "Fundamentos de comunicacion y redes", 3, "Hibrida"));
		cursos.add(new Curso(5L, "Ingenieria de Software", "Procesos para desarrollar software", 4, "Presencial"));
	}

	@GetMapping
	public List<Curso> obtenerTodos() {
		return cursos;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Curso> obtenerPorId(@PathVariable Long id) {
		return buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Curso> crear(@RequestBody Curso curso) {
		curso.setId(siguienteId++);
		cursos.add(curso);
		return ResponseEntity.status(HttpStatus.CREATED).body(curso);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Curso> actualizarCompleto(@PathVariable Long id,
			@RequestBody Curso datosCurso) {
		Optional<Curso> cursoEncontrado = buscarPorId(id);

		if (cursoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Curso curso = cursoEncontrado.get();
		curso.setId(id);
		curso.setNombre(datosCurso.getNombre());
		curso.setDescripcion(datosCurso.getDescripcion());
		curso.setCreditos(datosCurso.getCreditos());
		curso.setModalidad(datosCurso.getModalidad());

		return ResponseEntity.ok(curso);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Curso> actualizarParcial(@PathVariable Long id,
			@RequestBody Curso datosCurso) {
		Optional<Curso> cursoEncontrado = buscarPorId(id);

		if (cursoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Curso curso = cursoEncontrado.get();

		if (datosCurso.getNombre() != null) {
			curso.setNombre(datosCurso.getNombre());
		}
		if (datosCurso.getDescripcion() != null) {
			curso.setDescripcion(datosCurso.getDescripcion());
		}
		if (datosCurso.getCreditos() != null) {
			curso.setCreditos(datosCurso.getCreditos());
		}
		if (datosCurso.getModalidad() != null) {
			curso.setModalidad(datosCurso.getModalidad());
		}

		return ResponseEntity.ok(curso);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Optional<Curso> cursoEncontrado = buscarPorId(id);

		if (cursoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		cursos.remove(cursoEncontrado.get());
		return ResponseEntity.noContent().build();
	}

	private Optional<Curso> buscarPorId(Long id) {
		return cursos.stream()
				.filter(curso -> curso.getId().equals(id))
				.findFirst();
	}
}

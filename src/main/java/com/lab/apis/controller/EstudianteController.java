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

import com.lab.apis.model.Estudiante;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

	private final List<Estudiante> estudiantes = new ArrayList<>();
	private long siguienteId = 6;

	public EstudianteController() {
		estudiantes.add(new Estudiante(1L, "Ana", "Lopez", "Ingenieria en Sistemas", 20));
		estudiantes.add(new Estudiante(2L, "Carlos", "Ramirez", "Administracion", 22));
		estudiantes.add(new Estudiante(3L, "Maria", "Garcia", "Derecho", 21));
		estudiantes.add(new Estudiante(4L, "Luis", "Hernandez", "Arquitectura", 23));
		estudiantes.add(new Estudiante(5L, "Sofia", "Martinez", "Psicologia", 19));
	}

	@GetMapping
	public List<Estudiante> obtenerTodos() {
		return estudiantes;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Estudiante> obtenerPorId(@PathVariable Long id) {
		return buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Estudiante> crear(@RequestBody Estudiante estudiante) {
		estudiante.setId(siguienteId++);
		estudiantes.add(estudiante);
		return ResponseEntity.status(HttpStatus.CREATED).body(estudiante);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Estudiante> actualizarCompleto(@PathVariable Long id,
			@RequestBody Estudiante datosEstudiante) {
		Optional<Estudiante> estudianteEncontrado = buscarPorId(id);

		if (estudianteEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Estudiante estudiante = estudianteEncontrado.get();
		estudiante.setId(id);
		estudiante.setNombre(datosEstudiante.getNombre());
		estudiante.setApellido(datosEstudiante.getApellido());
		estudiante.setCarrera(datosEstudiante.getCarrera());
		estudiante.setEdad(datosEstudiante.getEdad());

		return ResponseEntity.ok(estudiante);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Estudiante> actualizarParcial(@PathVariable Long id,
			@RequestBody Estudiante datosEstudiante) {
		Optional<Estudiante> estudianteEncontrado = buscarPorId(id);

		if (estudianteEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Estudiante estudiante = estudianteEncontrado.get();

		if (datosEstudiante.getNombre() != null) {
			estudiante.setNombre(datosEstudiante.getNombre());
		}
		if (datosEstudiante.getApellido() != null) {
			estudiante.setApellido(datosEstudiante.getApellido());
		}
		if (datosEstudiante.getCarrera() != null) {
			estudiante.setCarrera(datosEstudiante.getCarrera());
		}
		if (datosEstudiante.getEdad() != null) {
			estudiante.setEdad(datosEstudiante.getEdad());
		}

		return ResponseEntity.ok(estudiante);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Optional<Estudiante> estudianteEncontrado = buscarPorId(id);

		if (estudianteEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		estudiantes.remove(estudianteEncontrado.get());
		return ResponseEntity.noContent().build();
	}

	private Optional<Estudiante> buscarPorId(Long id) {
		return estudiantes.stream()
				.filter(estudiante -> estudiante.getId().equals(id))
				.findFirst();
	}
}

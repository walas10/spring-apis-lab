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

import com.lab.apis.model.Tarea;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

	private final List<Tarea> tareas = new ArrayList<>();
	private long siguienteId = 6;

	public TareaController() {
		tareas.add(new Tarea(1L, "Completar laboratorio", "Finalizar las APIs REST", "ALTA", false));
		tareas.add(new Tarea(2L, "Estudiar Spring", "Repasar controladores REST", "MEDIA", false));
		tareas.add(new Tarea(3L, "Revisar ejercicios", "Comprobar el funcionamiento de las APIs", "ALTA", true));
		tareas.add(new Tarea(4L, "Preparar documentacion", "Documentar los endpoints creados", "BAJA", false));
		tareas.add(new Tarea(5L, "Probar Postman", "Ejecutar las solicitudes de la coleccion", "MEDIA", false));
	}

	@GetMapping
	public List<Tarea> obtenerTodas() {
		return tareas;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Tarea> obtenerPorId(@PathVariable Long id) {
		return buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Tarea> crear(@RequestBody Tarea tarea) {
		tarea.setId(siguienteId++);
		tareas.add(tarea);
		return ResponseEntity.status(HttpStatus.CREATED).body(tarea);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Tarea> actualizarCompleta(@PathVariable Long id,
			@RequestBody Tarea datosTarea) {
		Optional<Tarea> tareaEncontrada = buscarPorId(id);

		if (tareaEncontrada.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Tarea tarea = tareaEncontrada.get();
		tarea.setId(id);
		tarea.setTitulo(datosTarea.getTitulo());
		tarea.setDescripcion(datosTarea.getDescripcion());
		tarea.setPrioridad(datosTarea.getPrioridad());
		tarea.setCompletada(datosTarea.getCompletada());

		return ResponseEntity.ok(tarea);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Tarea> actualizarParcial(@PathVariable Long id,
			@RequestBody Tarea datosTarea) {
		Optional<Tarea> tareaEncontrada = buscarPorId(id);

		if (tareaEncontrada.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Tarea tarea = tareaEncontrada.get();

		if (datosTarea.getTitulo() != null) {
			tarea.setTitulo(datosTarea.getTitulo());
		}
		if (datosTarea.getDescripcion() != null) {
			tarea.setDescripcion(datosTarea.getDescripcion());
		}
		if (datosTarea.getPrioridad() != null) {
			tarea.setPrioridad(datosTarea.getPrioridad());
		}
		if (datosTarea.getCompletada() != null) {
			tarea.setCompletada(datosTarea.getCompletada());
		}

		return ResponseEntity.ok(tarea);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Optional<Tarea> tareaEncontrada = buscarPorId(id);

		if (tareaEncontrada.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		tareas.remove(tareaEncontrada.get());
		return ResponseEntity.noContent().build();
	}

	private Optional<Tarea> buscarPorId(Long id) {
		return tareas.stream()
				.filter(tarea -> tarea.getId().equals(id))
				.findFirst();
	}
}

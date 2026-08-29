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

import com.lab.apis.model.Empleado;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

	private final List<Empleado> empleados = new ArrayList<>();
	private long siguienteId = 6;

	public EmpleadoController() {
		empleados.add(new Empleado(1L, "Andrea Lopez", "Desarrolladora", 8500.00, "Tecnologia"));
		empleados.add(new Empleado(2L, "Carlos Perez", "Contador", 7200.00, "Finanzas"));
		empleados.add(new Empleado(3L, "Maria Garcia", "Analista", 7800.00, "Operaciones"));
		empleados.add(new Empleado(4L, "Luis Ramirez", "Supervisor", 9000.00, "Produccion"));
		empleados.add(new Empleado(5L, "Sofia Martinez", "Reclutadora", 6800.00, "Recursos Humanos"));
	}

	@GetMapping
	public List<Empleado> obtenerTodos() {
		return empleados;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Empleado> obtenerPorId(@PathVariable Long id) {
		return buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Empleado> crear(@RequestBody Empleado empleado) {
		empleado.setId(siguienteId++);
		empleados.add(empleado);
		return ResponseEntity.status(HttpStatus.CREATED).body(empleado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Empleado> actualizarCompleto(@PathVariable Long id,
			@RequestBody Empleado datosEmpleado) {
		Optional<Empleado> empleadoEncontrado = buscarPorId(id);

		if (empleadoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Empleado empleado = empleadoEncontrado.get();
		empleado.setId(id);
		empleado.setNombre(datosEmpleado.getNombre());
		empleado.setPuesto(datosEmpleado.getPuesto());
		empleado.setSalario(datosEmpleado.getSalario());
		empleado.setDepartamento(datosEmpleado.getDepartamento());

		return ResponseEntity.ok(empleado);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Empleado> actualizarParcial(@PathVariable Long id,
			@RequestBody Empleado datosEmpleado) {
		Optional<Empleado> empleadoEncontrado = buscarPorId(id);

		if (empleadoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Empleado empleado = empleadoEncontrado.get();

		if (datosEmpleado.getNombre() != null) {
			empleado.setNombre(datosEmpleado.getNombre());
		}
		if (datosEmpleado.getPuesto() != null) {
			empleado.setPuesto(datosEmpleado.getPuesto());
		}
		if (datosEmpleado.getSalario() != null) {
			empleado.setSalario(datosEmpleado.getSalario());
		}
		if (datosEmpleado.getDepartamento() != null) {
			empleado.setDepartamento(datosEmpleado.getDepartamento());
		}

		return ResponseEntity.ok(empleado);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Optional<Empleado> empleadoEncontrado = buscarPorId(id);

		if (empleadoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		empleados.remove(empleadoEncontrado.get());
		return ResponseEntity.noContent().build();
	}

	private Optional<Empleado> buscarPorId(Long id) {
		return empleados.stream()
				.filter(empleado -> empleado.getId().equals(id))
				.findFirst();
	}
}

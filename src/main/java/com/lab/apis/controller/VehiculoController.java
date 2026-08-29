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

import com.lab.apis.model.Vehiculo;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

	private final List<Vehiculo> vehiculos = new ArrayList<>();
	private long siguienteId = 6;

	public VehiculoController() {
		vehiculos.add(new Vehiculo(1L, "Toyota", "Corolla", 2024, 185000.0));
		vehiculos.add(new Vehiculo(2L, "Honda", "Civic", 2023, 180000.0));
		vehiculos.add(new Vehiculo(3L, "Mazda", "CX-5", 2024, 260000.0));
		vehiculos.add(new Vehiculo(4L, "Nissan", "Sentra", 2022, 145000.0));
		vehiculos.add(new Vehiculo(5L, "Kia", "Sportage", 2023, 225000.0));
	}

	@GetMapping
	public List<Vehiculo> obtenerTodos() {
		return vehiculos;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Vehiculo> obtenerPorId(@PathVariable Long id) {
		return buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Vehiculo> crear(@RequestBody Vehiculo vehiculo) {
		vehiculo.setId(siguienteId++);
		vehiculos.add(vehiculo);
		return ResponseEntity.status(HttpStatus.CREATED).body(vehiculo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Vehiculo> actualizarCompleto(@PathVariable Long id,
			@RequestBody Vehiculo datosVehiculo) {
		Optional<Vehiculo> vehiculoEncontrado = buscarPorId(id);

		if (vehiculoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Vehiculo vehiculo = vehiculoEncontrado.get();
		vehiculo.setId(id);
		vehiculo.setMarca(datosVehiculo.getMarca());
		vehiculo.setModelo(datosVehiculo.getModelo());
		vehiculo.setAnio(datosVehiculo.getAnio());
		vehiculo.setPrecio(datosVehiculo.getPrecio());

		return ResponseEntity.ok(vehiculo);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Vehiculo> actualizarParcial(@PathVariable Long id,
			@RequestBody Vehiculo datosVehiculo) {
		Optional<Vehiculo> vehiculoEncontrado = buscarPorId(id);

		if (vehiculoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Vehiculo vehiculo = vehiculoEncontrado.get();

		if (datosVehiculo.getMarca() != null) {
			vehiculo.setMarca(datosVehiculo.getMarca());
		}
		if (datosVehiculo.getModelo() != null) {
			vehiculo.setModelo(datosVehiculo.getModelo());
		}
		if (datosVehiculo.getAnio() != null) {
			vehiculo.setAnio(datosVehiculo.getAnio());
		}
		if (datosVehiculo.getPrecio() != null) {
			vehiculo.setPrecio(datosVehiculo.getPrecio());
		}

		return ResponseEntity.ok(vehiculo);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Optional<Vehiculo> vehiculoEncontrado = buscarPorId(id);

		if (vehiculoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		vehiculos.remove(vehiculoEncontrado.get());
		return ResponseEntity.noContent().build();
	}

	private Optional<Vehiculo> buscarPorId(Long id) {
		return vehiculos.stream()
				.filter(vehiculo -> vehiculo.getId().equals(id))
				.findFirst();
	}
}

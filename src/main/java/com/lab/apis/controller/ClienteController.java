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

import com.lab.apis.model.Cliente;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

	private final List<Cliente> clientes = new ArrayList<>();
	private long siguienteId = 6;

	public ClienteController() {
		clientes.add(new Cliente(1L, "Carlos", "Ramirez", "carlos.ramirez@email.com", "55551234"));
		clientes.add(new Cliente(2L, "Ana", "Lopez", "ana.lopez@email.com", "55552345"));
		clientes.add(new Cliente(3L, "Luis", "Garcia", "luis.garcia@email.com", "55553456"));
		clientes.add(new Cliente(4L, "Maria", "Martinez", "maria.martinez@email.com", "55554567"));
		clientes.add(new Cliente(5L, "Sofia", "Perez", "sofia.perez@email.com", "55555678"));
	}

	@GetMapping
	public List<Cliente> obtenerTodos() {
		return clientes;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
		return buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
		cliente.setId(siguienteId++);
		clientes.add(cliente);
		return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cliente> actualizarCompleto(@PathVariable Long id,
			@RequestBody Cliente datosCliente) {
		Optional<Cliente> clienteEncontrado = buscarPorId(id);

		if (clienteEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Cliente cliente = clienteEncontrado.get();
		cliente.setId(id);
		cliente.setNombre(datosCliente.getNombre());
		cliente.setApellido(datosCliente.getApellido());
		cliente.setCorreo(datosCliente.getCorreo());
		cliente.setTelefono(datosCliente.getTelefono());

		return ResponseEntity.ok(cliente);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Cliente> actualizarParcial(@PathVariable Long id,
			@RequestBody Cliente datosCliente) {
		Optional<Cliente> clienteEncontrado = buscarPorId(id);

		if (clienteEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Cliente cliente = clienteEncontrado.get();

		if (datosCliente.getNombre() != null) {
			cliente.setNombre(datosCliente.getNombre());
		}
		if (datosCliente.getApellido() != null) {
			cliente.setApellido(datosCliente.getApellido());
		}
		if (datosCliente.getCorreo() != null) {
			cliente.setCorreo(datosCliente.getCorreo());
		}
		if (datosCliente.getTelefono() != null) {
			cliente.setTelefono(datosCliente.getTelefono());
		}

		return ResponseEntity.ok(cliente);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Optional<Cliente> clienteEncontrado = buscarPorId(id);

		if (clienteEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		clientes.remove(clienteEncontrado.get());
		return ResponseEntity.noContent().build();
	}

	private Optional<Cliente> buscarPorId(Long id) {
		return clientes.stream()
				.filter(cliente -> cliente.getId().equals(id))
				.findFirst();
	}
}

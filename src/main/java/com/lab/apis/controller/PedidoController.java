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

import com.lab.apis.model.Pedido;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

	private final List<Pedido> pedidos = new ArrayList<>();
	private long siguienteId = 6;

	public PedidoController() {
		pedidos.add(new Pedido(1L, "Carlos Ramirez", "Laptop", 2, 13000.0, "PENDIENTE"));
		pedidos.add(new Pedido(2L, "Ana Lopez", "Teclado", 1, 450.0, "PROCESANDO"));
		pedidos.add(new Pedido(3L, "Luis Garcia", "Monitor", 2, 3200.0, "ENVIADO"));
		pedidos.add(new Pedido(4L, "Maria Martinez", "Mouse", 3, 750.0, "ENTREGADO"));
		pedidos.add(new Pedido(5L, "Sofia Perez", "Impresora", 1, 1850.0, "PENDIENTE"));
	}

	@GetMapping
	public List<Pedido> obtenerTodos() {
		return pedidos;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
		return buscarPorId(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Pedido> crear(@RequestBody Pedido pedido) {
		pedido.setId(siguienteId++);
		pedidos.add(pedido);
		return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Pedido> actualizarCompleto(@PathVariable Long id,
			@RequestBody Pedido datosPedido) {
		Optional<Pedido> pedidoEncontrado = buscarPorId(id);

		if (pedidoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pedido pedido = pedidoEncontrado.get();
		pedido.setId(id);
		pedido.setCliente(datosPedido.getCliente());
		pedido.setProducto(datosPedido.getProducto());
		pedido.setCantidad(datosPedido.getCantidad());
		pedido.setTotal(datosPedido.getTotal());
		pedido.setEstado(datosPedido.getEstado());

		return ResponseEntity.ok(pedido);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Pedido> actualizarParcial(@PathVariable Long id,
			@RequestBody Pedido datosPedido) {
		Optional<Pedido> pedidoEncontrado = buscarPorId(id);

		if (pedidoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Pedido pedido = pedidoEncontrado.get();

		if (datosPedido.getCliente() != null) {
			pedido.setCliente(datosPedido.getCliente());
		}
		if (datosPedido.getProducto() != null) {
			pedido.setProducto(datosPedido.getProducto());
		}
		if (datosPedido.getCantidad() != null) {
			pedido.setCantidad(datosPedido.getCantidad());
		}
		if (datosPedido.getTotal() != null) {
			pedido.setTotal(datosPedido.getTotal());
		}
		if (datosPedido.getEstado() != null) {
			pedido.setEstado(datosPedido.getEstado());
		}

		return ResponseEntity.ok(pedido);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		Optional<Pedido> pedidoEncontrado = buscarPorId(id);

		if (pedidoEncontrado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		pedidos.remove(pedidoEncontrado.get());
		return ResponseEntity.noContent().build();
	}

	private Optional<Pedido> buscarPorId(Long id) {
		return pedidos.stream()
				.filter(pedido -> pedido.getId().equals(id))
				.findFirst();
	}
}

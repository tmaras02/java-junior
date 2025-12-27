package hr.abysalto.hiring.api.junior.controller;

import hr.abysalto.hiring.api.junior.manager.OrderManager;
import hr.abysalto.hiring.api.junior.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderManager orderManager;

	@GetMapping("/list")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(orderManager.getAllOrders());
	}

	@PostMapping("/add")
	public ResponseEntity<?> add(@RequestBody Order order) {
		return ResponseEntity.ok(orderManager.save(order));
	}

	@PutMapping("/{id}/status/{status}")
	public ResponseEntity<?> changeStatus(@PathVariable Long id, @PathVariable String status) {
		orderManager.changeStatus(id, status);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/sorted")
	public ResponseEntity<?> sortedByTotalPrice() {
		return ResponseEntity.ok(orderManager.getOrdersSortedByTotalPrice());
	}
}

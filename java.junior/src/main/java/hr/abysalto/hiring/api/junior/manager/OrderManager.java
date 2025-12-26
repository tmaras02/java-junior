package hr.abysalto.hiring.api.junior.manager;

import hr.abysalto.hiring.api.junior.model.Order;

import java.util.List;

public interface OrderManager {
	Iterable<Order> getAllOrders();
	Order getById(Long id);
	Order save(Order order);
	void changeStatus(Long orderId, String status);
	List<Order> getOrdersSortedByTotalPrice();
}

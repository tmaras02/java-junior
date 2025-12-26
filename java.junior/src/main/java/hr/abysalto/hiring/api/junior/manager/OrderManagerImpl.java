package hr.abysalto.hiring.api.junior.manager;

import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import hr.abysalto.hiring.api.junior.model.OrderStatus;
import hr.abysalto.hiring.api.junior.repository.OrderItemRepository;
import hr.abysalto.hiring.api.junior.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class OrderManagerImpl implements OrderManager {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Override
	public Iterable<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public Order getById(Long id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Override
	public Order save(Order order) {
		BigDecimal total = calculateTotalPrice(order.getOrderNr());
		order.setTotalPrice(total);
		return orderRepository.save(order);
	}

	@Override
	public void changeStatus(Long orderId, String status) {
		Order order = getById(orderId);
		if (order != null) {
			order.setOrderStatus(OrderStatus.fromString(status));
			orderRepository.save(order);
		}
	}

	@Override
	public List<Order> getOrdersSortedByTotalPrice() {
		return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
				.sorted(Comparator.comparing(Order::getTotalPrice))
				.collect(Collectors.toList());
	}

	private BigDecimal calculateTotalPrice(Long orderId) {
		List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
		return items.stream()
				.map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}

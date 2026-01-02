package hr.abysalto.hiring.api.junior.manager;

import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import hr.abysalto.hiring.api.junior.model.OrderStatus;

import java.util.List;

public interface OrderManager {

    Order createOrder(Order order, List<OrderItem> items);
    List<OrderItem> getOrderItems(Long orderId);
    List<OrderItem> getOrderItemsByOrderIds(List<Long> orderIds);
    Iterable<Order> getAllOrders();
    List<Order> getAllOrdersSortedByTotalPrice(boolean descending);
    Order updateStatus(Long orderId, OrderStatus status);
}

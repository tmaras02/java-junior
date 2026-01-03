package hr.abysalto.hiring.api.junior.manager;

import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import hr.abysalto.hiring.api.junior.model.OrderStatus;
import hr.abysalto.hiring.api.junior.repository.OrderItemRepository;
import hr.abysalto.hiring.api.junior.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class OrderManagerImpl implements OrderManager {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderManagerImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional
    public Order createOrder(Order order, List<OrderItem> items) {

        if (order == null) {
            throw new IllegalArgumentException("Order is null");
        }
        if (order.getOrderStatus() == null) {
            throw new IllegalArgumentException("Order status is null");
        }

        BigDecimal total = BigDecimal.ZERO;

        if (items != null) {
            for (OrderItem item : items) {
                if (item == null) continue;

                BigDecimal price =
                        item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO;

                short quantity =
                        item.getQuantity() != null ? item.getQuantity() : 0;

                total = total.add(
                        price.multiply(BigDecimal.valueOf(quantity))
                );
            }
        }

        order.setTotalPrice(total);

        Order savedOrder = orderRepository.save(order);

        if (items != null) {
            short itemNr = 1;

            for (OrderItem item : items) {
                if (item == null) continue;

                item.setOrderId(savedOrder.getOrderId());

                if (item.getItemNr() == null) {
                    item.setItemNr(itemNr++);
                }

                orderItemRepository.save(item);
            }
        }

        return savedOrder;
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        if (orderId == null) {
            return List.of();
        }
        return orderItemRepository.findByOrderId(orderId);
    }

    @Override
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllOrdersSortedByTotalPrice(boolean descending) {

        List<Order> orders = new ArrayList<>();
        orderRepository.findAll().forEach(orders::add);

        Comparator<Order> comparator = Comparator.comparing(
                order -> order.getTotalPrice() != null
                        ? order.getTotalPrice()
                        : BigDecimal.ZERO
        );

        orders.sort(descending ? comparator.reversed() : comparator);

        return orders;
    }

    @Override
    @Transactional
    public Order updateStatus(Long orderId, OrderStatus status) {

        if (orderId == null) {
            return null;
        }
        if (status == null) {
            throw new IllegalArgumentException("Order status is null");
        }

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }

        order.setOrderStatus(status);
        return orderRepository.save(order);
    }
}

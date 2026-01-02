package hr.abysalto.hiring.api.junior.controller;

import hr.abysalto.hiring.api.junior.dto.CreateOrderRequestDTO;
import hr.abysalto.hiring.api.junior.dto.OrderDTO;
import hr.abysalto.hiring.api.junior.dto.OrderResponseDTO;
import hr.abysalto.hiring.api.junior.dto.StatusUpdateRequestDTO;
import hr.abysalto.hiring.api.junior.manager.OrderDtoManager;
import hr.abysalto.hiring.api.junior.manager.OrderManager;
import hr.abysalto.hiring.api.junior.model.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders", description = "for handling orders")
@RestController()
@RequestMapping("/orders")
public class OrderController {

    private final OrderManager orderManager;
    private final OrderDtoManager orderDtoManager;

    public OrderController(OrderManager orderManager, OrderDtoManager orderDtoManager) {
        this.orderManager = orderManager;
        this.orderDtoManager = orderDtoManager;
    }

    @Operation(
            summary = "Create new order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(
            @RequestBody CreateOrderRequestDTO req) {

        if (req == null || req.getOrder() == null) {
            return ResponseEntity.badRequest().build();
        }

        Order createdOrder =
                orderManager.createOrder(req.getOrder(), req.getItems());

        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrder(createdOrder);
        response.setItems(
                orderManager.getOrderItems(createdOrder.getOrderId())
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get all orders",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class))
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAll() {
        try {
            return ResponseEntity.ok(
                    orderDtoManager.getAllOrderDtos(orderManager.getAllOrders())
            );
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Change order status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> changeStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequestDTO req) {

        if (req == null || req.getStatus() == null) {
            return ResponseEntity.badRequest().build();
        }

        Order updatedOrder =
                orderManager.updateStatus(id, req.getStatus());

        return (updatedOrder == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(updatedOrder);
    }

    @Operation(summary = "Get orders sorted by total price")
    @GetMapping("/sorted")
    public ResponseEntity<List<OrderDTO>> sortOrdersByTotalPrice(
            @RequestParam(defaultValue = "false") boolean descending) {

        List<Order> sortedOrders =
                orderManager.getAllOrdersSortedByTotalPrice(descending);

        return ResponseEntity.ok(
                orderDtoManager.getAllOrderDtos(sortedOrders)
        );
    }
}

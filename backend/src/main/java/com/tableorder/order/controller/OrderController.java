package com.tableorder.order.controller;

import com.tableorder.menu.repository.MenuRepository;
import com.tableorder.order.dto.*;
import com.tableorder.order.repository.OrderRepository;
import com.tableorder.table.repository.TableRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final TableRepository tableRepository;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        if (!tableRepository.existsById(request.getTableId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "테이블을 찾을 수 없습니다");
        }

        List<OrderResponse.OrderItemResponse> items = new ArrayList<>();
        int totalAmount = 0;

        for (var itemReq : request.getItems()) {
            var menu = menuRepository.findById(itemReq.getMenuId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "메뉴를 찾을 수 없습니다"));
            int subtotal = menu.getPrice() * itemReq.getQuantity();
            totalAmount += subtotal;
            items.add(new OrderResponse.OrderItemResponse(
                    menu.getId(), menu.getName(), itemReq.getQuantity(), menu.getPrice()));
        }

        OrderResponse response = orderRepository.save(request.getTableId(), items, totalAmount);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(@RequestParam Long tableId) {
        return ResponseEntity.ok(orderRepository.findByTableId(tableId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다")));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다");
        }
        orderRepository.deleteById(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history")
    public ResponseEntity<OrderHistoryResponse> getOrderHistory(
            @RequestParam Long tableId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<OrderResponse> orders = orderRepository.findByTableId(tableId);
        long total = orders.size();
        int totalPages = (int) Math.ceil((double) total / size);

        List<OrderHistoryResponse.OrderHistoryItem> content = orders.stream()
                .skip((long) page * size)
                .limit(size)
                .map(o -> new OrderHistoryResponse.OrderHistoryItem(
                        o.getOrderId(), o.getOrderNumber(),
                        o.getItems().stream()
                                .map(i -> new OrderHistoryResponse.OrderHistoryItemDetail(
                                        i.getMenuName(), i.getQuantity(), i.getUnitPrice()))
                                .toList(),
                        o.getTotalAmount(), o.getCreatedAt()))
                .toList();

        return ResponseEntity.ok(new OrderHistoryResponse(content, totalPages, total, page));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<DashboardResponse>> getDashboard() {
        var allEntries = orderRepository.findAllEntries();

        Map<Long, List<OrderRepository.OrderEntry>> byTable = allEntries.stream()
                .collect(Collectors.groupingBy(OrderRepository.OrderEntry::tableId));

        List<DashboardResponse> dashboard = new ArrayList<>();
        for (var tableEntry : tableRepository.findAll()) {
            List<OrderRepository.OrderEntry> tableOrders = byTable.getOrDefault(tableEntry.getId(), List.of());

            int totalAmount = tableOrders.stream()
                    .mapToInt(e -> e.response().getTotalAmount())
                    .sum();

            List<DashboardResponse.DashboardOrderItem> recentOrders = tableOrders.stream()
                    .sorted(Comparator.comparing(e -> e.response().getCreatedAt(), Comparator.reverseOrder()))
                    .limit(5)
                    .map(e -> {
                        OrderResponse o = e.response();
                        String menuSummary = o.getItems().stream()
                                .map(i -> i.getMenuName() + " x" + i.getQuantity())
                                .collect(Collectors.joining(", "));
                        return new DashboardResponse.DashboardOrderItem(
                                o.getOrderId(), o.getOrderNumber(), menuSummary,
                                o.getTotalAmount(), o.getCreatedAt());
                    })
                    .toList();

            boolean hasNewOrder = !tableOrders.isEmpty();
            dashboard.add(new DashboardResponse(
                    tableEntry.getId(), tableEntry.getNumber(),
                    totalAmount, recentOrders, hasNewOrder));
        }

        return ResponseEntity.ok(dashboard);
    }
}

package com.tableorder.order.repository;

import com.tableorder.order.dto.OrderResponse;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final Map<Long, OrderEntry> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);
    private final AtomicLong orderNumberSeq = new AtomicLong(0);

    public record OrderEntry(Long orderId, Long tableId, OrderResponse response) {}

    public OrderResponse save(Long tableId, List<OrderResponse.OrderItemResponse> items, int totalAmount) {
        long id = sequence.incrementAndGet();
        long num = orderNumberSeq.incrementAndGet();
        String orderNumber = String.format("ORD-%tF-%03d", new Date(), num);
        String createdAt = java.time.Instant.now().toString();

        OrderResponse response = new OrderResponse(id, orderNumber, items, totalAmount, createdAt);
        store.put(id, new OrderEntry(id, tableId, response));
        return response;
    }

    public Optional<OrderResponse> findById(Long orderId) {
        OrderEntry entry = store.get(orderId);
        return entry != null ? Optional.of(entry.response()) : Optional.empty();
    }

    public List<OrderResponse> findByTableId(Long tableId) {
        return store.values().stream()
                .filter(e -> e.tableId().equals(tableId))
                .map(OrderEntry::response)
                .sorted(Comparator.comparing(OrderResponse::getCreatedAt).reversed())
                .toList();
    }

    public List<OrderEntry> findAllEntries() {
        return new ArrayList<>(store.values());
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    public void deleteById(Long id) {
        store.remove(id);
    }

    public Long getTableIdByOrderId(Long orderId) {
        OrderEntry entry = store.get(orderId);
        return entry != null ? entry.tableId() : null;
    }
}

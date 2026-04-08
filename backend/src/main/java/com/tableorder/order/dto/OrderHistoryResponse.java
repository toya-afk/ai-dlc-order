package com.tableorder.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderHistoryResponse {
    private List<OrderHistoryItem> content;
    private Integer totalPages;
    private Long totalElements;
    private Integer number;

    @Getter
    @AllArgsConstructor
    public static class OrderHistoryItem {
        private Long orderId;
        private String orderNumber;
        private List<OrderHistoryItemDetail> items;
        private Integer totalAmount;
        private String completedAt;
    }

    @Getter
    @AllArgsConstructor
    public static class OrderHistoryItemDetail {
        private String menuName;
        private Integer quantity;
        private Integer unitPrice;
    }
}

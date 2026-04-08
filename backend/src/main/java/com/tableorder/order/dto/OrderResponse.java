package com.tableorder.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String orderNumber;
    private List<OrderItemResponse> items;
    private Integer totalAmount;
    private String createdAt;

    @Getter
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Long menuId;
        private String menuName;
        private Integer quantity;
        private Integer unitPrice;
    }
}

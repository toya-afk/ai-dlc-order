package com.tableorder.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardResponse {
    private Long tableId;
    private Integer tableNumber;
    private Integer totalAmount;
    private List<DashboardOrderItem> recentOrders;
    private Boolean hasNewOrder;

    @Getter
    @AllArgsConstructor
    public static class DashboardOrderItem {
        private Long orderId;
        private String orderNumber;
        private String menuSummary;
        private Integer totalAmount;
        private String createdAt;
    }
}

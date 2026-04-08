package com.tableorder.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateRequest {
    @NotNull
    private Long tableId;
    @NotNull
    private List<OrderItemRequest> items;

    @Getter
    @Setter
    public static class OrderItemRequest {
        @NotNull
        private Long menuId;
        @NotNull
        private Integer quantity;
    }
}

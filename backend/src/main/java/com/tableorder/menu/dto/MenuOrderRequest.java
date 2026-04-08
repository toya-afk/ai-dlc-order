package com.tableorder.menu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuOrderRequest {
    private Long menuId;
    private Integer displayOrder;
}

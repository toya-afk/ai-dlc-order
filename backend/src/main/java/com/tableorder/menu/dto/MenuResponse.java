package com.tableorder.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuResponse {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
    private Integer displayOrder;
    private Double averageRating;
}

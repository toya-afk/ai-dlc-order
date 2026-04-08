package com.tableorder.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRequest {
    @NotBlank
    private String name;
    @NotNull
    private Integer price;
    private String description;
    private String imageUrl;
    @NotNull
    private Long categoryId;
    @NotNull
    private Integer displayOrder;
}

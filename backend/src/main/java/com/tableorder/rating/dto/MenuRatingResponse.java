package com.tableorder.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuRatingResponse {
    private Long menuId;
    private Double averageScore;
    private Integer totalCount;
}

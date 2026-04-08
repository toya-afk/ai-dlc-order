package com.tableorder.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecommendationResponse {
    private String gender;
    private String ageGroup;
    private List<RecommendedMenu> recommendedMenus;

    @Getter
    @AllArgsConstructor
    public static class RecommendedMenu {
        private Long menuId;
        private String menuName;
        private String imageUrl;
        private Integer orderCount;
        private Double averageRating;
    }
}

package com.tableorder.rating.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RatingRequest {
    @NotNull
    private Long orderId;
    @NotNull
    private List<RatingItem> ratings;

    @Getter
    @Setter
    public static class RatingItem {
        @NotNull
        private Long menuId;
        @NotNull
        private Integer score;
    }
}

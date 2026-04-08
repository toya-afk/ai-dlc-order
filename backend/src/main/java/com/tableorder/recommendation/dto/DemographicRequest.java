package com.tableorder.recommendation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemographicRequest {
    @NotNull
    private Long orderId;
    @NotNull
    private String gender;
    @NotNull
    private String ageGroup;
}

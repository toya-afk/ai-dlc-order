package com.tableorder.table.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableUpdateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String password;
}

package com.tableorder.table.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableRequest {
    @NotNull
    private Integer number;
    @NotBlank
    private String name;
    @NotBlank
    private String password;
}

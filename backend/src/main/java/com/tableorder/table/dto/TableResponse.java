package com.tableorder.table.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TableResponse {
    private Long id;
    private Integer number;
    private String name;
    private String password;
}

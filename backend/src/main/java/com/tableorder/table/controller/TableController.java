package com.tableorder.table.controller;

import com.tableorder.table.dto.TableRequest;
import com.tableorder.table.dto.TableResponse;
import com.tableorder.table.dto.TableUpdateRequest;
import com.tableorder.table.repository.TableRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableRepository tableRepository;

    @GetMapping
    public ResponseEntity<List<TableResponse>> getTables() {
        return ResponseEntity.ok(tableRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<TableResponse> createTable(@Valid @RequestBody TableRequest request) {
        TableResponse created = tableRepository.save(request.getNumber(), request.getName(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{tableId}")
    public ResponseEntity<TableResponse> updateTable(@PathVariable Long tableId,
                                                     @Valid @RequestBody TableUpdateRequest request) {
        if (!tableRepository.existsById(tableId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "테이블을 찾을 수 없습니다");
        }
        return ResponseEntity.ok(tableRepository.update(tableId, request.getName(), request.getPassword()));
    }
}

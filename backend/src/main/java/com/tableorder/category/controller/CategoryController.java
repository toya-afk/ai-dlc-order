package com.tableorder.category.controller;

import com.tableorder.category.dto.CategoryRequest;
import com.tableorder.category.dto.CategoryResponse;
import com.tableorder.category.repository.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse created = categoryRepository.save(request.getName(), request.getDisplayOrder());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long categoryId,
                                                           @Valid @RequestBody CategoryRequest request) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다");
        }
        return ResponseEntity.ok(categoryRepository.update(categoryId, request.getName(), request.getDisplayOrder()));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다");
        }
        categoryRepository.deleteById(categoryId);
        return ResponseEntity.noContent().build();
    }
}

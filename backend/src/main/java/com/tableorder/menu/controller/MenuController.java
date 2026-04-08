package com.tableorder.menu.controller;

import com.tableorder.category.repository.CategoryRepository;
import com.tableorder.menu.dto.MenuOrderRequest;
import com.tableorder.menu.dto.MenuRequest;
import com.tableorder.menu.dto.MenuResponse;
import com.tableorder.menu.repository.MenuRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<MenuResponse>> getMenus(
            @RequestParam(required = false) Long categoryId) {
        List<MenuResponse> result = categoryId == null
                ? menuRepository.findAll()
                : menuRepository.findByCategoryId(categoryId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(@Valid @RequestBody MenuRequest request) {
        String categoryName = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다"))
                .getName();
        MenuResponse created = menuRepository.save(request.getName(), request.getPrice(),
                request.getDescription(), request.getImageUrl(),
                request.getCategoryId(), categoryName, request.getDisplayOrder());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponse> updateMenu(@PathVariable Long menuId,
                                                   @Valid @RequestBody MenuRequest request) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다");
        }
        String categoryName = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다"))
                .getName();
        return ResponseEntity.ok(menuRepository.update(menuId, request.getName(), request.getPrice(),
                request.getDescription(), request.getImageUrl(),
                request.getCategoryId(), categoryName, request.getDisplayOrder()));
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다");
        }
        menuRepository.deleteById(menuId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/order")
    public ResponseEntity<Void> updateMenuOrder(@RequestBody List<MenuOrderRequest> requests) {
        for (MenuOrderRequest req : requests) {
            menuRepository.updateDisplayOrder(req.getMenuId(), req.getDisplayOrder());
        }
        return ResponseEntity.noContent().build();
    }
}

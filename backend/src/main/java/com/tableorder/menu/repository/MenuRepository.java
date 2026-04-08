package com.tableorder.menu.repository;

import com.tableorder.menu.dto.MenuResponse;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class MenuRepository {

    private final Map<Long, MenuResponse> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public MenuRepository() {
        save("김치찌개", 9000, "얼큰한 김치찌개", null, 1L, "메인", 0);
        save("된장찌개", 8000, "구수한 된장찌개", null, 1L, "메인", 1);
        save("제육볶음", 10000, "매콤한 제육볶음", null, 1L, "메인", 2);
        save("계란말이", 5000, "부드러운 계란말이", null, 2L, "사이드", 0);
        save("김치전", 6000, "바삭한 김치전", null, 2L, "사이드", 1);
        save("콜라", 2000, "코카콜라 355ml", null, 3L, "음료", 0);
        save("사이다", 2000, "칠성사이다 355ml", null, 3L, "음료", 1);
    }

    public MenuResponse save(String name, int price, String description, String imageUrl,
                             Long categoryId, String categoryName, int displayOrder) {
        long id = sequence.incrementAndGet();
        MenuResponse menu = new MenuResponse(id, name, price, description, imageUrl,
                categoryId, categoryName, displayOrder, 0.0);
        store.put(id, menu);
        return menu;
    }

    public MenuResponse update(Long id, String name, int price, String description, String imageUrl,
                               Long categoryId, String categoryName, int displayOrder) {
        MenuResponse existing = store.get(id);
        double rating = existing != null ? existing.getAverageRating() : 0.0;
        MenuResponse menu = new MenuResponse(id, name, price, description, imageUrl,
                categoryId, categoryName, displayOrder, rating);
        store.put(id, menu);
        return menu;
    }

    public void updateRating(Long id, double averageRating) {
        MenuResponse existing = store.get(id);
        if (existing != null) {
            store.put(id, new MenuResponse(existing.getId(), existing.getName(), existing.getPrice(),
                    existing.getDescription(), existing.getImageUrl(), existing.getCategoryId(),
                    existing.getCategoryName(), existing.getDisplayOrder(), averageRating));
        }
    }

    public List<MenuResponse> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparing(MenuResponse::getDisplayOrder))
                .toList();
    }

    public List<MenuResponse> findByCategoryId(Long categoryId) {
        return store.values().stream()
                .filter(m -> m.getCategoryId().equals(categoryId))
                .sorted(Comparator.comparing(MenuResponse::getDisplayOrder))
                .toList();
    }

    public Optional<MenuResponse> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    public void deleteById(Long id) {
        store.remove(id);
    }

    public void updateDisplayOrder(Long id, int displayOrder) {
        MenuResponse existing = store.get(id);
        if (existing != null) {
            store.put(id, new MenuResponse(existing.getId(), existing.getName(), existing.getPrice(),
                    existing.getDescription(), existing.getImageUrl(), existing.getCategoryId(),
                    existing.getCategoryName(), displayOrder, existing.getAverageRating()));
        }
    }
}

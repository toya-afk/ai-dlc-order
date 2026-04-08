package com.tableorder.category.repository;

import com.tableorder.category.dto.CategoryResponse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CategoryRepository {

    private final Map<Long, CategoryResponse> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public CategoryRepository() {
        save("메인", 0);
        save("사이드", 1);
        save("음료", 2);
    }

    public CategoryResponse save(String name, int displayOrder) {
        long id = sequence.incrementAndGet();
        CategoryResponse category = new CategoryResponse(id, name, displayOrder);
        store.put(id, category);
        return category;
    }

    public CategoryResponse update(Long id, String name, int displayOrder) {
        CategoryResponse category = new CategoryResponse(id, name, displayOrder);
        store.put(id, category);
        return category;
    }

    public List<CategoryResponse> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparing(CategoryResponse::getDisplayOrder))
                .toList();
    }

    public Optional<CategoryResponse> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    public void deleteById(Long id) {
        store.remove(id);
    }
}

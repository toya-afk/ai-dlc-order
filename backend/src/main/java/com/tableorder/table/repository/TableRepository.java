package com.tableorder.table.repository;

import com.tableorder.table.dto.TableResponse;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TableRepository {

    private final Map<Long, TableResponse> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public TableRepository() {
        save(1, "테이블 1", "0000");
        save(2, "테이블 2", "0000");
        save(3, "테이블 3", "0000");
    }

    public TableResponse save(int number, String name, String password) {
        long id = sequence.incrementAndGet();
        TableResponse table = new TableResponse(id, number, name, password);
        store.put(id, table);
        return table;
    }

    public TableResponse update(Long id, String name, String password) {
        TableResponse existing = store.get(id);
        TableResponse table = new TableResponse(id, existing.getNumber(), name, password);
        store.put(id, table);
        return table;
    }

    public List<TableResponse> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparing(TableResponse::getNumber))
                .toList();
    }

    public Optional<TableResponse> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<TableResponse> findByNumber(int number) {
        return store.values().stream()
                .filter(t -> t.getNumber() == number)
                .findFirst();
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }
}

package com.tableorder.recommendation.repository;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RecommendationRepository {

    // key: "gender:ageGroup", value: menuId -> order count
    private final Map<String, Map<Long, Integer>> store = new ConcurrentHashMap<>();

    public void saveDemographic(String gender, String ageGroup, List<Long> menuIds) {
        String key = gender + ":" + ageGroup;
        Map<Long, Integer> menuCounts = store.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
        for (Long menuId : menuIds) {
            menuCounts.merge(menuId, 1, Integer::sum);
        }
    }

    public Map<Long, Integer> getMenuCountsByDemographic(String gender, String ageGroup) {
        String key = gender + ":" + ageGroup;
        return store.getOrDefault(key, Map.of());
    }
}

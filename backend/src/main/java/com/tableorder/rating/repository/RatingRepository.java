package com.tableorder.rating.repository;

import com.tableorder.rating.dto.MenuRatingResponse;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RatingRepository {

    // menuId -> list of scores
    private final Map<Long, List<Integer>> store = new ConcurrentHashMap<>();

    public void addRating(Long menuId, int score) {
        store.computeIfAbsent(menuId, k -> Collections.synchronizedList(new ArrayList<>())).add(score);
    }

    public List<MenuRatingResponse> getAllAverages() {
        return store.entrySet().stream()
                .map(e -> {
                    List<Integer> scores = e.getValue();
                    double avg = scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                    return new MenuRatingResponse(e.getKey(),
                            Math.round(avg * 10.0) / 10.0,
                            scores.size());
                })
                .sorted(Comparator.comparing(MenuRatingResponse::getMenuId))
                .toList();
    }

    public double getAverageByMenuId(Long menuId) {
        List<Integer> scores = store.get(menuId);
        if (scores == null || scores.isEmpty()) return 0.0;
        return Math.round(scores.stream().mapToInt(Integer::intValue).average().orElse(0.0) * 10.0) / 10.0;
    }
}

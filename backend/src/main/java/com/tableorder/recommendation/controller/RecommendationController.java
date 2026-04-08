package com.tableorder.recommendation.controller;

import com.tableorder.menu.repository.MenuRepository;
import com.tableorder.order.repository.OrderRepository;
import com.tableorder.rating.repository.RatingRepository;
import com.tableorder.recommendation.dto.DemographicRequest;
import com.tableorder.recommendation.dto.RecommendationResponse;
import com.tableorder.recommendation.repository.RecommendationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationRepository recommendationRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final RatingRepository ratingRepository;

    @PostMapping("/demographic")
    public ResponseEntity<Void> saveDemographic(@Valid @RequestBody DemographicRequest request) {
        var order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문을 찾을 수 없습니다"));

        List<Long> menuIds = order.getItems().stream()
                .map(i -> i.getMenuId())
                .toList();

        recommendationRepository.saveDemographic(request.getGender(), request.getAgeGroup(), menuIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<RecommendationResponse> getRecommendations(
            @RequestParam String gender,
            @RequestParam String ageGroup) {

        Map<Long, Integer> menuCounts = recommendationRepository.getMenuCountsByDemographic(gender, ageGroup);

        List<RecommendationResponse.RecommendedMenu> recommended = menuCounts.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(5)
                .map(e -> {
                    var menu = menuRepository.findById(e.getKey());
                    return new RecommendationResponse.RecommendedMenu(
                            e.getKey(),
                            menu.map(m -> m.getName()).orElse("Unknown"),
                            menu.map(m -> m.getImageUrl()).orElse(null),
                            e.getValue(),
                            ratingRepository.getAverageByMenuId(e.getKey()));
                })
                .toList();

        return ResponseEntity.ok(new RecommendationResponse(gender, ageGroup, recommended));
    }
}

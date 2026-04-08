package com.tableorder.rating.controller;

import com.tableorder.menu.repository.MenuRepository;
import com.tableorder.rating.dto.MenuRatingResponse;
import com.tableorder.rating.dto.RatingRequest;
import com.tableorder.rating.repository.RatingRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingRepository ratingRepository;
    private final MenuRepository menuRepository;

    @PostMapping
    public ResponseEntity<Void> createRating(@Valid @RequestBody RatingRequest request) {
        for (var item : request.getRatings()) {
            ratingRepository.addRating(item.getMenuId(), item.getScore());
            double avg = ratingRepository.getAverageByMenuId(item.getMenuId());
            menuRepository.updateRating(item.getMenuId(), avg);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/menus")
    public ResponseEntity<List<MenuRatingResponse>> getMenuRatings() {
        return ResponseEntity.ok(ratingRepository.getAllAverages());
    }
}

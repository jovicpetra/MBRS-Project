package com.example.demo.services;
import com.example.demo.models.Review;

import java.util.List;

public interface ReviewService {
    List<Review> findAll();
    Review findOne(Long id);
    Review save(Review game);
    Review update(Review game);
    void remove(Long id);
}

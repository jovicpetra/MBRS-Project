package com.example.demo.services.impl;

import com.example.demo.models.Review;
import com.example.demo.repositories.ReviewRepository;
import com.example.demo.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Review findOne(Long id) {
        Optional<Review> reviewResponse = reviewRepository.findById(id);
        return reviewResponse.get();
    }

    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Review review) {
        Optional<Review> currentReviewResponse = reviewRepository.findById(review.getId());
        Review currentReview = currentReviewResponse.get();
        if (currentReview == null)
            return null;

        return reviewRepository.save(currentReview);
    }

    @Override
    public void remove(Long id) {
        reviewRepository.deleteById(id);
    }
}

package com.example.demo.controllers;


import com.example.demo.models.Review;
import com.example.demo.models.dto.ReviewDTO;
import com.example.demo.services.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/api/review")
public class ReviewController {
    @Autowired
    private
    ReviewService reviewService;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<ReviewDTO>> getAll() {
        List<Review> reviewList = reviewService.findAll();
        if (reviewList == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (Review review : reviewList) {
            ReviewDTO clientDTO = modelMapper.map(review, ReviewDTO.class);
            reviewDTOList.add(clientDTO);
        }

        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    ResponseEntity<ReviewDTO> appointmentService(@PathVariable Long id) {
        Review review = reviewService.findOne(id);
        if (review == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(modelMapper.map(review, ReviewDTO.class), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReviewDTO> create(@RequestBody @Valid ReviewDTO newGame) {

        Review createdReview = reviewService.save(modelMapper.map(newGame,Review.class));
        if (createdReview == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(modelMapper.map(createdReview, ReviewDTO.class), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<ReviewDTO> updateById(@RequestBody @Valid ReviewDTO review,
                                                @PathVariable Long id) {
        if (id != review.getId())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Review updated = reviewService.update(modelMapper.map(review,Review.class));
        if (updated == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(modelMapper.map(updated, ReviewDTO.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE) ResponseEntity<String> deleteById(@PathVariable Long id) {

        reviewService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

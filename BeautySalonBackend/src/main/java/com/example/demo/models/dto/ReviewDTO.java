package com.example.demo.models.dto;

import java.util.Date;

public class ReviewDTO {
    private long  id;
    private int rating ;
    private String comment;
    private Date reviewDate;

    public long getId() {
        return id;
    }
    public int getRating() {
        return rating;
    }
    public String getComment() {
        return comment;
    }
    public Date getReviewDate() {
        return reviewDate;
    }

    public void setId(long id) { this.id = id; }
    public void setRating(int rating) { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
    public void setReviewDate(Date reviewDate) { this.reviewDate = reviewDate; }
}

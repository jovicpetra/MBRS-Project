package com.example.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Review {
    @Id
    @GeneratedValue
    private long  id;
    @Column(name="rating" , length = 50)
    private int rating ;

    @Column(name="comment" , length = 50)
    private String comment;

    @Column(name="reviewDate" , length = 50, unique = true)
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

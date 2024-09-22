package com.example.demo.models.dto;


import java.util.Date;

public class TreatmentDTO {
    private long  id;
    private String name;
    private String description;
    private Double price;

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Double getPrice() {
        return price;
    }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }

}

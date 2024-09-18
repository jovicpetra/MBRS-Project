package com.example.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Treatment {
    @Id
    @GeneratedValue
    private long  id;
    @Column(name="name" , length = 50)
    private String name;
    @Column(name="description" , length = 50 )
    private String description;
    @Column(name="price" , length = 50)
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

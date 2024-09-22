package com.example.demo.models;


import com.example.demo.enums.Status;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Appointment {
    @Id
    @GeneratedValue
    private long  id;
    @Column(name="dateTime" , length = 50)
    private Date dateTime;
    @Column(name="status" , length = 50)
    private Status status;
    public long getId() {
        return id;
    }
    public Date getDate() {
        return dateTime;
    }
    public Status getStatus() { return status; }

    public void setId(long id) { this.id = id; }
    public void setDate(Date dateTime) { this.dateTime = dateTime; }
    public void setStatus(Status status) { this.status = status; }
}

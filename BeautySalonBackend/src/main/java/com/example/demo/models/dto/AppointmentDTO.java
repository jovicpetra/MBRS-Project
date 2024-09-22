package com.example.demo.models.dto;

import com.example.demo.enums.Status;

import java.util.Date;

public class AppointmentDTO {
    private long  id;
    private Date dateTime;
    private Status status;

    public long getId() {
        return id;
    }
    public Date getDate() {
        return dateTime;
    }
    public Status getStatus() {
        return status;
    }

    public void setId(long id) { this.id = id; }
    public void setDate(Date dateTime) { this.dateTime = dateTime; }
    public void setStatus(Status status) { this.status = status; }
}

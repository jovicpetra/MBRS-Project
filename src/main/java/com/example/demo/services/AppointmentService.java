package com.example.demo.services;

import com.example.demo.models.Appointment;

import java.util.List;

public interface AppointmentService {
    List<Appointment> findAll();
    Appointment findOne(Long id);
    Appointment save(Appointment game);
    Appointment update(Appointment game);
    void remove(Long id);
}

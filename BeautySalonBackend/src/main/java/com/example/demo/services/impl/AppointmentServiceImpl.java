package com.example.demo.services.impl;

import com.example.demo.models.Appointment;
import com.example.demo.repositories.AppointmentRepository;
import com.example.demo.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment findOne(Long id) {
        Optional<Appointment> AppointmentResponse = appointmentRepository.findById(id);
        return AppointmentResponse.get();
    }

    @Override
    public Appointment save(Appointment game) {
        return appointmentRepository.save(game);
    }

    @Override
    public Appointment update(Appointment game) {
        Optional<Appointment> currentAppointmentResponse = appointmentRepository.findById(game.getId());
        Appointment appointment = currentAppointmentResponse.get();
        if (appointment == null)
            return null;

        return appointmentRepository.save(game);
    }

    @Override
    public void remove(Long id) {
        appointmentRepository.deleteById(id);
    }

}





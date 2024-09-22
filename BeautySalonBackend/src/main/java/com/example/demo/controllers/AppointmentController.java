package com.example.demo.controllers;

import com.example.demo.models.Appointment;
import com.example.demo.models.dto.AppointmentDTO;
import com.example.demo.services.AppointmentService;
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
@RequestMapping(value = "/api/appointment")
public class AppointmentController {
    @Autowired
    private
    AppointmentService appointmentService;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<AppointmentDTO>> getAll() {
        List<Appointment> appointmentList = appointmentService.findAll();
        if (appointmentList == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<AppointmentDTO> appointmenDTOList = new ArrayList<>();

        for (Appointment appointment : appointmentList) {
            AppointmentDTO appointmentDTO = modelMapper.map(appointment, AppointmentDTO.class);
            appointmenDTOList.add(appointmentDTO);
        }

        return new ResponseEntity<>(appointmenDTOList, HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    ResponseEntity<AppointmentDTO> appointmentService(@PathVariable Long id) {
        Appointment appointment = appointmentService.findOne(id);
        if (appointment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(modelMapper.map(appointment, AppointmentDTO.class), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AppointmentDTO> create(@RequestBody @Valid AppointmentDTO newGame) {

        Appointment createdAppointment = appointmentService.save(modelMapper.map(newGame,Appointment.class));
        if (createdAppointment == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(modelMapper.map(createdAppointment, AppointmentDTO.class), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<AppointmentDTO> updateById(@RequestBody @Valid AppointmentDTO appointment,
                                              @PathVariable Long id) {
        if (id != appointment.getId())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Appointment updated = appointmentService.update(modelMapper.map(appointment,Appointment.class));
        if (updated == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(modelMapper.map(updated, AppointmentDTO.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE) ResponseEntity<String> deleteById(@PathVariable Long id) {

        appointmentService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

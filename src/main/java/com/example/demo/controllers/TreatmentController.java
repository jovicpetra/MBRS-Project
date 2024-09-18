package com.example.demo.controllers;

import com.example.demo.models.Treatment;
import com.example.demo.models.dto.TreatmentDTO;
import com.example.demo.services.TreatmentService;
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
@RequestMapping(value = "/api/treatment")
public class TreatmentController {
    @Autowired
    private
    TreatmentService treatmentService;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<TreatmentDTO>> getAll() {
        List<Treatment> treatmentList = treatmentService.findAll();
        if (treatmentList == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<TreatmentDTO> reviewDTOList = new ArrayList<>();

        for (Treatment treatment : treatmentList) {
            TreatmentDTO clientDTO = modelMapper.map(treatment, TreatmentDTO.class);
            reviewDTOList.add(clientDTO);
        }

        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    ResponseEntity<TreatmentDTO> appointmentService(@PathVariable Long id) {
        Treatment treatment = treatmentService.findOne(id);
        if (treatment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(modelMapper.map(treatment, TreatmentDTO.class), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TreatmentDTO> create(@RequestBody @Valid TreatmentDTO newGame) {

        Treatment treatmentReview = treatmentService.save(modelMapper.map(newGame,Treatment.class));
        if (treatmentReview == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(modelMapper.map(treatmentReview, TreatmentDTO.class), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<TreatmentDTO> updateById(@RequestBody @Valid TreatmentDTO review,
                                                @PathVariable Long id) {
        if (id != review.getId())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Treatment updated = treatmentService.update(modelMapper.map(review,Treatment.class));
        if (updated == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(modelMapper.map(updated, TreatmentDTO.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE) ResponseEntity<String> deleteById(@PathVariable Long id) {
        treatmentService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

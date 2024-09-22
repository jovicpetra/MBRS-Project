package com.example.demo.services.impl;

import com.example.demo.models.Treatment;
import com.example.demo.repositories.TreatmentRepository;
import com.example.demo.services.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TreatmentServiceImpl implements TreatmentService {

    @Autowired
    private TreatmentRepository treatmentRepository;
    @Override
    public List<Treatment> findAll() {
        return treatmentRepository.findAll();
    }

    @Override
    public Treatment findOne(Long id) {
        Optional<Treatment> treatmentResponse = treatmentRepository.findById(id);
        return treatmentResponse.get();
    }

    @Override
    public Treatment save(Treatment treatment) {
        return treatmentRepository.save(treatment);
    }

    @Override
    public Treatment update(Treatment treatment) {
        Optional<Treatment> currentTreatmentResponse = treatmentRepository.findById(treatment.getId());
        Treatment currentTreatment = currentTreatmentResponse.get();
        if (currentTreatment == null)
            return null;

        return treatmentRepository.save(currentTreatment);
    }

    @Override
    public void remove(Long id) {
        treatmentRepository.deleteById(id);
    }
}


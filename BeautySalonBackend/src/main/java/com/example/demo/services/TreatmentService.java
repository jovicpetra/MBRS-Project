package com.example.demo.services;

import com.example.demo.models.Treatment;

import java.util.List;

public interface TreatmentService {
    List<Treatment> findAll();
    Treatment findOne(Long id);
    Treatment save(Treatment game);
    Treatment update(Treatment game);
    void remove(Long id);
}

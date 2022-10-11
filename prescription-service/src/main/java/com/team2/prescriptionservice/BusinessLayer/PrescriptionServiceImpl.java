package com.team2.prescriptionservice.BusinessLayer;

import com.team2.prescriptionservice.DataLayer.Prescription;
import com.team2.prescriptionservice.DataLayer.PrescriptionRepo;
import com.team2.prescriptionservice.Exceptions.NotFoundException;

import java.util.Optional;

public class PrescriptionServiceImpl implements PrescriptionService  {
    private final PrescriptionRepo repository;

    public PrescriptionServiceImpl(PrescriptionRepo repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Prescription> findByPrescriptionId(int id) {
        try {
            //find prescription by prescriptionId
            return repository.findById(id);
        } catch (Exception e) {
            // if prescription not found
            throw new NotFoundException("User with ID: " + id + " was not found");
        }
    }
}

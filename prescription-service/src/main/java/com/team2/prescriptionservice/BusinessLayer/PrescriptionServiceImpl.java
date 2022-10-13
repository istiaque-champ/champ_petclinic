package com.team2.prescriptionservice.BusinessLayer;

import com.team2.prescriptionservice.DataLayer.Prescription;
import com.team2.prescriptionservice.DataLayer.PrescriptionRepo;
import com.team2.prescriptionservice.Exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionServiceImpl implements PrescriptionService  {
    private final PrescriptionRepo repository;

    public PrescriptionServiceImpl(PrescriptionRepo repository) {
        this.repository = repository;
    }

    @Override
    public Prescription findByPrescriptionId(int id) {
        try {
            //find prescription by prescriptionId
            return repository.findPrescriptionByPrescriptionId(id);
        } catch (Exception e) {
            // if prescription not found
            throw new NotFoundException("prescription with ID: " + id + " was not found");
        }
    }

    @Override
    public List<Prescription> findAllPrescriptions() {
        try {
            //find prescription by prescriptionId
            return (List<Prescription>) repository.findAll();
        } catch (Exception e) {
            // if prescription not found
            throw new NotFoundException("none found");
        }
    }
}

package com.team2.prescriptionservice.BusinessLayer;

import com.team2.prescriptionservice.DataLayer.Prescription;

import java.util.Optional;

public interface PrescriptionService {
    Optional<Prescription> findByPrescriptionId(int id);
}

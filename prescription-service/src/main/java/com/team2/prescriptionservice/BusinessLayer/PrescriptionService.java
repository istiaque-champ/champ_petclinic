package com.team2.prescriptionservice.BusinessLayer;

import com.team2.prescriptionservice.DataLayer.Prescription;
import com.team2.prescriptionservice.DataLayer.PrescriptionRequest;
import com.team2.prescriptionservice.DataLayer.PrescriptionResponse;

import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    PrescriptionResponse findByPrescriptionId(int id);
    List<PrescriptionResponse> findAllPrescriptions();
    PrescriptionResponse savePrescription(PrescriptionRequest prescriptionRequest);
}

package com.team2.prescriptionservice.BusinessLayer;

import com.team2.prescriptionservice.DataLayer.PrescriptionRequest;
import com.team2.prescriptionservice.DataLayer.PrescriptionResponse;

import java.util.List;

public interface PrescriptionService {
    PrescriptionResponse findByPrescriptionId(int id);
    List<PrescriptionResponse> findAllPrescriptions();
    PrescriptionResponse savePrescription(PrescriptionRequest prescriptionRequest);
    List<PrescriptionResponse> findAllPrescriptionsByPetId(Integer petId);
}

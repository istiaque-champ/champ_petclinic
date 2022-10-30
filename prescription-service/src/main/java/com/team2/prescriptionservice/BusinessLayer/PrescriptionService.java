package com.team2.prescriptionservice.BusinessLayer;

import com.team2.prescriptionservice.DataLayer.PrescriptionRequest;
import com.team2.prescriptionservice.DataLayer.PrescriptionResponse;

import java.util.List;

public interface PrescriptionService {
    PrescriptionResponse findByPrescriptionId(int id);
    PrescriptionResponse savePrescription(PrescriptionRequest prescriptionRequest);
    List<PrescriptionResponse> findAllPrescriptionsByPetId(Integer petId);
    void deletePrescriptionByPrescriptionId(int id);
    void deletePrescriptionByPetId(int petId);
    PrescriptionResponse updatePrescription(PrescriptionRequest prescription, int id);
}

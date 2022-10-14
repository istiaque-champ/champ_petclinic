package com.team2.prescriptionservice.DataLayer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepo extends CrudRepository<Prescription, Integer> {


        Prescription findPrescriptionByPrescriptionId(Integer prescriptionId);

        Prescription findPrescriptionByMedication(String medicationName);

        boolean existsPrescriptionByPrescriptionId(Integer prescriptionId);

        void deletePrescriptionByPrescriptionId(Integer prescriptionId);

        Prescription findTopByOrderByPrescriptionIdDesc();

}

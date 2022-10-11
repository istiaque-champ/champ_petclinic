package com.team2.prescriptionservice.DataLayer;

import org.springframework.data.repository.CrudRepository;

public interface PrescriptionRepo extends CrudRepository<Prescription, Integer> {

        public Prescription findPrescriptionByPrescriptionId(Integer prescriptionId);

        public Prescription findPrescriptionByMedication(String medicationName);

        public boolean existsPrescriptionByPrescriptionId(Integer prescriptionId);

        public void deletePrescriptionByPrescriptionId(Integer prescriptionId);


}

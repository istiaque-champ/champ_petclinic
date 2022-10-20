package com.team2.prescriptionservice.DataLayer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepo extends CrudRepository<Prescription, Integer> {

        List<Prescription> findPrescriptionsByPetId(Integer petId);
        Prescription findTopByOrderByPrescriptionIdDesc();
        Prescription findPrescriptionByPrescriptionId(Integer prescriptionId);

        boolean existsPrescriptionByPrescriptionId(Integer prescriptionId);
        boolean existsPrescriptionByPetId(int petId);

        void  deletePrescriptionByPrescriptionId(Integer prescriptionId);
        void deletePrescriptionsByPetId(Integer petId);





}

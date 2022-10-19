package com.team2.prescriptionservice.DataLayer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepo extends CrudRepository<Prescription, Integer> {


        Prescription findPrescriptionByPrescriptionId(Integer prescriptionId);
        boolean existsPrescriptionByPrescriptionId(Integer prescriptionId);
        Prescription findTopByOrderByPrescriptionIdDesc();
        Prescription deletePrescriptionById(Integer prescriptionId);
        void  deletePrescriptionByPrescriptionId(Integer prescriptionId);



        List<Prescription> findPrescriptionsByPetId(Integer petId);

}

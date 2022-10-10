package com.team2.prescriptionservice.DataLayer;

import org.springframework.data.repository.CrudRepository;

public interface PrescriptionRepo extends CrudRepository<Prescription, Integer> {
}

package com.team2.prescriptionservice.BusinessLayer;

import com.team2.prescriptionservice.DataLayer.*;
import com.team2.prescriptionservice.Exceptions.DatabaseError;
import com.team2.prescriptionservice.Exceptions.InvalidInputException;
import com.team2.prescriptionservice.Exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionServiceImpl implements PrescriptionService  {
    private final PrescriptionRepo repository;
    private final PrescriptionMapper mapper;

    public PrescriptionServiceImpl(PrescriptionRepo repository,PrescriptionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PrescriptionResponse findByPrescriptionId(int id) {
        try {
            //find prescription by prescriptionId
            if (!repository.existsPrescriptionByPrescriptionId(id)){
                throw new Exception();
            }
            return mapper.entityToResponseModel(repository.findPrescriptionByPrescriptionId(id));
        } catch (Exception e) {
            // if prescription not found
            throw new NotFoundException("prescription with ID: " + id + " was not found");
        }
    }

    @Override
    public List<PrescriptionResponse> findAllPrescriptions() {
        try {
            //find prescription by prescriptionId
            return mapper.entityListToResponseModelList((List<Prescription>) repository.findAll());
        } catch (Exception e) {
            // if prescription not found
            throw new NotFoundException("none found");
        }
    }

    @Override
    public PrescriptionResponse savePrescription(PrescriptionRequest prescriptionRequest){
        try {
            Prescription prescription = mapper.RequestModelToEntity(prescriptionRequest);
            prescription.setPrescriptionId((repository.findTopByOrderByPrescriptionIdDesc().getPrescriptionId())+1);
            prescription.setDatePrinted(Date.valueOf(prescriptionRequest.getDatePrinted()));
            System.out.println(prescription.getDatePrinted());
            return mapper.entityToResponseModel(repository.save(prescription));
        } catch (Exception e) {
            // if it does not work
            throw new DatabaseError("error adding to database");
        }
    }

    @Override
    public List<PrescriptionResponse> findAllPrescriptionsByPetId(Integer petId) {
        return mapper.entityListToResponseModelList(repository.findPrescriptionsByPetId(petId));
    }
}

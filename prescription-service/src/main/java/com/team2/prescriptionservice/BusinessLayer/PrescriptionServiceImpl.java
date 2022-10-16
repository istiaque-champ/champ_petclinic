package com.team2.prescriptionservice.BusinessLayer;

import com.team2.prescriptionservice.DataLayer.*;
import com.team2.prescriptionservice.Exceptions.DatabaseError;
import com.team2.prescriptionservice.Exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

import static com.team2.prescriptionservice.Exceptions.GlobalControllerExceptionHandler.LOG;

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
    @Transactional
    public void deletePrescription(int id) {
        if(repository.existsPrescriptionByPrescriptionId(id)){
            repository.deletePrescriptionById(id);
            return;
        }
        throw new NotFoundException("Unknown prescription provided: " + id);
    }

    @Override
    public PrescriptionResponse updatePrescription(PrescriptionRequest prescription, int id) {
        if(repository.existsPrescriptionByPrescriptionId(id)){
            Prescription inDbPrescription = repository.findPrescriptionByPrescriptionId(id);


            Prescription updatedData = mapper.RequestModelToEntity(prescription);

            inDbPrescription.setMedication(updatedData.getMedication());
            inDbPrescription.setAmount(updatedData.getAmount());
            inDbPrescription.setDatePrinted(updatedData.getDatePrinted());
            inDbPrescription.setInstructions(updatedData.getInstructions());


            return mapper.entityToResponseModel(repository.save(inDbPrescription));
        }

        throw new NotFoundException("Unknown prescription Id provided: " + id);
    }


}

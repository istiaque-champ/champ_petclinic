package com.team2.prescriptionservice.PresentationLayer;

import com.team2.prescriptionservice.BusinessLayer.PrescriptionService;
import com.team2.prescriptionservice.DataLayer.Prescription;
import com.team2.prescriptionservice.DataLayer.PrescriptionRepo;
import com.team2.prescriptionservice.DataLayer.PrescriptionRequest;
import com.team2.prescriptionservice.DataLayer.PrescriptionResponse;
import com.team2.prescriptionservice.Exceptions.InvalidInputException;
import com.team2.prescriptionservice.Utils.date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.Console;
import java.util.List;
import java.util.Optional;

@RequestMapping("{petId}/prescriptions")
@RestController
@Slf4j
public class PrescriptionRessource {

    private final PrescriptionService prescriptionService;

    PrescriptionRessource(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping(value = "/{prescriptionId}")
    public ResponseEntity<PrescriptionResponse> findPrescription(@PathVariable("prescriptionId") int prescriptionId) {
        System.out.println("Try find ");
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionService.findByPrescriptionId(prescriptionId));
    }

    @PostMapping()
    public ResponseEntity<PrescriptionResponse> addPrescription(@RequestBody PrescriptionRequest prescription) {
        try {
            validate(prescription);
            return ResponseEntity.status(HttpStatus.OK).body(prescriptionService.savePrescription(prescription));

        } catch (Exception e) {
            throw new InvalidInputException("Input Error");
        }
    }

    @GetMapping()
    public ResponseEntity<List<PrescriptionResponse>> findPrescriptionsByPetId(@PathVariable Integer petId) {
        System.out.println("Try find all ");
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionService.findAllPrescriptionsByPetId(petId));
    }

    @DeleteMapping()
    public ResponseEntity<?> deletePrescriptionByPetId(@PathVariable int petId) {
        prescriptionService.deletePrescriptionByPetId(petId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/{prescriptionId}")
    public ResponseEntity<?> deletePrescription(@PathVariable int prescriptionId) {
        prescriptionService.deletePrescriptionByPrescriptionId(prescriptionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PutMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResponse> updatePrescription(@RequestBody PrescriptionRequest prescription, @PathVariable int prescriptionId) {
        PrescriptionResponse responseModel = prescriptionService.updatePrescription(prescription, prescriptionId);
        try {
            validate(prescription);
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (Exception e) {
            throw new InvalidInputException("Input Error");
        }

    }

    private void validate(@RequestBody PrescriptionRequest prescription) {
        if (prescription.getAmount() == null || prescription.getPetId() == null || prescription.getDatePrinted() == null || prescription.getInstructions() == null || prescription.getMedication() == null) {
            throw new InvalidInputException("Missing fields");
        }
        if (!date.isValid(prescription.getDatePrinted())) {
            throw new InvalidInputException("Invalid date format");
        }
    }
}

package com.team2.prescriptionservice.PresentationLayer;

import com.team2.prescriptionservice.BusinessLayer.PrescriptionService;
import com.team2.prescriptionservice.DataLayer.Prescription;
import com.team2.prescriptionservice.DataLayer.PrescriptionRepo;
import com.team2.prescriptionservice.DataLayer.PrescriptionRequest;
import com.team2.prescriptionservice.DataLayer.PrescriptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.Console;
import java.util.List;
import java.util.Optional;

@RequestMapping("/prescriptions")
@RestController
@Slf4j
public class PrescriptionRessource {

    private final PrescriptionService prescriptionService;

    PrescriptionRessource(PrescriptionService prescriptionService){this.prescriptionService = prescriptionService;}

    @GetMapping(value = "/{prescriptionId}")
    public PrescriptionResponse findPrescription(@PathVariable("prescriptionId") int prescriptionId) {
        System.out.println("Try find ");
        return prescriptionService.findByPrescriptionId(prescriptionId);
    }

    @GetMapping()
    public List<PrescriptionResponse> findPrescriptions() {
        System.out.println("Try find all ");
        return prescriptionService.findAllPrescriptions();
    }

    @PostMapping()
    public PrescriptionResponse addPrescription(@RequestBody PrescriptionRequest prescription) {
        System.out.println("Try add ");
        return prescriptionService.savePrescription(prescription);
    }



}
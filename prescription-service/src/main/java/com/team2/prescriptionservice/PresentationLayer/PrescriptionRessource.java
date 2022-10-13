package com.team2.prescriptionservice.PresentationLayer;

import com.team2.prescriptionservice.BusinessLayer.PrescriptionService;
import com.team2.prescriptionservice.DataLayer.Prescription;
import com.team2.prescriptionservice.DataLayer.PrescriptionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Console;
import java.util.Optional;

@RequestMapping("/prescriptions")
@RestController
@Slf4j
public class PrescriptionRessource {

    private final PrescriptionService prescriptionService;

    PrescriptionRessource(PrescriptionService prescriptionService){this.prescriptionService = prescriptionService;}

    @GetMapping(value = "/{prescriptionId}")
    public Prescription findPrescription(@PathVariable("prescriptionId") int prescriptionId) {
        System.out.println("Try find ");
        return prescriptionService.findByPrescriptionId(prescriptionId);
    }

}

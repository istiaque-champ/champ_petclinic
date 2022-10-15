package com.team2.prescriptionservice.DataLayer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
class PrescriptionRepoTest {


    @Autowired
    PrescriptionRepo prescriptionRepo;

    public static final Integer VALID_PRESCRIPTION_ID = 2001;
    public static final String VALID_MEDICATION = "Amitraz";

    Prescription savedPrescription;


    @BeforeEach
    void setUp() {
        prescriptionRepo.deleteAll();


        Calendar cal = Calendar.getInstance();


        Prescription prescription = new Prescription();
        prescription.setPrescriptionId(VALID_PRESCRIPTION_ID);
        prescription.setMedication(VALID_MEDICATION);
        prescription.setAmount("30 Tabs");
        prescription.setDatePrinted(cal.getTime());
        prescription.setInstructions("Mix with food");

        savedPrescription = prescriptionRepo.save(prescription);

        Assertions.assertAll(
                () -> assertEquals(savedPrescription.getPrescriptionId(), VALID_PRESCRIPTION_ID),
                () -> assertEquals(savedPrescription.getMedication(), VALID_MEDICATION)
        );


    }

    @Test
    void whenValidPrescriptionId_ThenPrescriptionShouldBeFound(){

        Prescription foundEntity;

        foundEntity = prescriptionRepo.findPrescriptionByPrescriptionId(VALID_PRESCRIPTION_ID);

        Assertions.assertAll(
                () -> assertEquals(foundEntity.getPrescriptionId(), savedPrescription.getPrescriptionId()),
                () -> assertNotNull(savedPrescription)
        );

    }


    @Test
    void whenPrescriptionIdExists_ThenAssertTrue(){

        Boolean isFound;

        isFound = prescriptionRepo.existsPrescriptionByPrescriptionId(VALID_PRESCRIPTION_ID);

        Assertions.assertEquals(true, isFound);


    }











}
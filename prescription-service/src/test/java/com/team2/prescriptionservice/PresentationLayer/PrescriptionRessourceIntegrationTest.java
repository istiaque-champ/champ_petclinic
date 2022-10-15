package com.team2.prescriptionservice.PresentationLayer;

import com.team2.prescriptionservice.DataLayer.Prescription;
import com.team2.prescriptionservice.DataLayer.PrescriptionRepo;
import com.team2.prescriptionservice.DataLayer.PrescriptionRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class PrescriptionRessourceIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    PrescriptionRepo repository;

    private final String ENDPOINT = "/prescriptions";

    private final Integer VALID_PRESCRIPTION_ID = 1001;
    private final String VALID_MEDICATION = "Amoxicillin";
    private final String VALID_AMOUNT = "30 Tabs";
    private final Date VALID_DATE_PRINTED =  new java.util.Date();
    private final String VALID_INSTRUCTIONS = "Mix with their food";

    private final Integer VALID_PRESCRIPTION_ID_2 = 1002;
    private final String VALID_MEDICATION_2 = "Advil";
    private final String VALID_AMOUNT_2 = "20 Pills";
    private final String VALID_INSTRUCTIONS_2 = "Mix with their water";


    void createPrescriptions(){
        Prescription firstPrescription = new Prescription();
        firstPrescription.setPrescriptionId(VALID_PRESCRIPTION_ID);
        firstPrescription.setAmount(VALID_AMOUNT);
        firstPrescription.setDatePrinted(VALID_DATE_PRINTED);
        firstPrescription.setInstructions(VALID_INSTRUCTIONS);
        firstPrescription.setMedication(VALID_MEDICATION);

        repository.save(firstPrescription);

        Prescription secondPrescription = new Prescription();
        secondPrescription.setPrescriptionId(VALID_PRESCRIPTION_ID_2);
        secondPrescription.setAmount(VALID_AMOUNT_2);
        secondPrescription.setDatePrinted(VALID_DATE_PRINTED);
        secondPrescription.setInstructions(VALID_INSTRUCTIONS_2);
        secondPrescription.setMedication(VALID_MEDICATION_2);

        repository.save(secondPrescription);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void GetPrescriptionByPrescriptionIdTest() {

        createPrescriptions();

        webTestClient.get()
                .uri(ENDPOINT + "/" + VALID_PRESCRIPTION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.prescriptionId").isEqualTo(VALID_PRESCRIPTION_ID)
                .jsonPath("$.medication").isEqualTo(VALID_MEDICATION)
                .jsonPath("$.amount").isEqualTo(VALID_AMOUNT)
                .jsonPath("$.datePrinted").isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(VALID_DATE_PRINTED))
                .jsonPath("$.instructions").isEqualTo(VALID_INSTRUCTIONS);
    }

    @Test
    void GetAllPrescriptionsTest() {

        createPrescriptions();

        webTestClient.get()
                .uri(ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].prescriptionId").isEqualTo(VALID_PRESCRIPTION_ID)
                .jsonPath("$[0].medication").isEqualTo(VALID_MEDICATION)
                .jsonPath("$[0].amount").isEqualTo(VALID_AMOUNT)
                .jsonPath("$[0].datePrinted").isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(VALID_DATE_PRINTED))
                .jsonPath("$[0].instructions").isEqualTo(VALID_INSTRUCTIONS)
                .jsonPath("$[1].prescriptionId").isEqualTo(VALID_PRESCRIPTION_ID_2)
                .jsonPath("$[1].medication").isEqualTo(VALID_MEDICATION_2)
                .jsonPath("$[1].amount").isEqualTo(VALID_AMOUNT_2)
                .jsonPath("$[1].datePrinted").isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(VALID_DATE_PRINTED))
                .jsonPath("$[1].instructions").isEqualTo(VALID_INSTRUCTIONS_2);
    }

//    @Test
//    void PostPrescriptionTest() {
//
//        PrescriptionRequest request = new PrescriptionRequest();
//        request.setAmount(VALID_AMOUNT);
//        request.setInstructions(VALID_INSTRUCTIONS);
//        request.setMedication(VALID_MEDICATION);
//
//        webTestClient.post()
//                .uri(ENDPOINT)
//                .body(BodyInserters.fromValue(request))
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                .expectBody()
//                .jsonPath("$.prescriptionId").isEqualTo(VALID_PRESCRIPTION_ID)
//                .jsonPath("$.medication").isEqualTo(VALID_MEDICATION)
//                .jsonPath("$.amount").isEqualTo(VALID_AMOUNT)
//                .jsonPath("$.datePrinted").isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(VALID_DATE_PRINTED))
//                .jsonPath("$.instructions").isEqualTo(VALID_INSTRUCTIONS);
//    }
}
package com.team2.prescriptionservice.PresentationLayer;

import com.team2.prescriptionservice.DataLayer.Prescription;
import com.team2.prescriptionservice.DataLayer.PrescriptionRepo;
import com.team2.prescriptionservice.DataLayer.PrescriptionRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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

    private final String ENDPOINT = "1/prescriptions";

    private final Integer VALID_PRESCRIPTION_ID = 1001;
    private final Integer VALID_PET_ID = 1;
    private final String VALID_MEDICATION = "Amoxicillin";
    private final String VALID_AMOUNT = "30 Tabs";
    private final Date VALID_DATE_PRINTED =  new java.util.Date();
    private final String VALID_DATE_PRINTED_REQUEST = new SimpleDateFormat("yyyy-MM-dd").format(VALID_DATE_PRINTED);
    private final String VALID_INSTRUCTIONS = "Mix with their food";

    private final Integer VALID_PRESCRIPTION_ID_2 = 1002;
    private final Integer VALID_PET_ID_2 = 2;
    private final String VALID_MEDICATION_2 = "Advil";
    private final String VALID_AMOUNT_2 = "20 Pills";
    private final String VALID_INSTRUCTIONS_2 = "Mix with their water";

    private final Integer INVALID_PRESCRIPTION_ID = 99999;
    private final String INVALID_DATE_PRINTED_REQUEST = "2022.09.08";


    void createPrescriptions(){
        Prescription firstPrescription = new Prescription();
        firstPrescription.setPrescriptionId(VALID_PRESCRIPTION_ID);
        firstPrescription.setPetId(VALID_PET_ID);
        firstPrescription.setAmount(VALID_AMOUNT);
        firstPrescription.setDatePrinted(VALID_DATE_PRINTED);
        firstPrescription.setInstructions(VALID_INSTRUCTIONS);
        firstPrescription.setMedication(VALID_MEDICATION);

        repository.save(firstPrescription);

        Prescription secondPrescription = new Prescription();
        secondPrescription.setPrescriptionId(VALID_PRESCRIPTION_ID_2);
        secondPrescription.setPetId(VALID_PET_ID);
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

    @BeforeEach
    void create(){createPrescriptions();}

    @Test
    void GetPrescriptionByPrescriptionIdTest() {


        webTestClient.get()
                .uri(ENDPOINT + "/" + VALID_PRESCRIPTION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.prescriptionId").isEqualTo(VALID_PRESCRIPTION_ID)
                .jsonPath("$.petId").isEqualTo(VALID_PET_ID)
                .jsonPath("$.medication").isEqualTo(VALID_MEDICATION)
                .jsonPath("$.amount").isEqualTo(VALID_AMOUNT)
                .jsonPath("$.datePrinted").isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(VALID_DATE_PRINTED))
                .jsonPath("$.instructions").isEqualTo(VALID_INSTRUCTIONS);
    }

    @Test
    void GetAllPrescriptionsTest() {


        webTestClient.get()
                .uri(ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].prescriptionId").isEqualTo(VALID_PRESCRIPTION_ID)
                .jsonPath("$[0].petId").isEqualTo(VALID_PET_ID)
                .jsonPath("$[0].medication").isEqualTo(VALID_MEDICATION)
                .jsonPath("$[0].amount").isEqualTo(VALID_AMOUNT)
                .jsonPath("$[0].datePrinted").isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(VALID_DATE_PRINTED))
                .jsonPath("$[0].instructions").isEqualTo(VALID_INSTRUCTIONS)
                .jsonPath("$[1].prescriptionId").isEqualTo(VALID_PRESCRIPTION_ID_2)
                .jsonPath("$[1].petId").isEqualTo(VALID_PET_ID)
                .jsonPath("$[1].medication").isEqualTo(VALID_MEDICATION_2)
                .jsonPath("$[1].amount").isEqualTo(VALID_AMOUNT_2)
                .jsonPath("$[1].datePrinted").isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(VALID_DATE_PRINTED))
                .jsonPath("$[1].instructions").isEqualTo(VALID_INSTRUCTIONS_2);
    }

    @Test
    void PostPrescriptionTest() {

        PrescriptionRequest request = new PrescriptionRequest();
        request.setAmount(VALID_AMOUNT);
        request.setDatePrinted(VALID_DATE_PRINTED_REQUEST);
        request.setInstructions(VALID_INSTRUCTIONS);
        request.setMedication(VALID_MEDICATION);
        request.setPetId(VALID_PET_ID);


        System.out.println(BodyInserters.fromValue(request));

        webTestClient.post()
                .uri(ENDPOINT)
                .body(BodyInserters.fromValue(request))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.prescriptionId").isNotEmpty()
                .jsonPath("$.medication").isEqualTo(VALID_MEDICATION)
                .jsonPath("$.amount").isEqualTo(VALID_AMOUNT)
                .jsonPath("$.datePrinted").isEqualTo(VALID_DATE_PRINTED_REQUEST)
                .jsonPath("$.instructions").isEqualTo(VALID_INSTRUCTIONS);
    }

    @Test
    void PutPrescriptionTest() {

        PrescriptionRequest request = new PrescriptionRequest();
        request.setAmount(VALID_AMOUNT_2);
        request.setDatePrinted(VALID_DATE_PRINTED_REQUEST);
        request.setInstructions(VALID_INSTRUCTIONS_2);
        request.setMedication(VALID_MEDICATION_2);
        request.setPetId(VALID_PET_ID);


        System.out.println(BodyInserters.fromValue(request));

        webTestClient.put()
                .uri(ENDPOINT+"/"+VALID_PRESCRIPTION_ID)
                .body(BodyInserters.fromValue(request))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.prescriptionId").isEqualTo(VALID_PRESCRIPTION_ID)
                .jsonPath("$.medication").isEqualTo(VALID_MEDICATION_2)
                .jsonPath("$.amount").isEqualTo(VALID_AMOUNT_2)
                .jsonPath("$.datePrinted").isEqualTo(VALID_DATE_PRINTED_REQUEST)
                .jsonPath("$.instructions").isEqualTo(VALID_INSTRUCTIONS_2)
                .jsonPath("$.petId").isEqualTo(VALID_PET_ID);
    }

    @Test
    void deleteByPrescriptionIdTest() {

        webTestClient.delete()
                .uri(ENDPOINT+"/"+VALID_PRESCRIPTION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }
    @Test
    void deleteByPetIdTest() {

        webTestClient.delete()
                .uri(VALID_PET_ID+"/prescriptions")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void IfNoPrescriptionIdFoundOnGetThrow404(){
        webTestClient.get()
                .uri(ENDPOINT + "/" + INVALID_PRESCRIPTION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void IfNoPrescriptionIdFoundOnDeleteByPrescriptionIdThrow404(){
        webTestClient.delete()
                .uri(ENDPOINT + "/" + INVALID_PRESCRIPTION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void IfNoPrescriptionIdFoundOnDeleteByPetIdThrow404(){
        webTestClient.delete()
                .uri(ENDPOINT + "/" + INVALID_PRESCRIPTION_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void IfNoPrescriptionIdFoundOnPutThrow404(){

        PrescriptionRequest request = new PrescriptionRequest();
        request.setAmount(VALID_AMOUNT_2);
        request.setDatePrinted(VALID_DATE_PRINTED_REQUEST);
        request.setInstructions(VALID_INSTRUCTIONS_2);
        request.setMedication(VALID_MEDICATION_2);
        request.setPetId(VALID_PET_ID);

        webTestClient.put()
                .uri(ENDPOINT + "/" + INVALID_PRESCRIPTION_ID)
                .body(BodyInserters.fromValue(request))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void IfMissingFieldsThrow422(){

        PrescriptionRequest request = new PrescriptionRequest();
        request.setAmount(VALID_AMOUNT);
        request.setDatePrinted(VALID_DATE_PRINTED_REQUEST);
        request.setInstructions(VALID_INSTRUCTIONS);
        request.setPetId(VALID_PET_ID);

        webTestClient.post()
                .uri(ENDPOINT)
                .body(BodyInserters.fromValue(request))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void IfInvalidOnPostDateFormatThrow422(){

        PrescriptionRequest request = new PrescriptionRequest();
        request.setAmount(VALID_AMOUNT);
        request.setDatePrinted(INVALID_DATE_PRINTED_REQUEST);
        request.setInstructions(VALID_INSTRUCTIONS);
        request.setMedication(VALID_MEDICATION);
        request.setPetId(VALID_PET_ID);

        webTestClient.post()
                .uri(ENDPOINT)
                .body(BodyInserters.fromValue(request))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
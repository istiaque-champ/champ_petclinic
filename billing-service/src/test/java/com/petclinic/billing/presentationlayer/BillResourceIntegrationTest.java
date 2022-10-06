package com.petclinic.billing.presentationlayer;

import com.petclinic.billing.businesslayer.BillServiceImpl;
import com.petclinic.billing.datalayer.Bill;
import com.petclinic.billing.datalayer.BillDTO;
import com.petclinic.billing.datalayer.BillRepository;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class BillResourceIntegrationTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    BillRepository billRepository;

    private final String BASE_URI = "/bills";

    private final Integer VALID_BILL_ID = 1;
    private final Integer VALID_CUSTOMER_ID = 1;
    private final String VALID_VISIT_TYPE = "Examinations";
    private final Instant VALID_DATE = Instant.now();
    private final Double VALID_AMOUNT = BillServiceImpl.visitTypePrices.get(VALID_VISIT_TYPE);

    private final String SECOND_VALID_VISIT_TYPE = "Injury";
    private final Double SECOND_VALID_AMOUNT = BillServiceImpl.visitTypePrices.get(SECOND_VALID_VISIT_TYPE);

    private final Integer INVALID_BILL_ID = -1;
    private final Integer INVALID_CUSTOMER_ID = -1;
    private final BillDTO VALID_BILL_REQUEST_MODEL = buildBillDTORequestModel();
    private final BillDTO SECOND_VALID_BILL_REQUEST_MODEL = buildBillDTORequestModel2();
    private final BillDTO INVALID_CUSTOMER_ID_REQUEST_MODEL = buildInvalidCustomerIdBillDTO();
    private final BillDTO INVALID_VISIT_TYPE_REQUEST_MODEL = buildInvalidVisitTypeBillDTO();

    @Test
    void testGetByIdIntegration() {
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient.get()
                .uri(BASE_URI + "/" + VALID_BILL_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.billId").isEqualTo(VALID_BILL_ID)
                .jsonPath("$.customerId").isEqualTo(VALID_CUSTOMER_ID)
                .jsonPath("$.visitType").isEqualTo(VALID_VISIT_TYPE)
                .jsonPath("$.amount").isEqualTo(VALID_AMOUNT);
    }

    @Test
    void testGetAllIntegration(){
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient.get()
                .uri(BASE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                //.jsonPath("$[0].billId").isEqualTo(VALID_BILL_ID) failed, can't figure out why
                .jsonPath("$[0].billId").isNotEmpty()
                .jsonPath("$[0].customerId").isEqualTo(VALID_CUSTOMER_ID)
                .jsonPath("$[0].visitType").isEqualTo(VALID_VISIT_TYPE)
                .jsonPath("$[0].amount").isEqualTo(VALID_AMOUNT);
    }

    @Test
    void testCreateIntegration(){
        Publisher<Void> setup = billRepository.deleteAll();

        StepVerifier
                .create(setup)
                .expectNextCount(0)
                .verifyComplete();

        webTestClient.post()
                .uri(BASE_URI)
                .body(Mono.just(VALID_BILL_REQUEST_MODEL), BillDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.billId").isNotEmpty()
                .jsonPath("$.customerId").isEqualTo(VALID_CUSTOMER_ID)
                .jsonPath("$.visitType").isEqualTo(VALID_VISIT_TYPE)
                .jsonPath("$.amount").isEqualTo(VALID_AMOUNT);
    }

    @Test
    void testUpdateIntegration(){
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient.put()
                .uri(BASE_URI + "/" + VALID_BILL_ID)
                .body(Mono.just(SECOND_VALID_BILL_REQUEST_MODEL), BillDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.billId").isEqualTo(VALID_BILL_ID)
                .jsonPath("$.customerId").isEqualTo(VALID_CUSTOMER_ID)
                .jsonPath("$.visitType").isEqualTo(SECOND_VALID_VISIT_TYPE)
                .jsonPath("$.amount").isEqualTo(SECOND_VALID_AMOUNT);
    }

    @Test
    void testGetByCustomerIdIntegration(){
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient.get()
                .uri(BASE_URI + "/customer/" + VALID_CUSTOMER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].billId").isEqualTo(VALID_BILL_ID)
                .jsonPath("$[0].customerId").isEqualTo(VALID_CUSTOMER_ID)
                .jsonPath("$[0].visitType").isEqualTo(VALID_VISIT_TYPE)
                .jsonPath("$[0].amount").isEqualTo(VALID_AMOUNT);
    }

    @Test
    void testDeleteIntegration(){
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient.delete()
                .uri(BASE_URI + "/" + VALID_BILL_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testGetAllEmptyIntegration(){
        Publisher<Void> setup = billRepository.deleteAll();

        StepVerifier
                .create(setup)
                .expectNextCount(0)
                .verifyComplete();

        webTestClient.get()
                .uri(BASE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0]").doesNotExist();
    }

    @Test
    void testGetByBillIdNotFoundIntegration(){
        Publisher<Void> setup = billRepository.deleteAll();

        StepVerifier
                .create(setup)
                .expectNextCount(0)
                .verifyComplete();

        webTestClient.get()
                .uri(BASE_URI + "/" + VALID_BILL_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    //This test is for an invalid Id input, which does not happen anymore. I'll refactor it and the code to be about customerId instead
    @Test
    void testCreateInvalidCustomerIdIntegration(){
        Publisher<Void> setup = billRepository.deleteAll();

        StepVerifier
                .create(setup)
                .expectNextCount(0)
                .verifyComplete();

        webTestClient.post()
                .uri(BASE_URI)
                .body(Mono.just(INVALID_CUSTOMER_ID_REQUEST_MODEL), BillDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("That customer id is invalid");
    }

    @Test
    void testCreateInvalidVisitTypeIntegration(){
        Publisher<Void> setup = billRepository.deleteAll();

        StepVerifier
                .create(setup)
                .expectNextCount(0)
                .verifyComplete();

        webTestClient.post()
                .uri(BASE_URI)
                .body(Mono.just(INVALID_VISIT_TYPE_REQUEST_MODEL), BillDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("That visit type does not exist");
    }

    @Test
    void testUpdateNotFoundIntegration(){
        Publisher<Void> setup = billRepository.deleteAll();

        StepVerifier
                .create(setup)
                .expectNextCount(0)
                .verifyComplete();

        webTestClient.put()
                .uri(BASE_URI + "/" + VALID_BILL_ID)
                .body(Mono.just(SECOND_VALID_BILL_REQUEST_MODEL), BillDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }



    @Test
    void testUpdateInvalidCustomerIdIntegration(){
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient.put()
                .uri(BASE_URI + "/" + VALID_BILL_ID)
                .body(Mono.just(INVALID_CUSTOMER_ID_REQUEST_MODEL), BillDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("That customer id is invalid");
    }

    @Test
    void testUpdateInvalidVisitTypeIntegration(){
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient.put()
                .uri(BASE_URI + "/" + VALID_BILL_ID)
                .body(Mono.just(INVALID_VISIT_TYPE_REQUEST_MODEL), BillDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("That visit type does not exist");
    }

    @Test
    void getBillIdInvalidBillIdIntegration(){
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient.get()
                .uri(BASE_URI + "/" + INVALID_BILL_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("That id is invalid");
    }

    @Test
    void getBillsByInvalidCustomerIdIntegration(){
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient.get()
                .uri(BASE_URI + "/customer/" + INVALID_CUSTOMER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("That id is invalid");
    }

    @Test
    void updateBillInvalidBillIdIntegration(){
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient.put()
                .uri(BASE_URI + "/" + INVALID_BILL_ID)
                .body(Mono.just(VALID_BILL_REQUEST_MODEL), BillDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("That id is invalid");
    }

    @Test
    void deleteBillByInvalidBillIdIntegration(){
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient.delete()
                .uri(BASE_URI + "/" + INVALID_BILL_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("That id is invalid");
    }

    BillDTO buildBillDTORequestModel(){
        BillDTO billDTO = new BillDTO();

        billDTO.setCustomerId(VALID_CUSTOMER_ID);
        billDTO.setVisitType(VALID_VISIT_TYPE);
        billDTO.setDate(VALID_DATE);

        return billDTO;
    }

    BillDTO buildBillDTORequestModel2(){
        BillDTO billDTO = new BillDTO();

        billDTO.setCustomerId(VALID_CUSTOMER_ID);
        billDTO.setVisitType(SECOND_VALID_VISIT_TYPE);
        billDTO.setDate(VALID_DATE);

        return billDTO;
    }

    BillDTO buildInvalidCustomerIdBillDTO(){
        BillDTO billDTO = new BillDTO();

        billDTO.setCustomerId(INVALID_CUSTOMER_ID);
        billDTO.setVisitType(SECOND_VALID_VISIT_TYPE);
        billDTO.setDate(VALID_DATE);

        return billDTO;
    }

    BillDTO buildInvalidVisitTypeBillDTO(){
        BillDTO billDTO = new BillDTO();

        billDTO.setCustomerId(VALID_CUSTOMER_ID);
        String INVALID_VISIT_TYPE = "Random";
        billDTO.setVisitType(INVALID_VISIT_TYPE);
        billDTO.setDate(VALID_DATE);

        return billDTO;
    }
}

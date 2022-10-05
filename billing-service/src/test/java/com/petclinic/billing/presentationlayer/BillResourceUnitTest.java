package com.petclinic.billing.presentationlayer;

import com.petclinic.billing.businesslayer.BillService;
import com.petclinic.billing.businesslayer.BillServiceImpl;
import com.petclinic.billing.datalayer.BillDTO;
import com.petclinic.billing.http.HttpErrorInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class BillResourceUnitTest {

    @MockBean
    BillService service;

    @Autowired
    WebTestClient webTestClient;

    private final String BASE_URI = "/bills";

    private final Integer VALID_BILL_ID = 1;
    private final Integer VALID_CUSTOMER_ID = 1;
    private final String VALID_VISIT_TYPE = "Examinations";
    private final Instant VALID_DATE = Instant.now();
    private final Double VALID_AMOUNT = BillServiceImpl.visitTypePrices.get(VALID_VISIT_TYPE);

    private final Integer NOT_FOUND_BILL_ID = 2;

    private final Integer INVALID_BILL_ID = -1;

    private final BillDTO VALID_BILL_RESPONSE_MODEL = buildBillDTOResponseModel();
    private final BillDTO VALID_BILL_REQUEST_MODEL = buildBillDTORequestModel();

    @Test
    void findBillUnitTest() {
        when(service.GetBill(VALID_BILL_ID)).thenReturn(Mono.just(VALID_BILL_RESPONSE_MODEL));

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
    void deleteBillUnitTest() {
        when(service.DeleteBill(VALID_BILL_ID)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(BASE_URI + "/" + VALID_BILL_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void createBillUnitTest() {
        when(service.CreateBill(any(Mono.class))).thenReturn(Mono.just(VALID_BILL_RESPONSE_MODEL));

        webTestClient.post()
                .uri(BASE_URI)
                .body(Mono.just(VALID_BILL_REQUEST_MODEL), BillDTO.class)
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
    void getBillByCustomerIdUnitTest() {
        when(service.GetBillByCustomerId(VALID_CUSTOMER_ID)).thenReturn(Flux.just(VALID_BILL_RESPONSE_MODEL));

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
    void updateBillUnitTest() {
        when(service.UpdateBill(any(Mono.class), anyInt())).thenReturn(Mono.just(VALID_BILL_RESPONSE_MODEL));

        webTestClient.put()
                .uri(BASE_URI + "/" + VALID_BILL_ID)
                .body(Mono.just(VALID_BILL_REQUEST_MODEL), BillDTO.class)
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
    void findAllBillsUnitTest() {
        when(service.GetAllBills()).thenReturn(Flux.just(VALID_BILL_RESPONSE_MODEL));

        webTestClient.get()
                .uri(BASE_URI)
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
    void testGetNotFoundUnitTest()  {
        when(service.GetBill(NOT_FOUND_BILL_ID)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri(BASE_URI + "/" + NOT_FOUND_BILL_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    //Doing a get on a flux (getAll, getByCustomerId) should return an empty flux and not a notFound
    @Test
    void testEmptyGetAllUnitTest(){
        when(service.GetAllBills()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri(BASE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0]").doesNotExist();
    }

    @Test
    void testEmptyCustomerIdUnitTest(){
        int NOT_FOUND_CUSTOMER_ID = 2;
        when(service.GetBillByCustomerId(NOT_FOUND_CUSTOMER_ID)).thenReturn(Flux.empty());

        webTestClient.get()
                .uri(BASE_URI + "/customer/" + NOT_FOUND_CUSTOMER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0]").doesNotExist();
    }

    @Test
    void testUpdateNotFoundUnitTest() {
        when(service.UpdateBill(any(Mono.class), anyInt())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri(BASE_URI + "/" + NOT_FOUND_BILL_ID)
                .body(Mono.just(VALID_BILL_REQUEST_MODEL), BillDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    //I don't get this test but it passes and it's from last year
    @Test
    void testEmptyHttpErrorInfo() {
        HttpErrorInfo httpErrorInfo = new HttpErrorInfo();

        assertNull(httpErrorInfo.getHttpStatus());
        assertNull(httpErrorInfo.getPath());
        assertNull(httpErrorInfo.getTimestamp());
        assertNull(httpErrorInfo.getMessage());
    }

    //I don't get this test but it passes and it's from last year
    @Test
    void testHttpErrorInfoConstructor() {
        HttpErrorInfo httpErrorInfo = new HttpErrorInfo("timestamp1", HttpStatus.NOT_FOUND, "Bill not found");

        assertEquals(httpErrorInfo.getHttpStatus(),  HttpStatus.NOT_FOUND);
        assertEquals(httpErrorInfo.getPath(), "timestamp1");
        assertNotNull(httpErrorInfo.getTimestamp());
        assertEquals(httpErrorInfo.getMessage(), "Bill not found");
    }

    @Test
    void getBillIdInvalidBillIdUnit(){

        webTestClient.get()
                .uri(BASE_URI + "/" + INVALID_BILL_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("That id is invalid");
    }

    @Test
    void getBillsByInvalidCustomerIdUnit(){

        int INVALID_CUSTOMER_ID = -1;
        webTestClient.get()
                .uri(BASE_URI + "/customer/" + INVALID_CUSTOMER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("That id is invalid");
    }

    @Test
    void updateBillInvalidBillIdUnit(){

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
    void deleteBillByInvalidBillIdUnit(){

        webTestClient.delete()
                .uri(BASE_URI + "/" + INVALID_BILL_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("That id is invalid");
    }

    BillDTO buildBillDTOResponseModel(){
        BillDTO billDTO = new BillDTO();

        billDTO.setBillId(VALID_BILL_ID);
        billDTO.setCustomerId(VALID_CUSTOMER_ID);
        billDTO.setVisitType(VALID_VISIT_TYPE);
        billDTO.setAmount(VALID_AMOUNT);
        billDTO.setDate(VALID_DATE);

        return billDTO;
    }

    BillDTO buildBillDTORequestModel(){
        BillDTO billDTO = new BillDTO();

        billDTO.setCustomerId(VALID_CUSTOMER_ID);
        billDTO.setVisitType(VALID_VISIT_TYPE);
        billDTO.setAmount(VALID_AMOUNT);
        billDTO.setDate(VALID_DATE);

        return billDTO;
    }

}
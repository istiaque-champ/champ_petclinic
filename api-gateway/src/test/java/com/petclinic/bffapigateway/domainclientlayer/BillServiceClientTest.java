package com.petclinic.bffapigateway.domainclientlayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petclinic.bffapigateway.dtos.BillDetails;
import com.petclinic.bffapigateway.dtos.BillDetailsExpanded;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BillServiceClientTest {

    private BillServiceClient billServiceClient;

    private MockWebServer server;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        server = new MockWebServer();
        billServiceClient = new BillServiceClient(
                WebClient.builder(),
                "bills-service",
                "7000"
        );
        billServiceClient.setBillServiceUrl(server.url("/").toString());
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void shutdown() throws IOException {
        this.server.shutdown();
    }

    @Test
    void getBilling() {
        prepareResponse(response -> response
                .setHeader("Content-Type", "application/json")
                .setBody("{\n" +
                        "        \"billId\": 63701,\n" +
                        "        \"date\": \"2022-10-09T03:29:52.992+00:00\",\n" +
                        "        \"customerId\": 1,\n" +
                        "        \"vetId\": 2,\n" +
                        "        \"petId\": 2,\n" +
                        "        \"visitType\": \"Examinations\",\n" +
                        "        \"amount\": 59.99\n" +
                        "    }"));

        Mono<BillDetailsExpanded> billDetailsMono = billServiceClient.getBilling(63701);

        assertEquals(63701, billDetailsMono.block().getBillId());
    }

    @Test
    void getAllBilling() {
        prepareResponse(response -> response
            .setHeader("Content-Type", "application/json")
            .setBody("[{\n" +
                    "        \"billId\": 63701,\n" +
                    "        \"date\": \"2022-10-09T03:29:52.992+00:00\",\n" +
                    "        \"customerId\": 1,\n" +
                    "        \"vetId\": 2,\n" +
                    "        \"petId\": 2,\n" +
                    "        \"visitType\": \"Examinations\",\n" +
                    "        \"amount\": 59.99\n" +
                    "    }]"));

        Flux<BillDetailsExpanded> billDetailsFlux = billServiceClient.getAllBilling();

        assertEquals(63701, billDetailsFlux.blockFirst().getBillId());
    }

    @Test
    void createBill() throws JsonProcessingException {
        BillDetails entity = new BillDetails();

        entity.setBillId(1);

        entity.setAmount(599);

        entity.setCustomerId(2);

        entity.setVetId(1);
        entity.setPetId(1);

        final String body = objectMapper.writeValueAsString(objectMapper.convertValue(entity, BillDetails.class));

        prepareResponse(response -> response
                .setHeader("Content-Type", "application/json")
                .setBody(body));

        Mono<BillDetailsExpanded> billDetailsMono = billServiceClient.createBill(entity);

        assertEquals(1, billDetailsMono.block().getBillId());
    }

    @Test
    void getBillsByVetId() {
        prepareResponse(response -> response
                .setHeader("Content-Type", "application/json")
                .setBody("[{\n" +
                        "        \"billId\": 63701,\n" +
                        "        \"date\": \"2022-10-09T03:29:52.992+00:00\",\n" +
                        "        \"customerId\": 1,\n" +
                        "        \"vetId\": 2,\n" +
                        "        \"petId\": 2,\n" +
                        "        \"visitType\": \"Examinations\",\n" +
                        "        \"amount\": 59.99\n" +
                        "    }]"));

        Flux<BillDetailsExpanded> billDetailsFlux = billServiceClient.getBillsByVetId(2);

        assertEquals(63701, billDetailsFlux.blockFirst().getBillId());
    }

    @Test
    void getBillsByPetId() {
        prepareResponse(response -> response
                .setHeader("Content-Type", "application/json")
                .setBody("[{\n" +
                        "        \"billId\": 63701,\n" +
                        "        \"date\": \"2022-10-09T03:29:52.992+00:00\",\n" +
                        "        \"customerId\": 1,\n" +
                        "        \"vetId\": 2,\n" +
                        "        \"petId\": 2,\n" +
                        "        \"visitType\": \"Examinations\",\n" +
                        "        \"amount\": 59.99\n" +
                        "    }]"));

        Flux<BillDetailsExpanded> billDetailsFlux = billServiceClient.getBillsByPetId(2);

        assertEquals(63701, billDetailsFlux.blockFirst().getBillId());
    }

    @Test
    void getBillsByCustomerId() {
        prepareResponse(response -> response
                .setHeader("Content-Type", "application/json")
                .setBody("[{\n" +
                        "        \"billId\": 63701,\n" +
                        "        \"date\": \"2022-10-09T03:29:52.992+00:00\",\n" +
                        "        \"customerId\": 1,\n" +
                        "        \"vetId\": 2,\n" +
                        "        \"petId\": 2,\n" +
                        "        \"visitType\": \"Examinations\",\n" +
                        "        \"amount\": 59.99\n" +
                        "    }]"));

        Flux<BillDetailsExpanded> billDetailsFlux = billServiceClient.getBillsByCustomerId(1);

        assertEquals(63701, billDetailsFlux.blockFirst().getBillId());
    }

    @Test
    void deleteBill() {
        prepareResponse(response -> response
            .setHeader("Content-Type", "application/json")
            .setBody("{\n" +
                    "        \"billId\": 63701,\n" +
                    "        \"date\": \"2022-10-09T03:29:52.992+00:00\",\n" +
                    "        \"customerId\": 1,\n" +
                    "        \"vetId\": 2,\n" +
                    "        \"petId\": 2,\n" +
                    "        \"visitType\": \"Examinations\",\n" +
                    "        \"amount\": 59.99\n" +
                    "    }"));

        Mono<Void> empty = billServiceClient.deleteBill(1);
        assertEquals(empty.block(), null);
    }

    private void prepareResponse(Consumer<MockResponse> consumer) {
        MockResponse response = new MockResponse();
        consumer.accept(response);
        this.server.enqueue(response);
    }

}
package com.petclinic.bffapigateway.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petclinic.bffapigateway.dtos.BillDetails;
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

        Mono<BillDetails> billDetailsMono = billServiceClient.getBilling(63701);

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

        Flux<BillDetails> billDetailsFlux = billServiceClient.getAllBilling();

        assertEquals(63701, billDetailsFlux.blockFirst().getBillId());
    }

    @Test
    void createBill() {
    }

    @Test
    void getBillsByVetId() {
    }

    @Test
    void getBillsByPetId() {
    }

    @Test
    void getBillsByCustomerId() {
    }

    @Test
    void deleteBill() {
    }

    private void prepareResponse(Consumer<MockResponse> consumer) {
        MockResponse response = new MockResponse();
        consumer.accept(response);
        this.server.enqueue(response);
    }

}
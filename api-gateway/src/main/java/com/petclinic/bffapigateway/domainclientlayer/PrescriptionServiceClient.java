package com.petclinic.bffapigateway.domainclientlayer;

import com.petclinic.bffapigateway.dtos.OwnerDetails;
import com.petclinic.bffapigateway.dtos.PrescriptionDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PrescriptionServiceClient {

    private final WebClient.Builder webClientBuilder;
    private final String prescriptionServiceUrl;

    public PrescriptionServiceClient(
            WebClient.Builder webClientBuilder,
            @Value("${app.prescription-service.host}") String prescriptionServiceHost,
            @Value("${app.prescription-service.port}") String prescriptionServicePort
    ) {
        this.webClientBuilder = webClientBuilder;
        prescriptionServiceUrl = "http://" + prescriptionServiceHost + ":" + prescriptionServicePort + "/prescriptions/";
    }

    public Mono<PrescriptionDetails> getPrescription(final int prescriptionId) {
        return webClientBuilder.build().get()
                .uri(prescriptionServiceUrl + prescriptionId)
                .retrieve()
                .bodyToMono(PrescriptionDetails.class);
    }

    public Flux<PrescriptionDetails> getPrescriptions() {
        return webClientBuilder.build().get()
                .uri(prescriptionServiceUrl)
                .retrieve()
                .bodyToFlux(PrescriptionDetails.class);
    }
}

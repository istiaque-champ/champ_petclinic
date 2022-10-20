package com.petclinic.bffapigateway.domainclientlayer;

import com.petclinic.bffapigateway.dtos.OwnerDetails;
import com.petclinic.bffapigateway.dtos.PrescriptionDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
        prescriptionServiceUrl = "http://" + prescriptionServiceHost + ":" + prescriptionServicePort;
    }

    public Mono<PrescriptionDetails> getPrescription(final int prescriptionId) {
        return webClientBuilder.build().get()
                .uri(prescriptionServiceUrl +"/1/prescriptions/"+ prescriptionId)
                .retrieve()
                .bodyToMono(PrescriptionDetails.class);
    }

    public Flux<PrescriptionDetails> getPrescriptions(Integer petId) {
        return webClientBuilder.build().get()
                .uri(prescriptionServiceUrl+"/"+petId+"/prescriptions")
                .retrieve()
                .bodyToFlux(PrescriptionDetails.class);
    }

    public Mono<PrescriptionDetails> createPrescription (PrescriptionDetails model){

        return webClientBuilder.build().post()
                .uri(prescriptionServiceUrl+"/1/prescriptions")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(model), PrescriptionDetails.class)
                .retrieve().bodyToMono(PrescriptionDetails.class);

    }

    public Mono<PrescriptionDetails> updatePrescription(Integer prescriptionId, PrescriptionDetails model) {
        return webClientBuilder.build().put()
                .uri(prescriptionServiceUrl+"/1/prescriptions/"+prescriptionId)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(model), PrescriptionDetails.class)
                .retrieve().bodyToMono(PrescriptionDetails.class);
    }

    public Mono<PrescriptionDetails> deletePrescription(Integer prescriptionId) {
        return webClientBuilder.build().delete()
                .uri(prescriptionServiceUrl+"/1/prescriptions/"+prescriptionId)
                .retrieve().bodyToMono(PrescriptionDetails.class);
    }
}

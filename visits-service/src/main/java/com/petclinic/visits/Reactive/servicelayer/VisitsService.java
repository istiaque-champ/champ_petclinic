package com.petclinic.visits.Reactive.servicelayer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface VisitsService {
    //Here are all the services that was offered in the last microservice.
    //I have made them into mono or in the validate as a flux.
    //Might have to double check here as some of the logic dont make sense or arent good but for the main part it works !
    Mono<VisitsDTO> addVisits(Mono<VisitsIDLessDTO> visitDTOMono );
    Mono<VisitsDTO>getVisitsForPet(int petId);
    Mono<VisitsDTO>getVisitssForPet(int petId, boolean scheduled);
    Mono<VisitsDTO>getVisitsByVisitId(String visitId);
    Mono<Void> deleteVisits(String visitId);
    Mono<VisitsDTO>updateVisit (String visitId, Mono<VisitsDTO> visitDTOMono);
    //Mono<VisitDTO>getVisitsForPets(List<Integer> petIds)
    Mono<VisitsDTO> getVisitsForPractitioner (int practitionerId);
    Mono<VisitsDTO>getVisitsByPractitionerIdAndMonth(int practitionerId, Date startDate, Date EndDate);
    Flux<VisitsDTO> validateVisitId(String visitId);



}

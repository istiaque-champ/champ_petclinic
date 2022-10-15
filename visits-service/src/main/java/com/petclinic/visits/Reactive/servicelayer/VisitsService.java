package com.petclinic.visits.Reactive.servicelayer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface VisitsService {

    Mono<VisitDTO> addVisits(Mono<VisitsIdLessDTO> visitDTOMono );
    Mono<VisitDTO>getVisitsForPet(int petId);
    Mono<VisitDTO>getVisitssForPet(int petId, boolean scheduled);
    Mono<VisitDTO>getVisitsByVisitId(String visitId);
    Mono<Void> deleteVisits(String visitId);
    Mono<VisitDTO>updateVisit (String visitId, Mono<VisitDTO> visitDTOMono);
    //Mono<VisitDTO>getVisitsForPets(List<Integer> petIds)
    Mono<VisitDTO> getVisitsForPractitioner (int practitionerId);
    Mono<VisitDTO>getVisitsByPractitionerIdAndMonth(int practitionerId, Date startDate, Date EndDate);
    Flux<VisitDTO> validateVisitId(String visitId);



}

package com.petclinic.visits.servicelayer;

import com.petclinic.visits.dataacesslayer.Reactive.VisitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.Date;

public class VisitsServiceImpl implements VisitsService{

    @Autowired
    VisitsRepository visitsRepository;


    @Override
    public Mono<VisitDTO> addVisits(Mono<VisitsIdLessDTO> visitDTOMono) {
        return null;
    }

    @Override
    public Mono<VisitDTO> getVisitsForPet(int petId) {
        return null;
    }

    @Override
    public Mono<VisitDTO> getVisitssForPet(int petId, boolean scheduled) {
        return null;
    }

    @Override
    public Mono<VisitDTO> getVisitsByVisitId(String visitId) {
        return null;
    }

    @Override
    public Mono<Void> deleteVisits(String visitId) {
        return null;
    }

    @Override
    public Mono<VisitDTO> updateVisit(String visitId, Mono<VisitDTO> visitDTOMono) {
        return null;
    }

    @Override
    public Mono<VisitDTO> getVisitsForPractitioner(int practitionerId) {
        return null;
    }

    @Override
    public Mono<VisitDTO> getVisitsByPractitionerIdAndMonth(int practitionerId, Date startDate, Date EndDate) {
        return null;
    }

    @Override
    public Flux<VisitDTO> validateVisitId(String visitId) {
        return null;
    }
}

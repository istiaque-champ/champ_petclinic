package com.petclinic.visits.Reactive.servicelayer;

import com.petclinic.visits.Reactive.Util.EntityDtoUtil;
import com.petclinic.visits.Reactive.dataacesslayer.VisitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.Date;

public class VisitsServiceImpl implements VisitsService{
// I did not have time to start implementing the IMPL
    // ran into some other issues that put myself back in my work

    @Autowired
    VisitsRepository visitsRepository;

    @Override
    public Mono<VisitsDTO> addVisits(Mono<VisitsIDLessDTO> visitDTOMono) {
        return null;
    }

    @Override
    public Mono<VisitsDTO> getVisitsForPet(int petId) {
        return null;
    }

    @Override
    public Mono<VisitsDTO> getVisitssForPet(int petId, boolean scheduled) {
        return null;
    }

    @Override
    public Mono<VisitsDTO> getVisitsByVisitId(String visitId) {
        return null;
    }

    @Override
    public Mono<Void> deleteVisits(String visitId) {
    return null;
    }

    @Override
    public Mono<VisitsDTO> updateVisit(String visitId, Mono<VisitsDTO> visitDTOMono) {

        return null;

        }

    @Override
    public Mono<VisitsDTO> getVisitsForPractitioner(int practitionerId) {
        return visitsRepository.findVisitsByPractitionerId(practitionerId)
                .map(EntityDtoUtil::toDTO);


    }

    @Override
    public Mono<VisitsDTO> getVisitsByPractitionerIdAndMonth(int practitionerId, Date startDate, Date EndDate) {
        return null;
    }

    @Override
    public Flux<VisitsDTO> validateVisitId(String visitId) {
        return null;
    }
}

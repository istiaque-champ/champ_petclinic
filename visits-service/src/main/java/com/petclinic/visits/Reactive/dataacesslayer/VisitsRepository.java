package com.petclinic.visits.Reactive.dataacesslayer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Date;

@Repository
public interface VisitsRepository extends ReactiveMongoRepository<Visits, Integer> {

    Mono<Visits> findbyId(int visitId);
    Mono<Visits> findByPetId(int petId);
    Mono<Visits>findByPetIdIn(Collection<Integer>petIds);
    Mono<Visits> findVisitsByPractitionerId(int practitionerId);
    Mono<Visits> findAllByDateInBetween (Date startingDate, Date EndDate);



}

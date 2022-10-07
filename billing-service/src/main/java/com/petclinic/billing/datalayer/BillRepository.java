package com.petclinic.billing.datalayer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Changed from JPA to ReactiveCRUD, makes the repo reactive without changing the entity
//Note: Mongo would also work, but this makes the conversion easier and faster
@Repository
public interface BillRepository extends ReactiveMongoRepository<Bill, Integer> {
    @Transactional(readOnly = true)
    Mono<Bill> findBillByBillId(int billId);

    @Transactional(readOnly = true)
    Flux<Bill> findBillsByCustomerId(int customerId);

    @Transactional(readOnly = true)
    Flux<Bill> findBillsByVetId(int vetId);

    @Transactional
    Mono<Void> deleteBillByBillId(int billId);
}

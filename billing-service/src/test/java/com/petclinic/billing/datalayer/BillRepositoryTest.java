package com.petclinic.billing.datalayer;

import com.petclinic.billing.businesslayer.BillServiceImpl;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
//recreating the test classes entirely as they are significantly different
class BillRepositoryTest {

    private static final int VALID_PET_ID = 1;
    @Autowired
    BillRepository billRepository;

    private final Integer VALID_BILL_ID = 1;
    private final Integer VALID_CUSTOMER_ID = 1;
    private final String VALID_VISIT_TYPE = "Examinations";
    private final Instant VALID_DATE = Instant.now();
    private final Double VALID_AMOUNT = BillServiceImpl.visitTypePrices.get(VALID_VISIT_TYPE);
    private final Double SECOND_VALID_AMOUNT = 50.00;
    private final Integer VALID_VET_ID = 1;
    private final Integer VALID_VISIT_ID = 1;



    private Bill getSetupBill() {
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);
        setupBill.setVetId(VALID_VET_ID);
        setupBill.setPetId(VALID_PET_ID);
        setupBill.setVisitId(VALID_VISIT_ID);
        return setupBill;
    }

    @Test
    void findAllBills(){
        Bill setupBill = getSetupBill();

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        Flux<Bill> find = billRepository.findAll();
        Publisher<Bill> composite = Mono
                .from(setup)
                .thenMany(find);

        StepVerifier
                .create(composite)
                .consumeNextWith(bill -> {
                    assertNotNull(bill.getId());
                    assertEquals(bill.getBillId(), setupBill.getBillId());
                    assertEquals(bill.getCustomerId(), setupBill.getCustomerId());
                    assertEquals(bill.getVisitType(), setupBill.getVisitType());
                    assertNotNull(bill.getDate());
                    assertEquals(bill.getAmount(), setupBill.getAmount());
                    assertEquals(bill.getVetId(), setupBill.getVetId());
                    assertEquals(bill.getPetId(), setupBill.getPetId());
                    assertEquals(bill.getVisitId(), setupBill.getVisitId());
                })
                .verifyComplete();
    }

    @Test
    void createBill(){
        Bill setupBill = getSetupBill();

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .consumeNextWith(bill -> {
                    assertNotNull(bill.getId());
                    assertEquals(bill.getBillId(), setupBill.getBillId());
                    assertEquals(bill.getCustomerId(), setupBill.getCustomerId());
                    assertEquals(bill.getVisitType(), setupBill.getVisitType());
                    assertNotNull(bill.getDate());
                    assertEquals(bill.getAmount(), setupBill.getAmount());
                    assertEquals(bill.getVetId(), setupBill.getVetId());
                    assertEquals(bill.getPetId(), setupBill.getPetId());
                    assertEquals(bill.getVisitId(), setupBill.getVisitId());
                })
                .verifyComplete();
    }

    //checks both create and find by bill_id
    @Test
    void findBillByBillId() {
        Bill setupBill = getSetupBill();

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        Mono<Bill> find = billRepository.findBillByBillId(VALID_BILL_ID);
        Publisher<Bill> composite = Mono
                .from(setup)
                .then(find);

        StepVerifier
                .create(composite)
                .consumeNextWith(bill -> {
                    assertNotNull(bill.getId());
                    assertEquals(bill.getBillId(), setupBill.getBillId());
                    assertEquals(bill.getCustomerId(), setupBill.getCustomerId());
                    assertEquals(bill.getVisitType(), setupBill.getVisitType());
                    assertNotNull(bill.getDate());
                    assertEquals(bill.getAmount(), setupBill.getAmount());
                    assertEquals(bill.getVetId(), setupBill.getVetId());
                    assertEquals(bill.getPetId(), setupBill.getPetId());
                    assertEquals(bill.getVisitId(), setupBill.getVisitId());
                })
                .verifyComplete();
    }

    @Test
    void findBillByCustomerId() {
        Bill setupBill = getSetupBill();

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        Flux<Bill> find = billRepository.findBillsByCustomerId(VALID_CUSTOMER_ID);
        Publisher<Bill> composite = Mono
                .from(setup)
                .thenMany(find);

        StepVerifier
                .create(composite)
                .consumeNextWith(bill -> {
                    assertNotNull(bill.getId());
                    assertEquals(bill.getBillId(), setupBill.getBillId());
                    assertEquals(bill.getCustomerId(), setupBill.getCustomerId());
                    assertEquals(bill.getVisitType(), setupBill.getVisitType());
                    assertNotNull(bill.getDate());
                    assertEquals(bill.getAmount(), setupBill.getAmount());
                    assertEquals(bill.getVetId(), setupBill.getVetId());
                    assertEquals(bill.getPetId(), setupBill.getPetId());
                    assertEquals(bill.getVisitId(), setupBill.getVisitId());
                })
                .verifyComplete();
    }

    @Test
    void findBillsByVetId(){
        Bill setupBill = getSetupBill();

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        Flux<Bill> find = billRepository.findBillsByVetId(VALID_VET_ID);
        Publisher<Bill> composite = Mono
                .from(setup)
                .thenMany(find);

        StepVerifier
                .create(composite)
                .consumeNextWith(bill -> {
                    assertNotNull(bill.getId());
                    assertEquals(bill.getBillId(), setupBill.getBillId());
                    assertEquals(bill.getCustomerId(), setupBill.getCustomerId());
                    assertEquals(bill.getVisitType(), setupBill.getVisitType());
                    assertNotNull(bill.getDate());
                    assertEquals(bill.getAmount(), setupBill.getAmount());
                    assertEquals(bill.getVetId(), setupBill.getVetId());
                    assertEquals(bill.getPetId(), setupBill.getPetId());
                    assertEquals(bill.getVisitId(), setupBill.getVisitId());
                })
                .verifyComplete();
    }



    @Test
    void findBillsByPetId(){
        Bill setupBill = getSetupBill();

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        Flux<Bill> find = billRepository.findBillsByPetId(VALID_PET_ID);
        Publisher<Bill> composite = Mono
                .from(setup)
                .thenMany(find);

        StepVerifier
                .create(composite)
                .consumeNextWith(bill -> {
                    assertNotNull(bill.getId());
                    assertEquals(bill.getBillId(), setupBill.getBillId());
                    assertEquals(bill.getCustomerId(), setupBill.getCustomerId());
                    assertEquals(bill.getVisitType(), setupBill.getVisitType());
                    assertNotNull(bill.getDate());
                    assertEquals(bill.getAmount(), setupBill.getAmount());
                    assertEquals(bill.getVetId(), setupBill.getVetId());
                    assertEquals(bill.getPetId(), setupBill.getPetId());
                    assertEquals(bill.getVisitId(), setupBill.getVisitId());
                })
                .verifyComplete();
    }

    @Test
    void deleteBillByBillId() {
        Bill setupBill = getSetupBill();

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        Mono<Void> delete = billRepository.deleteBillByBillId(VALID_BILL_ID);

        Publisher<Void> composite = Mono
                .from(setup)
                .then(delete);

        StepVerifier
                .create(composite)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void update(){
        Bill setupBill = getSetupBill();

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        Mono<Bill> change = billRepository.findBillByBillId(VALID_BILL_ID)
                .flatMap(bill -> {
                    bill.setAmount(SECOND_VALID_AMOUNT);
                    return billRepository.save(bill);
                });

        Publisher<Bill> composite = Mono
                .from(setup)
                .then(change);

        StepVerifier
                .create(composite)
                .consumeNextWith(bill -> {
                    assertNotNull(bill.getId());
                    assertEquals(bill.getBillId(), setupBill.getBillId());
                    assertEquals(bill.getCustomerId(), setupBill.getCustomerId());
                    assertEquals(bill.getVisitType(), setupBill.getVisitType());
                    assertNotNull(bill.getDate());
                    assertEquals(bill.getAmount(), SECOND_VALID_AMOUNT);
                    assertEquals(bill.getVetId(), setupBill.getVetId());
                    assertEquals(bill.getPetId(), setupBill.getPetId());
                    assertEquals(bill.getVisitId(), setupBill.getVisitId());
                })
                .verifyComplete();
    }

    @Test
    void findBillByVisitId(){
        Bill setupBill = getSetupBill();

        Publisher<Bill> setup = billRepository.deleteAll().thenMany(billRepository.save(setupBill));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

        Mono<Bill> find = billRepository.findBillByVisitId(VALID_VISIT_ID);
        Publisher<Bill> composite = Mono
                .from(setup)
                .thenMany(find);

        StepVerifier
                .create(composite)
                .consumeNextWith(bill -> {
                    assertNotNull(bill.getId());
                    assertEquals(bill.getBillId(), setupBill.getBillId());
                    assertEquals(bill.getCustomerId(), setupBill.getCustomerId());
                    assertEquals(bill.getVisitType(), setupBill.getVisitType());
                    assertNotNull(bill.getDate());
                    assertEquals(bill.getAmount(), setupBill.getAmount());
                    assertEquals(bill.getVetId(), setupBill.getVetId());
                    assertEquals(bill.getPetId(), setupBill.getPetId());
                    assertEquals(bill.getVisitId(), setupBill.getVisitId());
                })
                .verifyComplete();
    }
}
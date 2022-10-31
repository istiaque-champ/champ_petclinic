package com.petclinic.billing.businesslayer;

import com.petclinic.billing.datalayer.BillDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BillService {
    Mono<BillDTO> GetBill(int billId);

    Flux<BillDTO> GetAllBills();

    Mono<BillDTO> CreateBill(Mono<BillDTO> model);

    Mono<Void> DeleteBill(int billId);

    Flux<BillDTO> GetBillsByCustomerId(int customerId);

    Flux<BillDTO> GetBillsByVetId(int vetId);

    Flux<BillDTO> GetBillsByPetId(int petId);
    Mono<BillDTO> GetBillByVisitId(int visitId);

    Mono<BillDTO> UpdateBill(Mono<BillDTO> model, int billId);
}

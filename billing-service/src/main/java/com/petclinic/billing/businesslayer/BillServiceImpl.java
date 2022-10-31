package com.petclinic.billing.businesslayer;

import com.petclinic.billing.datalayer.BillDTO;
import com.petclinic.billing.datalayer.BillRepository;
import com.petclinic.billing.util.EntityDTOUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Service
public class BillServiceImpl implements BillService{
    private final BillRepository billRepository;

    //made the hashmap a static variable, so it's only created once. Public because EntityDTOUtil uses it
    public static final HashMap<String, Double> visitTypePrices = setUpVisitList();

    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    private static HashMap<String, Double> setUpVisitList(){
        HashMap<String, Double> visitTypesPrices = new HashMap<>();
        visitTypesPrices.put("Examinations", 59.99);
        visitTypesPrices.put("Injury", 229.99);
        visitTypesPrices.put("Medical", 109.99);
        visitTypesPrices.put("Chronic", 89.99);
        visitTypesPrices.put("Consultations", 39.99);
        visitTypesPrices.put("Operations", 399.99);
        return visitTypesPrices;
    }

    //Get a bill by its billId
    @Override
    public Mono<BillDTO> GetBill(int billId) {
      return billRepository.findBillByBillId(billId)
             .map(EntityDTOUtil::entityToDTO);
    }
    //Gets all bills
    @Override
    public Flux<BillDTO> GetAllBills() {
        return billRepository.findAll()
                .map(EntityDTOUtil::entityToDTO);
    }

    //Create a bill based on the DTO
    @Override
    public Mono<BillDTO> CreateBill(Mono<BillDTO> model) {
        //Validation checks moved to EntityDTOUtil
        return model
                .map(EntityDTOUtil::dtoToEntity)
                .flatMap(billRepository::save)
                .map(EntityDTOUtil::entityToDTO);
    }

    @Override
    public Mono<BillDTO> UpdateBill(Mono<BillDTO> model, int billId){
        return billRepository.findBillByBillId(billId)
                .flatMap(bill -> model
                        .map(EntityDTOUtil::dtoToEntity)
                        .doOnNext(b -> b.setBillId(bill.getBillId()))
                        .doOnNext(b -> b.setId(bill.getId()))
                )
                .flatMap(billRepository::save)
                .map(EntityDTOUtil::entityToDTO);
    }

    //Deletes a bill by its id
    @Override
    public Mono<Void> DeleteBill(int billId) {
        return billRepository.deleteBillByBillId(billId);
    }

    //Returns all the bills of a specific customer by its customer id
    @Override
    public Flux<BillDTO> GetBillsByCustomerId(int customerId) {
            return billRepository.findBillsByCustomerId(customerId)
                    .map(EntityDTOUtil::entityToDTO);

    }

    @Override
    public Flux<BillDTO> GetBillsByVetId(int vetId) {
        return billRepository.findBillsByVetId(vetId)
                .map(EntityDTOUtil::entityToDTO);
    }

    @Override
    public Flux<BillDTO> GetBillsByPetId(int petId) {
        return billRepository.findBillsByPetId(petId)
                .map(EntityDTOUtil::entityToDTO);
    }

    @Override
    public Mono<BillDTO> GetBillByVisitId(int visitId) {
        return billRepository.findBillByVisitId(visitId)
                .map(EntityDTOUtil::entityToDTO);
    }
}

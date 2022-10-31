package com.petclinic.billing.presentationlayer;

import com.petclinic.billing.businesslayer.BillService;
import com.petclinic.billing.datalayer.BillDTO;
import com.petclinic.billing.util.EntityDTOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/bills")
public class BillResource {
    private final BillService SERVICE;

    BillResource(BillService service){
        this.SERVICE = service;
    }

    // Create Bill //
    //WE NEED TO FIGURE OUT HOW TO MAKE IT HAVE A CREATED STATUS
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<BillDTO>> createBill(@Valid @RequestBody Mono<BillDTO> billDTO){
        return SERVICE.CreateBill(billDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "{billId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<BillDTO>> updateBill(@RequestBody Mono<BillDTO> billDTO, @PathVariable int billId){
        return SERVICE.UpdateBill(billDTO, EntityDTOUtil.verifyId(billId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Read Bill //
    @GetMapping(value = "{billId}")
    public Mono<ResponseEntity<BillDTO>> findBill(@PathVariable("billId") int billId){
        return SERVICE.GetBill(EntityDTOUtil.verifyId(billId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping()
    public Flux<BillDTO> findAllBills() {
        return SERVICE.GetAllBills();
    }

    @GetMapping(value = "/customer/{customerId}")
    public Flux<BillDTO> getBillsByCustomerId(@PathVariable("customerId") int customerId) {return SERVICE.GetBillsByCustomerId( EntityDTOUtil.verifyId(customerId));}

    @GetMapping(value = "/vets/{vetId}")
    public Flux<BillDTO> getBillsByVetId(@PathVariable int vetId){
        return SERVICE.GetBillsByVetId(EntityDTOUtil.verifyId(vetId));
    }

    @GetMapping(value = "/pets/{petId}")
    public Flux<BillDTO> getBillsByPetId(@PathVariable int petId){
        return SERVICE.GetBillsByPetId(EntityDTOUtil.verifyId(petId));
    }

    @GetMapping(value = "/visits/{visitId}")
    public Mono<ResponseEntity<BillDTO>> findBillByVisitId(@PathVariable int visitId){
        return SERVICE.GetBillByVisitId(EntityDTOUtil.verifyId(visitId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    // Delete Bill //
    @DeleteMapping(value = "{billId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBill(@PathVariable("billId") int billId){
        return SERVICE.DeleteBill(EntityDTOUtil.verifyId(billId));
    }
}

package com.petclinic.billing.businesslayer;

import com.petclinic.billing.datalayer.BillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataSetup implements CommandLineRunner {

    private static final int VALID_CUSTOMER_ID1 = 1;
    private static final int VALID_CUSTOMER_ID2 = 2;
    private static final String VALID_VISIT_TYPE = "Examinations";
    private static final Instant VALID_DATE = Instant.now();
    private static final int VALID_VET_ID1 = 1;
    private static final int VALID_VET_ID2 = 2;
    private static final int VALID_PET_ID1 = 1;
    private static final int VALID_PET_ID2 = 2;

    @Autowired
    private BillService billService;

    @Override
    public void run(String... args) throws Exception {
        List<BillDTO> billDTOS = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            BillDTO setupBill = new BillDTO();
            setupBill.setCustomerId(i % 2 == 0 ? VALID_CUSTOMER_ID1 : VALID_CUSTOMER_ID2);
            setupBill.setVisitType(VALID_VISIT_TYPE);
            setupBill.setDate(VALID_DATE);
            setupBill.setVetId(i % 2 == 0 ? VALID_VET_ID1 : VALID_VET_ID2);
            setupBill.setPetId(i % 2 == 0 ? VALID_PET_ID1 : VALID_PET_ID2);

            billDTOS.add(setupBill);
        }

        Flux.fromIterable(billDTOS)
                .flatMap(p -> billService.CreateBill(Mono.just(p))
                        .log(p.toString()))
                .subscribe();
    }
}

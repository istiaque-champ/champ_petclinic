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

    private static final int VALID_CUSTOMER_ID = 1;
    private static final String VALID_VISIT_TYPE = "Examinations";
    private static final Instant VALID_DATE = Instant.now();

    @Autowired
    private BillService billService;

    @Override
    public void run(String... args) throws Exception {
        List<BillDTO> billDTOS = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            BillDTO setupBill = new BillDTO();
            setupBill.setCustomerId(VALID_CUSTOMER_ID);
            setupBill.setVisitType(VALID_VISIT_TYPE);
            setupBill.setDate(VALID_DATE);

            billDTOS.add(setupBill);
        }

        Flux.fromIterable(billDTOS)
                .flatMap(p -> billService.CreateBill(Mono.just(p))
                        .log(p.toString()))
                .subscribe();
    }
}

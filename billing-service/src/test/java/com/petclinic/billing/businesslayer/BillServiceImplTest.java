package com.petclinic.billing.businesslayer;

import com.petclinic.billing.datalayer.Bill;
import com.petclinic.billing.datalayer.BillDTO;
import com.petclinic.billing.datalayer.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BillServiceImplTest {

    @MockBean
    BillRepository billRepository;

    @Autowired
    BillService billService;

    private final Integer VALID_BILL_ID = 1;
    private final Integer VALID_CUSTOMER_ID = 1;
    private final String VALID_VISIT_TYPE = "Examinations";
    private final Instant VALID_DATE = Instant.now();
    private final Double VALID_AMOUNT = BillServiceImpl.visitTypePrices.get(VALID_VISIT_TYPE);
    private final Double SECOND_VALID_AMOUNT = 50.00;
    private final Integer VALID_VET_ID = 1;
    private final Integer VALID_PET_ID = 1;


    @BeforeEach
    void setup() {
        //Create bill object
        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(VALID_AMOUNT);
        setupBill.setVetId(VALID_VET_ID);
        setupBill.setPetId(VALID_PET_ID);

        //Setup mock repo
        when(billRepository.save(any(Bill.class))).thenReturn(Mono.just(setupBill));
        when(billRepository.findBillByBillId(VALID_BILL_ID)).thenReturn(Mono.just(setupBill));
        when(billRepository.deleteBillByBillId(VALID_BILL_ID)).thenReturn(Mono.empty());
        when(billRepository.findBillsByCustomerId(VALID_CUSTOMER_ID)).thenReturn(Flux.just(setupBill));
        when(billRepository.findAll()).thenReturn(Flux.just(setupBill));
        when(billRepository.findBillsByVetId(VALID_VET_ID)).thenReturn(Flux.just(setupBill));
        when(billRepository.findBillsByPetId(VALID_PET_ID)).thenReturn(Flux.just(setupBill));
    }

    @Test
    public void test_GetBill(){
        billService.GetBill(VALID_BILL_ID)
                .map(billDTO -> {
                    assertEquals(billDTO.getBillId(), VALID_BILL_ID);
                    assertEquals(billDTO.getCustomerId(), VALID_CUSTOMER_ID);
                    assertEquals(billDTO.getVisitType(), VALID_VISIT_TYPE);
                    assertEquals(billDTO.getDate(), VALID_DATE);
                    assertEquals(billDTO.getAmount(), VALID_AMOUNT);
                    assertEquals(billDTO.getVetId(), VALID_VET_ID);
                    assertEquals(billDTO.getPetId(), VALID_PET_ID);
                    return billDTO;
                });
    }

    @Test
    public void test_GetAllBills() {
        billService.GetAllBills()
                .map(billDTO -> {
                    assertEquals(billDTO.getBillId(), VALID_BILL_ID);
                    assertEquals(billDTO.getCustomerId(), VALID_CUSTOMER_ID);
                    assertEquals(billDTO.getVisitType(), VALID_VISIT_TYPE);
                    assertEquals(billDTO.getDate(), VALID_DATE);
                    assertEquals(billDTO.getAmount(), VALID_AMOUNT);
                    assertEquals(billDTO.getVetId(), VALID_VET_ID);
                    assertEquals(billDTO.getPetId(), VALID_PET_ID);
                    return billDTO;
                });
    }

    @Test
    public void test_CreateBill(){
        BillDTO billDTOInput = new BillDTO();
        billDTOInput.setDate(VALID_DATE);
        billDTOInput.setVisitType(VALID_VISIT_TYPE);
        billDTOInput.setCustomerId(VALID_CUSTOMER_ID);
        billDTOInput.setVetId(VALID_VET_ID);

        billService.CreateBill(Mono.just(billDTOInput))
                .map(billDTO -> {
                    assertEquals(billDTO.getCustomerId(), VALID_CUSTOMER_ID);
                    assertEquals(billDTO.getVisitType(), VALID_VISIT_TYPE);
                    assertEquals(billDTO.getDate(), VALID_DATE);
                    assertEquals(billDTO.getAmount(), VALID_AMOUNT);
                    assertEquals(billDTO.getVetId(), VALID_VET_ID);
                    assertEquals(billDTO.getPetId(), VALID_PET_ID);
                    return billDTO;
                });
    }


    @Test
    public void test_DeleteBill(){
        billService.DeleteBill(VALID_BILL_ID);
        verify(billRepository, times(1)).deleteBillByBillId(VALID_BILL_ID);
    }
    @Test
    public void test_GetBillByCustomerId(){
        billService.GetBillsByCustomerId(VALID_CUSTOMER_ID)
                .map(billDTO -> {
                    assertEquals(billDTO.getBillId(), VALID_BILL_ID);
                    assertEquals(billDTO.getCustomerId(), VALID_CUSTOMER_ID);
                    assertEquals(billDTO.getVisitType(), VALID_VISIT_TYPE);
                    assertEquals(billDTO.getDate(), VALID_DATE);
                    assertEquals(billDTO.getAmount(), VALID_AMOUNT);
                    assertEquals(billDTO.getVetId(), VALID_VET_ID);
                    assertEquals(billDTO.getPetId(), VALID_PET_ID);
                    return billDTO;
                });
    }

    @Test
    void updateBill() {
        BillDTO billDTOInput = new BillDTO();
        billDTOInput.setDate(VALID_DATE);
        billDTOInput.setVisitType(VALID_VISIT_TYPE);
        billDTOInput.setCustomerId(VALID_CUSTOMER_ID);
        billDTOInput.setVetId(VALID_VET_ID);
        billDTOInput.setAmount(SECOND_VALID_AMOUNT);

        Bill setupBill = new Bill();
        setupBill.setBillId(VALID_BILL_ID);
        setupBill.setCustomerId(VALID_CUSTOMER_ID);
        setupBill.setVisitType(VALID_VISIT_TYPE);
        setupBill.setDate(VALID_DATE);
        setupBill.setAmount(SECOND_VALID_AMOUNT);
        setupBill.setVetId(VALID_VET_ID);

        when(billRepository.save(any(Bill.class))).thenReturn(Mono.just(setupBill));

        billService.UpdateBill(Mono.just(billDTOInput), VALID_BILL_ID)
                .map(billDTO -> {
                    assertEquals(billDTO.getBillId(), VALID_BILL_ID);
                    assertEquals(billDTO.getCustomerId(), VALID_CUSTOMER_ID);
                    assertEquals(billDTO.getVisitType(), VALID_VISIT_TYPE);
                    assertEquals(billDTO.getDate(), VALID_DATE);
                    assertEquals(billDTO.getAmount(), SECOND_VALID_AMOUNT);
                    assertEquals(billDTO.getVetId(), VALID_VET_ID);
                    assertEquals(billDTO.getPetId(), VALID_PET_ID);
                    return billDTO;
                });
    }

    @Test
    void test_GetBillByVetId(){
        billService.GetBillsByVetId(VALID_VET_ID)
                .map(billDTO -> {
                    assertEquals(billDTO.getBillId(), VALID_BILL_ID);
                    assertEquals(billDTO.getCustomerId(), VALID_CUSTOMER_ID);
                    assertEquals(billDTO.getVisitType(), VALID_VISIT_TYPE);
                    assertEquals(billDTO.getDate(), VALID_DATE);
                    assertEquals(billDTO.getAmount(), VALID_AMOUNT);
                    assertEquals(billDTO.getVetId(), VALID_VET_ID);
                    assertEquals(billDTO.getPetId(), VALID_PET_ID);
                    return billDTO;
                });
    }

    @Test
    void test_GetBillByPetId(){
        billService.GetBillsByPetId(VALID_PET_ID)
                .map(billDTO -> {
                    assertEquals(billDTO.getBillId(), VALID_BILL_ID);
                    assertEquals(billDTO.getCustomerId(), VALID_CUSTOMER_ID);
                    assertEquals(billDTO.getVisitType(), VALID_VISIT_TYPE);
                    assertEquals(billDTO.getDate(), VALID_DATE);
                    assertEquals(billDTO.getAmount(), VALID_AMOUNT);
                    assertEquals(billDTO.getVetId(), VALID_VET_ID);
                    assertEquals(billDTO.getPetId(), VALID_PET_ID);
                    return billDTO;
                });
    }
}


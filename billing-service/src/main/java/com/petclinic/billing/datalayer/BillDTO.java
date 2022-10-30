package com.petclinic.billing.datalayer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Setter
@Getter
@NoArgsConstructor
public class BillDTO {

    private int billId;
    private int customerId;
    private int petId;
    private int vetId;
    private String visitType;
    private Instant date;
    private double amount;

}
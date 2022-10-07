package com.petclinic.billing.datalayer;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@NoArgsConstructor
public class BillDTO {

    private int billId;
    private int customerId;
    private int vetId;
    private String visitType;
    private Instant date;
    private double amount;

}
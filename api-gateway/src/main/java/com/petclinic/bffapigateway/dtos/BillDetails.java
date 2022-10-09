package com.petclinic.bffapigateway.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
public class BillDetails {
    private int billId;
    private Date date;
    private int customerId;
    private int vetId;
    private int petId;
    private String visitType;
    private double amount;

}

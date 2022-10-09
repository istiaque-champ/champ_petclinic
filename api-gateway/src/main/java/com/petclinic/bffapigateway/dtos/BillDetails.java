package com.petclinic.bffapigateway.dtos;

import lombok.Data;

import java.util.Date;


@Data
public class BillDetails {
    private int billId;
    private Date date;
    private int customerId;
    private int vetId;
    private int petId;
    private String visitType;
    private double amount;

}

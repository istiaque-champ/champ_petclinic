package com.petclinic.billing.datalayer;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "bills")
public class Bill {
    @Id
    private String id;

    @Indexed(unique = true)
    private Integer billId;

    private Integer customerId;

    private String visitType;

    private Instant date = Instant.now();

    private double amount;

    public Bill(){}

    public String getId(){return id;}
    public int getBillId() {
        return billId;
    }
    public int getCustomerId(){return  customerId;}
    public Instant getDate(){return date;}
    public String getVisitType(){return visitType;}
    public double getAmount(){return amount;}

    public void setId(String id) {
        this.id = id;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public void setCustomerId(int customerId){this.customerId = customerId;}

    public void setDate(Instant date) {
        this.date = date;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
package com.petclinic.billing.datalayer;

import org.springframework.lang.Nullable;

import java.time.Instant;


public class BillDTO {

    private int billId;
    private int customerId;
    private String visitType;
    private Instant date;
    @Nullable
    private double amount;

    public BillDTO(){
        billId = 0;
        customerId = 0;
        visitType = null;
        date = null;
        amount = 0;

    }


    public int getBillId(){
        return billId;
    }
    public int getCustomerId(){return customerId;}
    public Instant getDate(){
        return date;
    }
    public String getVisitType(){
        return visitType;
    }
    public double getAmount(){
        return amount;
    }

    public void setBillId(int billId){
        this.billId = billId;
    }

    public void setCustomerId(int customerId){this.customerId = customerId;}

    public void setDate(Instant date){
        this.date = date;
    }

    public void setVisitType(String visitType){
        this.visitType = visitType;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }
}
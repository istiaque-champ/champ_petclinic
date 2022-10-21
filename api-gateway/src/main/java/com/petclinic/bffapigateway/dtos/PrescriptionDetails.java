package com.petclinic.bffapigateway.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PrescriptionDetails {
    private Integer prescriptionId;
    private Integer petId;
    private String medication;
    private String amount;
    private String datePrinted;
    private String instructions;
}

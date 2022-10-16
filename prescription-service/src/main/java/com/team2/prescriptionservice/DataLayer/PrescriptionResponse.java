package com.team2.prescriptionservice.DataLayer;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Setter
@Getter
@NoArgsConstructor
public class PrescriptionResponse extends RepresentationModel<PrescriptionResponse>{
    private Integer prescriptionId;
    private String medication;
    private String amount;
    private Date datePrinted;
    private String instructions;
}

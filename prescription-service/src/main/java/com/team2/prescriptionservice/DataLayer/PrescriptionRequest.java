package com.team2.prescriptionservice.DataLayer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class PrescriptionRequest extends RepresentationModel<PrescriptionRequest> {
    private String medication;
    private String amount;
    private String datePrinted;
    private String instructions;
}

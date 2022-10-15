package com.petclinic.visits.servicelayer;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class VisitDTO {
    private String visitId;
    private Date date;
    private String description;
    private int petId;
    private int practitionerId;
    private boolean status;

}

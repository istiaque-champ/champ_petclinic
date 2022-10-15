package com.petclinic.visits.Reactive.servicelayer;

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
    private String petId;
    private String  practitionerId;
    private boolean status;

}

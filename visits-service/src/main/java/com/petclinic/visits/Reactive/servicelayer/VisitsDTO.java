package com.petclinic.visits.Reactive.servicelayer;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

//dto from the visits class in DATAACESSLAYER
public class VisitsDTO {

    private String visitId;
    private Date date;
    private String description;
    private int petId;
    private int  practitionerId;
    private boolean status;

}

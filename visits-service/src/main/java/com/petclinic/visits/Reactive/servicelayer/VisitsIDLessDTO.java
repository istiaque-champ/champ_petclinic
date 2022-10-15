package com.petclinic.visits.Reactive.servicelayer;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitsLessDTO {
    //changed the int to string petId to make it more conform to the other ids
    private Date date;
    private String description;
    private String petId;
    private String practitionerId;
    private boolean status;
}

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
public class VisitsIdLessDTO {
    private Date date;
    private String description;
    private int petId;
    private int practitionerId;
    private boolean status;
}

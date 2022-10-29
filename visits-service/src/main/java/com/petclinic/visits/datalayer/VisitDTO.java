package com.petclinic.visits.datalayer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitDTO {
    //it is a string due to the fact it is a UUID
    private String visitId;
// json format for the date and the pattern
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String description;
    //Id for the pet selected
    private int petId;
    //id for the practitioner
    private int practitionerId;
    //status is a boolean, if is active or not 
    private boolean status;

}

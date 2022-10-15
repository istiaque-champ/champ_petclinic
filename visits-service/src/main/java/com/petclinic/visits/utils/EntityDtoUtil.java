package com.petclinic.visits.utils;

import com.petclinic.visits.dataacesslayer.Reactive.Visits;
import com.petclinic.visits.datalayer.Visit;
import com.petclinic.visits.servicelayer.VisitDTO;
import lombok.Generated;


public class EntityDtoUtil {

  @Generated
    public EntityDtoUtil(){}

    public static VisitDTO toDTO(Visits visits){

      VisitDTO dto = new VisitDTO();
      dto.setVisitId(visits.getVisitId());


//finsih up the dtos
      return dto;
    }


}

package com.petclinic.visits.utils;

import com.petclinic.visits.Reactive.dataacesslayer.Visits;
import com.petclinic.visits.Reactive.servicelayer.VisitDTO;
import com.petclinic.visits.datalayer.Visit;
import com.petclinic.visits.datalayer.VisitIdLessDTO;
import lombok.Generated;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Random;


public class EntityDTOUtil {

  @Generated
    public EntityDTOUtil(){}

    public static VisitDTO toDTO(Visits visits){

      VisitDTO dto = new VisitDTO();
      dto.setVisitId(visits.getVisitId());
      dto.setDate(visits.getDate());
      dto.setDescription(visits.getDescription());
      dto.setPetId(visits.getPetId());
      dto.setPractitionerId(visits.getPractitionerId());
      dto.setStatus(visits.isStatus());

      //can i change the pet id and practitioner id to string ?check VisitDTO
        //finsih up the dtos
      return dto;
    }

    public static Visits toEntity (VisitDTO dto){

    Visits visits = new Visits();
    visits.setVisitId(generateVisitId());
    visits.setDate(new Date());
    visits.setPetId(dto.getPetId());
    visits.setPractitionerId(dto.getPractitionerId());
    visits.setStatus(dto.isStatus());


    return visits;
    }

  private static String generateVisitId() {
    Random random = new Random();
    int number = random.nextInt(99999);
    return "22" + (String.format("%05d", number));
  }


  public static Visit dtoToEntity(Mono<VisitIdLessDTO> visit) {
  }
}

package com.petclinic.visits.utils;

import com.petclinic.visits.datalayer.Visit;
import com.petclinic.visits.datalayer.VisitDTO;

import com.petclinic.visits.utils.exceptions.InvalidInputException;
import lombok.Generated;
import org.springframework.beans.BeanUtils;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class EntityDTOUtil{
  @Generated
  public EntityDTOUtil(){}

  public static VisitDTO entityToDTO(Mono<Visit> visit){
    VisitDTO visitDTO = new VisitDTO();
    BeanUtils.copyProperties(visit, visitDTO);
    return visitDTO;
  }

  public static Mono<Visit> dtoToEntity(VisitDTO visitDTO){
    visitDTO.setVisitId(String.valueOf(UUID.randomUUID()));

    //Check Valid IDs
    if(visitDTO.getPractitionerId() <= 0){
      throw new InvalidInputException("The practitioner id is invalid");
    }
    if(visitDTO.getPetId() <= 0){
      throw new InvalidInputException("The pet id is invalid");
    }

    //if(VisitsServiceImpl.)

    Mono<Visit> visit = new Mono<Visit>() {
      @Override
      public void subscribe(CoreSubscriber<? super Visit> actual) {

      }
    };
    BeanUtils.copyProperties(visitDTO, visit);

    return visit;
  }

  public static Integer verifyId(Integer id){
    if(id <= 0){
      throw new InvalidInputException("That id is invalid");
    }
    return id;
  }
}

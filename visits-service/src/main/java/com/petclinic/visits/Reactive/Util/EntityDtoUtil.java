package com.petclinic.visits.Reactive.Util;

import com.petclinic.visits.Reactive.dataacesslayer.Visits;
import com.petclinic.visits.Reactive.dataacesslayer.VisitsIdLess;
import com.petclinic.visits.Reactive.servicelayer.VisitsDTO;
import com.petclinic.visits.Reactive.servicelayer.VisitsIDLessDTO;
import lombok.Generated;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class EntityDtoUtil {

  @Generated
    public EntityDtoUtil(){}

    public static VisitsDTO toDTO(Visits visits){
//setting out our DTO data to DTO
      VisitsDTO dto = new VisitsDTO();
      dto.setVisitId(visits.getVisitId());
      dto.setDate(visits.getDate());
      dto.setDescription(visits.getDescription());
      dto.setPetId(visits.getPetId());
      dto.setPractitionerId(visits.getPractitionerId());
      dto.setStatus(visits.isStatus());
        //finsih up the dtos
      return dto;
    }
//settings the visitsCLass to entity
    public static Visits toEntity (VisitsDTO dto){

    Visits visits = new Visits();
    visits.setVisitId(generateVisitId());
    visits.setDate(new Date());
    visits.setPetId(dto.getPetId());
    visits.setPractitionerId(dto.getPractitionerId());
    visits.setStatus(dto.isStatus());


    return visits;
    }
//autogenerate the VisitsID
  private static String generateVisitId() {
    Random random = new Random();
    int number = random.nextInt(99999);
    return "22" + (String.format("%05d", number));
  }

  // getting from the visitsLessDTO to the visitsIdLess entity

  public static VisitsIDLessDTO toDTO(VisitsIdLess visitsIdLess) {
    VisitsIDLessDTO dto = new VisitsIDLessDTO();
    BeanUtils.copyProperties(visitsIdLess, dto);
    return dto;
  }
  // getting from the visitsIdLess entity to the DTO

  public static VisitsIdLess toEntity(VisitsIDLessDTO dto) {
    VisitsIdLess visitsIdLess = new VisitsIdLess();
    BeanUtils.copyProperties(dto, visitsIdLess);
    return visitsIdLess;
  }


  public static Set<VisitsIDLessDTO> toDTOSet(Set<VisitsIdLess> visitsLess) {
    Set<VisitsIDLessDTO> visitsIDLessDTOs = new HashSet<>();
    for (VisitsIdLess visitsIdLess:
            visitsLess) {
      VisitsIDLessDTO visitsIDLessDTO = toDTO(visitsIdLess);
      visitsIDLessDTOs.add(visitsIDLessDTO);
    }

    return visitsIDLessDTOs;
  }

  public static Set<VisitsIdLess>  toEntitySet(Set<VisitsIDLessDTO> visitsIDLessDTOs){
    Set<VisitsIdLess> visitsLess = new HashSet<>();
    for (VisitsIDLessDTO visitsIDLessDTO:
            visitsIDLessDTOs) {
      VisitsIdLess visitsIdLess = toEntity(visitsIDLessDTO);
      visitsLess.add(visitsIdLess);
    }

    return visitsLess;
  }



}

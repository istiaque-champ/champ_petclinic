package com.team2.prescriptionservice.DataLayer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    @Mappings({
            @Mapping(target="id", ignore = true),
            @Mapping(target = "prescriptionId", ignore = true),
            @Mapping(target = "datePrinted", ignore = true)
    })
    Prescription RequestModelToEntity(PrescriptionRequest prescriptionRequest);

    PrescriptionResponse entityToResponseModel(Prescription prescription);

    List<PrescriptionResponse> entityListToResponseModelList(List<Prescription> prescriptions);
}

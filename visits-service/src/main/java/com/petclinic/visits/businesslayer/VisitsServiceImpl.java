package com.petclinic.visits.businesslayer;

import com.petclinic.visits.datalayer.Visit;
import com.petclinic.visits.datalayer.VisitDTO;
import com.petclinic.visits.datalayer.VisitIdLessDTO;
import com.petclinic.visits.datalayer.VisitRepository;
import com.petclinic.visits.utils.exceptions.InvalidInputException;
import com.petclinic.visits.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.petclinic.visits.utils.EntityDTOUtil;


import java.util.*;
import java.util.stream.Collectors;

/*
 * This class implements the necessary methods to make our service work. It is currently responsible for the logic
 * of basic CRUD operations.
 *
 * Contributors:
 *   70963776+cjayneb@users.noreply.github.com
 */

@Service
@Slf4j
public class VisitsServiceImpl implements VisitsService {

    private final VisitRepository visitRepository;

    //    private final VisitMapper mapper;
    public static final HashMap<Integer, String> visitMap= setUpVisit();

    public VisitsServiceImpl(VisitRepository repo){
        this.visitRepository = repo;
    }

    private static HashMap<Integer, String> setUpVisit(){
        HashMap<Integer, String> visitMap = new HashMap<>();
        return visitMap;
    }


    @Override
    public Mono<VisitDTO> addVisit(Mono<VisitIdLessDTO> visit) {

        if(visit.getDescription() == null || visit.getDescription().isEmpty()){
            throw new InvalidInputException("Visit description required.");
        }
        try{
            Visit visitEntity = EntityDTOUtil.dtoToEntity(visit);
            log.info("Calling visit repo to create a visit for pet with petId: {}", visit.getPetId());
            Visit createdEntity = visitRepository.save(visitEntity);
            return createdEntity
                    .map();
        }
        catch(DuplicateKeyException dke){
            throw new InvalidInputException("Duplicate visitId.", dke);
        }
    }

    @Override
    public Flux<VisitDTO> getVisitsForPet(int petId) {

        if(petId < 0)
            throw new InvalidInputException("PetId can't be negative.");

    }

    @Override
    public Flux<VisitDTO> getVisitsForPet(int petId, boolean scheduled) {
        return null;
    }

    @Override
    public Mono<VisitDTO> getVisitByVisitId(String visitId) {
        return null;
    }

    @Override
    public void deleteVisit(String visitId) {

    }

    @Override
    public Mono<VisitDTO> updateVisit(Mono<VisitDTO> visit) {
        return null;
    }

    @Override
    public Flux<VisitDTO> getVisitsForPets(List<Integer> petIds) {
        return null;
    }

    @Override
    public Flux<VisitDTO> getVisitsForPractitioner(int practitionerId) {
        return null;
    }

    @Override
    public Flux<VisitDTO> getVisitsByPractitionerIdAndMonth(int practitionerId, Date startDate, Date EndDate) {
        return null;
    }

    @Override
    public boolean validateVisitId(String visitId) {
        return false;
    }
}

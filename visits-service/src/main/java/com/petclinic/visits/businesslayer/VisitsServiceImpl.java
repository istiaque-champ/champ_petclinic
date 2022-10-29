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

//logic behind adding a visit, we are currently having problems trying to figure out how to add it propery
    @Override
    public Mono<VisitDTO> addVisit(Mono<VisitIdLessDTO> visit) {

        if(visit.getDescription() == null || visit.getDescription().isEmpty()){
            throw new InvalidInputException("Visit description required.");
        }
        try{
            Mono<Visit> visitEntity = EntityDTOUtil.dtoToEntity(visit);
            log.info("Calling visit repo to create a visit for pet with petId: {}", visit.getPetId());
            Mono<Mono<Visit>> createdEntity = visitRepository.save(visitEntity);
            return createdEntity
                    .map();
        }
        catch(DuplicateKeyException dke){
            throw new InvalidInputException("Duplicate visitId.", dke);
        }
    }
//logic behind get visits for pet
    @Override
    public Flux<VisitDTO> getVisitsForPet(int petId) {

        if(petId < 0)
            throw new InvalidInputException("PetId can't be negative.");

        log.info("Calling visit repo to get visits for pet with petId: {}", petId);
        Flux<Visit> returnedVisits = (Flux<Visit>) visitRepository.findByPetId(petId);
        //having trouble understanding the use for stream
        Flux<VisitDTO> visitDTOList = returnedVisits.stream()
                .filter(v -> v != null)
                .map(EntityDTOUtil::entityToDTO)
                .collect(Collectors.toList());
        return visitDTOList;
    }
//logic behind the get visits for pet, the get date and stream arent working
    @Override
    public Flux<VisitDTO> getVisitsForPet(int petId, boolean scheduled) {
        Date now = new Date(System.currentTimeMillis());
        log.debug("Fetching the visits for pet with petId: {}", petId);
        Flux<VisitDTO> visitsForPet = getVisitsForPet(petId);

        if(scheduled){
            log.debug("Filtering out visits before {}", now);
            visitsForPet = visitsForPet.stream().filter(v -> v.getDate().after(now)).collect(Collectors.toList());
        }
        else{
            log.debug("Filtering out visits after {}", now);
            visitsForPet = visitsForPet.stream().filter(v -> v.getDate().before(now)).collect(Collectors.toList());
        }
        return visitsForPet;
    }
//logic behind the getvisitbyvisit id, having issue with the .get dependency
    @Override
    public VisitDTO getVisitByVisitId(String visitId) {
        if (!validateVisitId(visitId))
            throw new InvalidInputException("VisitId not in the right format.");

        Mono<Visit> returnedVisit = visitRepository.findByVisitId(UUID.fromString(visitId));

        if(returnedVisit.get.getDescription() == null)
            throw new NotFoundException("Visit with visitId: " + visitId + " does not exist.");

        VisitDTO visitDTO = EntityDTOUtil.entityToDTO(returnedVisit);

        return visitDTO;
    }
//the delete, pretty straight foward.
    @Override
    public void deleteVisit(String visitId) {
        log.debug("Visit object is deleted with this id: " + visitId);
        Visit visit = visitRepository.findByVisitId(UUID.fromString(visitId)).orElse(new Visit());
        if(visit.getVisitId() != null)
            visitRepository.delete(visit);

        log.debug("Visit deleted");

    }
//update logic, you are basically getting the id from the request and modifying its fields then returning a updated object of type visit.
    @Override
    public Mono<VisitDTO> updateVisit(Mono<VisitDTO> visit) {
        Mono<Visit> visitEntity = EntityDTOUtil.dtoToEntity(visit);
        Optional<Visit> entity = visitRepository.findByVisitId(UUID.fromString(visit.getVisitId()));
        visitEntity.setId(entity.get().getId());
        log.info("Updating visit with petId: {} and visitId: {}", visit.getPetId(), visit.getVisitId());
        Mono<Mono<Visit>> updatedVisitEntity = visitRepository.save(visitEntity);
        return mapper.entityToModel(updatedVisitEntity);
    }
//get visits for pets logic
    @Override
    public Flux<VisitDTO> getVisitsForPets(List<Integer> petIds) {
        List<Visit> returnedVisits = (List<Visit>) visitRepository.findByPetIdIn(petIds);
        List<VisitDTO> visitDTOList = returnedVisits.stream()
                .filter(v -> v != null)
                .map(visit -> mapper.entityToModel(visit))
                .collect(Collectors.toList());
        return (Flux<VisitDTO>) visitDTOList;
    }
//get vistsi for a practitioner
    @Override
    public Flux<VisitDTO> getVisitsForPractitioner(int practitionerId) {
        if(practitionerId < 0)
            throw new InvalidInputException("PractitionerId can't be negative.");
        List<Visit> returnedVisits = (List<Visit>) visitRepository.findVisitsByPractitionerId(practitionerId);
        List<VisitDTO> visitDTOList = returnedVisits.stream()
                .filter(v -> v != null)
                .map(visit -> mapper.entityToModel(visit))
                .collect(Collectors.toList());
        return (Flux<VisitDTO>) visitDTOList;
    }
//get  getVisitsByPractitionerIdAndMonth, we are checking if the id is negative and then continuing from there
    @Override
    public Flux<VisitDTO> getVisitsByPractitionerIdAndMonth(int practitionerId, Date startDate, Date EndDate) {

        List<Visit> returnedVisits = visitRepository.findAllByDateBetween(startDate, endDate);

        if(practitionerId < 0)
            throw new InvalidInputException("PractitionerId can't be negative.");

        returnedVisits = returnedVisits.stream().filter(v -> v.getPractitionerId() == practitionerId).collect(Collectors.toList());

        List<VisitDTO> visitDTOList = returnedVisits.stream()
                .filter(v -> v != null)
                .map(visit -> mapper.entityToModel(visit))
                .collect(Collectors.toList());

        return (Flux<VisitDTO>) visitDTOList;
    }

    @Override
    public boolean validateVisitId(String visitId) {
        String uuidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
        if(visitId.matches(uuidRegex))
            return true;
        return false;
    }
}

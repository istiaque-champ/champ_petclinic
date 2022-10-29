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
/*
   private final VisitMapper mapper;

    public VisitsServiceImpl(VisitRepository repo, VisitMapper mapper){
        this.visitRepository = repo;
        this.mapper = mapper;
    }*/
    public static final HashMap<Integer, String> visitMap= setUpVisit();
    public VisitsServiceImpl(VisitRepository repo){
        this.visitRepository = repo;
    }

    private static HashMap<Integer, String> setUpVisit(){
        HashMap<Integer, String> visitMap = new HashMap<>();
        return visitMap;
    }


}

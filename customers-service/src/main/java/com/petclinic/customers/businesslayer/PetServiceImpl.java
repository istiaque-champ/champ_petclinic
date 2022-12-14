package com.petclinic.customers.businesslayer;

import com.petclinic.customers.customerExceptions.exceptions.NotFoundException;
import com.petclinic.customers.datalayer.Owner;
import com.petclinic.customers.datalayer.Pet;
import com.petclinic.customers.datalayer.PetRepository;
import com.petclinic.customers.datalayer.PetType;
import com.petclinic.customers.presentationlayer.PetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {

    private static final Logger LOG = LoggerFactory.getLogger(PetServiceImpl.class);

    private final PetRepository petRepository;

    private final OwnerService ownerService;

    public PetServiceImpl(PetRepository petRepository, OwnerService ownerService) {
        this.petRepository = petRepository;
        this.ownerService = ownerService;
    }

    @Override
    public Optional<Pet> findByPetId(int ownerId, int petId) {
        try {
            Optional<Owner> optionalOwner = ownerService.findByOwnerId(ownerId);
            Owner owner = optionalOwner.get();
            //Search pet in database with the owner
            Optional<Pet> pet = petRepository.findPetByOwner(owner, petId);
            LOG.debug("Pet with ID: " + petId + " has been found");
            return pet;
        }
        catch (Exception e)
        {
            // Exception if pet not found
            LOG.debug(e.getMessage());
            throw new NotFoundException("Pet with ID: " + petId + " not found!");
        }
    }

    @Override
    public List<Pet> findAll(int ownerId) {

        Optional<Owner> optionalOwner = ownerService.findByOwnerId(ownerId);
        Owner owner = optionalOwner.get();
        return petRepository.findAllPetByOwner(owner);
    }

    @Override
    public Pet updatePet(int id, Pet newPet) {
        try{
            Optional<Pet> optionalPet = petRepository.findById(id);
            Pet foundPet = optionalPet.get();
            foundPet.setName(newPet.getName());
            foundPet.setBirthDate(newPet.getBirthDate());
            foundPet.setType(newPet.getType());
            foundPet.setOwner(newPet.getOwner());
            LOG.debug("updatePet: Pet with id {} updated",id);
            return petRepository.save(foundPet);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new NotFoundException("updatePet failed, Pet with id: " + id + " not found.");
        }
    }





    @Override
    public Pet updatePet(int id, PetRequest newPet) {
        try{
            Optional<Pet> optionalPet = petRepository.findById(id);
            Pet foundPet = optionalPet.get();
            foundPet.setName(newPet.getName());
            foundPet.setBirthDate(newPet.getBirthDate());
            foundPet.setType(newPet.getType());
            foundPet.setNotes(newPet.getNotes());
            LOG.debug("updatePet: Pet with id {} updated",id);
            return petRepository.save(foundPet);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw new NotFoundException("updatePet failed, Pet with id: " + id + " not found.");
        }
    }





    @Override
    public Pet CreatePet(PetRequest petRequest, int ownerId)
    {
        try {
            Pet pet = new Pet();
            //Owner owner = ownerService.findByOwnerId(ownerId).orElseThrow(() -> new NotFoundException("Owner "+ ownerId +" not found"));
            Optional<Owner> ownerOpt = ownerService.findByOwnerId(ownerId);
            Owner owner = ownerOpt.get();
            owner.addPet(pet);

            pet.setName(petRequest.getName());
            pet.setBirthDate(petRequest.getBirthDate());
            pet.setType(petRequest.getType());
            pet.setNotes(petRequest.getNotes());
            LOG.debug("New pet has been saved! The pet name is: " + pet.getName());
            return petRepository.save(pet);
        }
        catch (Exception e)
        {
            throw new NotFoundException("Owner with ID : " + ownerId+ " is not found");
        }
    }

    @Override
    public void deletePet(int petId, int ownerId) {

        try{
            Optional<Owner> ownerOpt = ownerService.findByOwnerId(ownerId);
            Owner owner = ownerOpt.get();
            Optional<Pet> petOpt = findByPetId(ownerId, petId);
            Pet pet = petOpt.get();
            owner.removePet(pet);

            //Now, owner is safe from deletion
            pet.setOwner(null);
            petRepository.save(pet);
            petRepository.delete(pet);
            LOG.debug("Pet with ID: " + petId + " has been deleted successfully.");
        }
        catch (Exception ex)
        {
            throw new NotFoundException("Owner or pet is not valid. Please standby for assistance. A specialized support team will shortly make contact with you.");
        }
    }

    @Override
    public List<PetType> getAllPetTypes() {
        return petRepository.findPetTypes();
    }

}

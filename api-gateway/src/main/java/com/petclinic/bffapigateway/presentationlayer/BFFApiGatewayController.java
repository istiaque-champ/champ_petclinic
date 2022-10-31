package com.petclinic.bffapigateway.presentationlayer;


import com.petclinic.bffapigateway.domainclientlayer.*;
import com.petclinic.bffapigateway.dtos.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author Maciej Szarlinski
 * @author Istiaque MD Shariar
 * Copied from https://github.com/spring-petclinic/spring-petclinic-microservices
 * Modified to remove circuitbreaker
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/gateway")
public class BFFApiGatewayController {

    private final CustomersServiceClient customersServiceClient;

    private final VisitsServiceClient visitsServiceClient;

    private final VetsServiceClient vetsServiceClient;

    private final AuthServiceClient authServiceClient;

    private final BillServiceClient billServiceClient;

    private final PrescriptionServiceClient prescriptionServiceClient;


    @GetMapping(value = "bills/{billId}")
    public Mono<BillDetailsExpanded> getBillingInfo(final @PathVariable int billId)
    {
        return billServiceClient.getBilling(billId)
                .flatMap(billDetails ->
                        customersServiceClient.getOwner(billDetails.getCustomerId())
                                .map(addOwnersToBillDetails(billDetails))
                );
    }


    @PostMapping(value = "bills",
            consumes = "application/json",
            produces = "application/json")
    public Mono<BillDetailsExpanded> createBill(@RequestBody BillDetails model) {
        return billServiceClient.createBill(model)
                .flatMap(billDetails ->
                    customersServiceClient.getOwner(billDetails.getCustomerId())
                            .map(addOwnersToBillDetails(billDetails))
                );
    }

    @PutMapping(value = "bills/{billId}", produces = "application/json", consumes = "application/json")
    public Mono<BillDetailsExpanded> updateBill(@RequestBody BillDetails bill, @PathVariable int billId){
        return billServiceClient.editBill(billId, bill)
                .flatMap(billDetails ->
                        customersServiceClient.getOwner(billDetails.getCustomerId())
                                .map(addOwnersToBillDetails(billDetails))
                );
    }

    @GetMapping(value = "bills")
    public Flux<BillDetailsExpanded> getAllBilling() {
        return billServiceClient.getAllBilling()
                .flatMap(billDetails ->
                        customersServiceClient.getOwner(billDetails.getCustomerId())
                                .map(addOwnersToBillDetails(billDetails))
                );
    }

    @GetMapping(value = "/bills/vets/{vetId}")
    public Flux<BillDetailsExpanded> getBillsByVetId(final @PathVariable int vetId){
        return billServiceClient.getBillsByVetId(vetId)
                .flatMap(billDetails ->
                        customersServiceClient.getOwner(billDetails.getCustomerId())
                                .map(addOwnersToBillDetails(billDetails))
                );
    }

    @GetMapping(value = "/bills/pets/{petId}")
    public Flux<BillDetailsExpanded> getBillsByPetId(final @PathVariable int petId){
        return billServiceClient.getBillsByPetId(petId)
                .flatMap(billDetails ->
                        customersServiceClient.getOwner(billDetails.getCustomerId())
                                .map(addOwnersToBillDetails(billDetails))
                );
    }
    
    @GetMapping(value = "/bills/customers/{customerId}")
    public Flux<BillDetailsExpanded> getBillsByCustomerId(final @PathVariable int customerId){
        return billServiceClient.getBillsByCustomerId(customerId)
                .flatMap(billDetails ->
                        customersServiceClient.getOwner(billDetails.getCustomerId())
                                .map(addOwnersToBillDetails(billDetails))
                );
    }

    @GetMapping(value = "/bills/visits/{visitId}")
    public Flux<BillDetailsExpanded> getBillsByVisitId(final @PathVariable int visitId){
        return billServiceClient.getBillByVisitId(visitId)
                .flatMap(billDetails ->
                        customersServiceClient.getOwner(billDetails.getCustomerId())
                                .map(addOwnersToBillDetails(billDetails))
                );
    }
    
    @DeleteMapping(value = "bills/{billId}")
    public Mono<Void> deleteBill(final @PathVariable int billId){
        return billServiceClient.deleteBill(billId);
    }

    private Function<OwnerDetails, BillDetailsExpanded> addOwnersToBillDetails(BillDetailsExpanded billDetailsExpanded){
        return owner -> {
            billDetailsExpanded.setOwnerDetails(owner);
            return billDetailsExpanded;
        };
    }

    @PostMapping(value = "owners/{ownerId}/pets" , produces = "application/json", consumes = "application/json")
    public Mono<PetDetails> createPet(@RequestBody PetDetails pet, @PathVariable int ownerId){
        return customersServiceClient.createPet(pet, ownerId);
    }

    @PutMapping(value = "owners/{ownerId}/pets/{petId}" , produces = "application/json", consumes = "application/json")
    public Mono<PetDetails> updatePet(@RequestBody PetDetails pet, @PathVariable int petId, @PathVariable int ownerId){
        return customersServiceClient.updatePet(petId,ownerId, pet);
    }

    @PutMapping(value = "owners/pets/{petId}" , produces = "application/json", consumes = "application/json")
    public Mono<PetDetails> updatePet(@RequestBody PetDetails pet, @PathVariable int petId){
        return customersServiceClient.updatePet(petId, pet);
    }

    @GetMapping(value = "owners/{ownerId}/pets/{petId}")
    public Mono<PetDetails> getPet(@PathVariable int ownerId, @PathVariable int petId){
        return customersServiceClient.getPet(ownerId, petId);
    }

    @DeleteMapping("owners/{ownerId}/pets/{petId}")
    public Mono<PetDetails> deletePet(@PathVariable int ownerId, @PathVariable int petId){
        prescriptionServiceClient.deletePrescriptionByPetId(petId);
        return customersServiceClient.deletePet(ownerId,petId);
    }

    @GetMapping("owners/petTypes")
    public Flux<PetType> getPetTypes(){
        return customersServiceClient.getPetTypes();
    }


    @PutMapping(
            value = "owners/*/pets/{petId}/visits/{visitId}",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<VisitDetails> updateVisit(@RequestBody VisitDetails visit, @PathVariable int petId, @PathVariable String visitId) {
        visit.setPetId(petId);
        visit.setVisitId(visitId);
        return visitsServiceClient.updateVisitForPet(visit);
    }

    @DeleteMapping (value = "visits/{visitId}")
    public Mono<Void> deleteVisitsByVisitId(final @PathVariable String visitId){
        return visitsServiceClient.deleteVisitByVisitId(visitId);
    }

    @GetMapping(value = "visits/{petId}")
    public Flux<VisitDetails> getVisitsForPet(final @PathVariable int petId){
        return visitsServiceClient.getVisitsForPet(petId);
    }

    @GetMapping(value ="visit/{visitId}")
    public Mono<VisitDetails> getVisitByVisitId(final @PathVariable String visitId){
        return visitsServiceClient.getVisitByVisitId(visitId);
    }

    @GetMapping(value = "visits/previous/{petId}")
    public Flux<VisitDetails> getPreviousVisitsForPet(@PathVariable final int petId) {
        return visitsServiceClient.getPreviousVisitsForPet(petId);
    }

    @GetMapping(value = "visits/scheduled/{petId}")
    public Flux<VisitDetails> getScheduledVisitsForPet(@PathVariable final int petId) {
        return visitsServiceClient.getScheduledVisitsForPet(petId);

    }

    @GetMapping(value = "visits/vets/{practitionerId}")
    public Flux<VisitDetails> getVisitForPractitioner(@PathVariable int practitionerId){
        return visitsServiceClient.getVisitForPractitioner(practitionerId);
    }

    @GetMapping(value = "visits/calendar/{practitionerId}")
    public Flux<VisitDetails> getVisitsByPractitionerIdAndMonth(@PathVariable("practitionerId") int practitionerId,
                                                                @RequestParam("dates") List<String> dates) {
        String startDate = dates.get(0);
        String endDate = dates.get(1);

        return visitsServiceClient.getVisitsByPractitionerIdAndMonth(practitionerId, startDate, endDate);
    }

    private Function<Visits, OwnerDetails> addVisitsToOwner(OwnerDetails owner) {
        return visits -> {
            owner.getPets()
                    .forEach(pet -> pet.getVisits()
                            .addAll(visits.getItems().stream()
                                    .filter(v -> v.getPetId() == pet.getId())
                                    .collect(Collectors.toList()))
                    );
            return owner;
        };
    }

    @PostMapping(
            value = "visit/owners/{ownerId}/pets/{petId}/visits",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<VisitDetails> addVisit(@RequestBody VisitDetails visit, @PathVariable String ownerId, @PathVariable String petId) {
        visit.setPetId(Integer.parseInt(petId));
        return visitsServiceClient.createVisitForPet(visit);
    }

    /**
     * Retrieve all vets from DB
     */
    @GetMapping(value = "vets")
    public Flux<VetDetails> getVets() {
        return vetsServiceClient.getVets();
    }

    /**
     * Get a single vet given its vetID
     */
    @GetMapping(value = "vets/{vetId}")
    public Mono<VetDetails> getVet(final @PathVariable int vetId) {
        return vetsServiceClient.getVet(vetId);
    }

    /**
     * Create Vet
     */
    @PostMapping(value = "vets",
            consumes = "application/json",
            produces = "application/json")
    public Mono<VetDetails> createVet(@RequestBody VetDetails model) {
        return vetsServiceClient.createVet(model);
    }

    /**
     * Delete vet from DB given the vetID
     */
    @DeleteMapping(value = "vets/{vetId}")
    public Mono<VetDetails> deleteVet(final @PathVariable long vetId) {
        return vetsServiceClient.deleteVet(vetId);
    }

    /**
     * Update vet details
     */
    @PutMapping(
            value = "vets/{vetId}",
            consumes = "application/json",
            produces = "application/json"
    )
    public Mono<VetDetails> updateVet(@PathVariable int vetId, @RequestBody VetDetails vet) {
        log.debug("Trying to update vet");
        return vetsServiceClient.updateVet(vetId, vet);
    }

    @PostMapping(value = "users",
            consumes = "application/json",
            produces = "application/json")
    public Mono<UserDetails> createUser(@RequestBody Register model) {
        return authServiceClient.createUser(model);
    }

    @DeleteMapping(value = "users/{userId}")
    public Mono<UserDetails> deleteUser(final @PathVariable long userId) {
        return authServiceClient.deleteUser(userId);
    }

    @GetMapping(value = "users/{userId}")
    public Mono<UserDetails> getUserDetails(final @PathVariable long userId) {
        return authServiceClient.getUser(userId);
    }
    @GetMapping(value = "users")
    public Flux<UserDetails> getAll() {
        return authServiceClient.getUsers();
    }

    @PutMapping(value = "users/{userId}",
            consumes = "application/json",
            produces = "application/json")
    public Mono<UserDetails> updateUser(final @PathVariable long userId, @RequestBody Register model) {
        return authServiceClient.updateUser(userId, model);
    }

    @GetMapping(value = "admin/roles")
    public Flux<Role> getRoles() {
        return authServiceClient.getRoles();
    }

    @DeleteMapping(value = "admin/roles/{id}")
    public Mono<Void> deleteRole(@PathVariable int id) {
        return authServiceClient.deleteRole(id);
    }

    @PostMapping(value = "admin/roles")
    public Mono<Role> addRole(@RequestBody final Role model) {
        return authServiceClient.addRole(model);
    }


    /**
     * Owners Methods
     * **/

    @GetMapping(value = "owners")
    public Flux<OwnerDetails> getOwners() {
        return customersServiceClient.getOwners()
                .flatMap(n ->
                        visitsServiceClient.getVisitsForPets(n.getPetIds())
                                .map(addVisitsToOwner(n))
                );
    }

    @GetMapping(value = "owners/{ownerId}")
    public Mono<OwnerDetails> getOwnerDetails(final @PathVariable int ownerId) {
        return customersServiceClient.getOwner(ownerId)
                .flatMap(owner ->
                        visitsServiceClient.getVisitsForPets(owner.getPetIds())
                                .map(addVisitsToOwner(owner))
                );
    }

    @PostMapping(value = "owners",
            consumes = "application/json",
            produces = "application/json")
    public Mono<OwnerDetails> createOwner(@RequestBody OwnerDetails model){
        return customersServiceClient.createOwner(model);
    }

    @PutMapping(value = "owners/{ownerId}",consumes = "application/json" ,produces = "application/json")
    public Mono<OwnerDetails> updateOwnerDetails(@PathVariable int ownerId, @RequestBody OwnerDetails od) {
        return customersServiceClient.updateOwner(ownerId, od)
                .flatMap(owner ->
                        visitsServiceClient.getVisitsForPets(owner.getPetIds())
                                .map(addVisitsToOwner(owner)));
    }

    @DeleteMapping(value = "owners/{ownerId}")
    public Mono<OwnerDetails> deleteOwner(@PathVariable int ownerId){
        return customersServiceClient.deleteOwner(ownerId);
    }

    /**
     * End of Owner Methods
     * **/

    //Prescription Methods
    @GetMapping(value = "owners/{ownerId}/pets/{petId}/prescriptions/{prescriptionId}")
    public Mono<PrescriptionDetails> getPrescriptionDetails(@PathVariable int prescriptionId, @PathVariable int ownerId, @PathVariable int petId) {

        return prescriptionServiceClient.getPrescription(prescriptionId);
    }

    @GetMapping(value = "owners/{ownerId}/pets/{petId}/prescriptions")
    public Flux<PrescriptionDetails> getAllPrescriptionDetails(@PathVariable Integer petId) {
        return prescriptionServiceClient.getPrescriptions(petId);
    }
    @PostMapping(value = "owners/{ownerId}/pets/{petId}/prescriptions",
            consumes = "application/json",
            produces = "application/json")
    public Mono<PrescriptionDetails> createPrescription(@PathVariable Integer petId,@RequestBody PrescriptionDetails model){
        model.setPetId(petId);
        return prescriptionServiceClient.createPrescription(model);
    }
    @PutMapping(value = "owners/{ownerId}/pets/{petId}/prescriptions/{prescriptionId}",
            consumes = "application/json",
            produces = "application/json")
    public Mono<PrescriptionDetails> updatePrescription(@PathVariable Integer prescriptionId,@RequestBody PrescriptionDetails model){
        return prescriptionServiceClient.updatePrescription(prescriptionId,model);
    }

    @DeleteMapping(value = "owners/{ownerId}/pets/{petId}/prescriptions/{prescriptionId}")
    public Mono<PrescriptionDetails> deletePrescriptionByPrescriptionId(@PathVariable Integer prescriptionId){
        return prescriptionServiceClient.deletePrescriptionByPrescriptionId(prescriptionId);
    }
    @DeleteMapping(value = "owners/{ownerId}/pets/{petId}/prescriptions")
    public Mono<PrescriptionDetails> deletePrescriptionByPetId(@PathVariable Integer petId){
        return prescriptionServiceClient.deletePrescriptionByPetId(petId);
    }
    //End of Prescription Methods

    @GetMapping("/verification/{token}")
    public Mono<UserDetails> verifyUser(@PathVariable final String token) {
        return authServiceClient.verifyUser(token);
    }

    @PostMapping("/users/login")
    public Mono<ResponseEntity<UserDetails>> login(@RequestBody final Login login) {
        return authServiceClient.login(login)
                .map(n -> ResponseEntity.ok()
                        .header(AUTHORIZATION, n.getT1())
                        .body(n.getT2())
                );
    }
}
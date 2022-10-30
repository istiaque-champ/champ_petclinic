'use strict';

angular.module('visitsVetList')
    .controller('VisitsVetListController', ['$http', '$state', '$stateParams', '$filter', function ($http, $state, $stateParams, $filter) {
        var self = this;
        //var petId = $stateParams.petId || 0;
        var practitionerId = $stateParams.practitionerId || 0;
        // var postURL = "api/gateway/visit/owners/" + ($stateParams.ownerId || 0) + "/pets/" + petId + "/visits";
        var postURL = "api/gateway/visits/vets/" + ($stateParams.practitionerId || 0);
        var vetsUrl = "api/gateway/vets";
        // var billsUrl = "api/gateway/bill";
        var visitId = 0;
        // self.practitionerId = 0;
        self.date = new Date();
        self.desc = "";

        // GET LIST OF VETS/PRACTITIONERS
        $http.get(vetsUrl).then(function (resp) {
            self.vets = resp.data;
        });

        //MIGHT NEED TO EDIT SO IT'S NOT RUN ON GET
        //Fetch visits according to vet id
        // $http.get("api/gateway/visits/vets/"+practitionerId).then(function (resp) {
        //     self.visits = resp.data;
        //     self.sortFetchedVisits();
        // });

        // GET AND ASSIGN VET/PRACTITIONER INFORMATION
        self.loadVetInfo = function() {
            let selectedVetsId = $("#selectedVet").val();

            let foundVet = false;
            let vetPhoneNumber = "";
            let vetEmailAddress = "";
            let vetWorkdays = "";
            let vetSpecialtiesObject = null;

            $.each(self.vets, function(i, vet) {
                if(selectedVetsId == vet.vetId) {
                    foundVet = true;
                    vetPhoneNumber = vet.phoneNumber;
                    vetEmailAddress = vet.email;
                    vetSpecialtiesObject = vet.specialties;
                    vetWorkdays = vet.workday;

                    return false;
                }
            });

            let vetSpecialties = "";
            $.each(vetSpecialtiesObject, function(i, specialty) {
                if(i < vetSpecialtiesObject.length - 1) {
                    vetSpecialties += specialty.name + ", ";
                } else {
                    vetSpecialties += specialty.name;
                }
            });

            if(foundVet) {
                $("#vetPhoneNumber").val(vetPhoneNumber);
                $("#vetEmailAddress").val(vetEmailAddress);
                $("#vetSpecialties").val(vetSpecialties);
                $("#vetWorkdays").val(vetWorkdays).trigger("change");
            } else {
                $("#vetPhoneNumber").val("");
                $("#vetEmailAddress").val("");
                $("#vetSpecialties").val("");
                $("#vetWorkdays").val("");
            }
        }

        // GET PRACTITIONER NAME WITH ID
        self.getPractitionerName = function (id){
            var practitionerName = "";
            $.each(self.vets, function (i, vet){
                if (vet.vetId == id){
                    practitionerName = vet.firstName + " " + vet.lastName;
                    return false;
                }
            });
            return practitionerName;
        };

        // GET OWNER NAME WITH ID
        self.getOwnerName = function (id){
            var ownerName = "";
            $.each(self.owner, function (i, owner){
                if (owner.ownerId == id){
                    ownerName = owner.firstName + " " + owner.lastName;
                    return false;
                }
            });
            return ownerName;
        };

        // Function to get the current date
        function getCurrentDate() {
            let dateObj = new Date();
            var dd = String(dateObj.getDate()).padStart(2, '0');
            var mm = String(dateObj.getMonth() + 1).padStart(2, '0');
            var yyyy = dateObj.getFullYear();
            return Date.parse(yyyy + '-' + mm + '-' + dd);
        }

        // Function to parse the visit date into a comparable format
        function parseDate(date) {
            var dd = String(date.getDate()).padStart(2, '0');
            var mm = String(date.getMonth() + 1).padStart(2, '0');
            var yyyy = date.getFullYear();
            return Date.parse(yyyy + '-' + mm + '-' + dd);
        }

        // Lists holding visits for the table to display
        self.upcomingVisits = [];
        self.previousVisits = [];

        self.sortFetchedVisits = function() {
            let currentDate = getCurrentDate();

            $.each(self.visits, function(i, visit) {
                let selectedVisitDate = Date.parse(visit.date);

                if(selectedVisitDate >= currentDate) {
                    self.upcomingVisits.push(visit);
                } else {
                    self.previousVisits.push(visit);
                }
            });
        }

        // MUST EDIT TO GET ALL VISITS WITH THE PRACTITIONER ID
        self.getVisitsForPractitionerId = function() {
            let pId = localStorage.getItem("practitionerId");

            if(pId != null && pId !== "") {
                let info = pId;

                if(info !== undefined){
                    console.log(info.toString());
                    let practitionerId = parseInt(info);
                    let startDate = info[1];
                    let endDate = info[2];

                    if(!isNaN(practitionerId)) {
                        $http.get("api/gateway/visits/vets/" + practitionerId).then(function (resp) {
                            //getting current date for comparison
                            let currentDate = getCurrentDate();

                            // Add the visit to one of the lists depending on its date
                            let isForUpcomingVisitsTable = Date.parse(resp.data.date) >= currentDate;
                            if (isForUpcomingVisitsTable) {
                                self.upcomingVisits.push(resp.data);
                            } else {
                                self.previousVisits.push(resp.data);
                            }

                            // Call the last sort after adding if there is one
                            callLastSort(isForUpcomingVisitsTable);
                        });
                    }
                }
            }
        }



        // No confirmation needed since data should only be viewable

        // self.showConfirmationModal = function(e, visitId = 0, status = 0, practitionerId = 0, date = null, description = "") {
        //     // Get the name of button sender
        //     let buttonText = $(e.target).text();
        //
        //     // Check if form needs to be valid for adding and updating a visit
        //     if(buttonText === "Add New Visit" || buttonText === "Update Visit") {
        //         var form = document.querySelector('form');
        //
        //         // If form isn't valid then don't display confirmation popup and alert required fields
        //         if(!form.reportValidity()) {
        //             return false;
        //         }
        //     }
        //
        //     // Set modal's title and body to match the sender's request
        //     $('#confirmationModalTitle').text(buttonText);
        //     $('#confirmationModalBody').text("Are you sure you want to " + buttonText.toLowerCase() + "?");
        //
        //     // The confirm button on the popup modal
        //     let modalConfirmButton = $('#confirmationModalConfirmButton');
        //
        //     // Check if the sender was the Add New Visit Button
        //     if(buttonText !== "Add New Visit") {
        //         // Set the targeted visit data attribute to the visit's id
        //         modalConfirmButton.data("targetVisit", visitId);
        //
        //         // Set other data attributes if the button is for cancel
        //         if(buttonText.toLowerCase().includes("cancel")) {
        //             modalConfirmButton.data("targetStatus", status);
        //             modalConfirmButton.data("targetPractitionerId", practitionerId);
        //             modalConfirmButton.data("targetDate", date);
        //             modalConfirmButton.data("targetDescription", description);
        //             modalConfirmButton.data("cancel-index", $(e.target).closest("tr").data("index"));
        //         }
        //
        //         if(buttonText.toLowerCase() === "delete visit") {
        //             modalConfirmButton.data("delete-index", $(e.target).closest("tr").data("index"));
        //             modalConfirmButton.data("delete-table-name", $(e.target).closest("tr").data("table-name"));
        //         }
        //     }
        //
        //     // Show the modal
        //     $('#confirmationModal').modal('show');
        // }

        // Form no longer has button, simply on change
        // self.completeFormAction = function() {
        //     // Check which button modal was called by and perform appropriate action
        //     let modalTitle = $('#confirmationModalTitle').text();
        //     let modalButton = $('#confirmationModalConfirmButton');
        //
        //     if(modalTitle === $('#submit_button').text()) {
        //         $('#visitForm').submit();
        //
        //         if(modalTitle.toLowerCase() === "update visit") {
        //             self.resetForm();
        //         }
        //     }
        //     else if(modalTitle.toLowerCase() === "delete visit") {
        //         self.deleteVisit(modalButton.data("targetVisit"));
        //     }
        //     else if(modalTitle.toLowerCase().includes("cancel")) {
        //         self.cancelVisit(modalButton.data("targetVisit"), modalButton.data("targetStatus"), modalButton.data("targetPractitionerId"), modalButton.data("targetDate"), modalButton.data("targetDescription"));
        //     }
        //
        //     // Hide modal after performing action
        //     $('#confirmationModal').modal('hide');
        // }

        // self.switchToUpdateForm = function (e, practitionerId, date, description, id, visitStatus){
        //     visitId = id;
        //     $("#selectedVet option[value='"+practitionerId+"']").prop("selected", true);
        //     $('#date_input').val(date);
        //     console.log(date);
        //     $('#description_textarea').val(description);
        //     //not needed
        //     // $('#submit_button').text("Update Visit");
        //     // $('#cancel_button').css("visibility", "visible");
        //
        //     let d = date.toString().split("-");
        //     editDateParsing(d[0], d[1], d[2]);
        //
        //     self.loadVetInfo();
        //
        //     // Save the sender's index to data attribute on visitForm called data-update-index
        //     let modalConfirmButton = $('#confirmationModalConfirmButton');
        //     modalConfirmButton.data("update-table", $(e.target).closest('tr').data("table-name"));
        //     modalConfirmButton.data("update-index", $(e.target).closest('tr').data("index"));
        //
        //     self.submit = function () {
        //         var data = {
        //             date: $('#date_input').val(),
        //             description: $('#description_textarea').val(),
        //             practitionerId: $("#selectedVet").val(),
        //             status: visitStatus
        //         };
        //
        //         // Might not be needed
        //         let putURL = "api/gateway/owners/*/pets/" + petId + "/visits/" + visitId;
        //
        //         $http.put(putURL, data).then(function(response) {
        //             let currentDate = getCurrentDate();
        //             let modalConfirmButton = $('#confirmationModalConfirmButton');
        //
        //             // Get the index of the visit to be updated
        //             let index = parseInt(modalConfirmButton.data("update-index"));
        //
        //             // See if the unedited visit was in upcoming
        //             let outdatedVisitWasInUpcoming = modalConfirmButton.data("update-table") === "upcomingVisits";
        //
        //             // See if the edited visit will be in upcoming or not
        //             let updatedVisitWillBeInUpcoming = Date.parse(response.data.date) >= currentDate;
        //
        //             // Perform an action depending on where the visit was located and where it will be
        //             if(outdatedVisitWasInUpcoming === true && updatedVisitWillBeInUpcoming === true) {
        //                 self.upcomingVisits[index] = response.data;
        //             }
        //             if(outdatedVisitWasInUpcoming === false && updatedVisitWillBeInUpcoming === false) {
        //                 self.previousVisits[index] = response.data;
        //             }
        //             if(outdatedVisitWasInUpcoming === true && updatedVisitWillBeInUpcoming === false) {
        //                 // Remove the old visit from upcoming
        //                 self.upcomingVisits.splice(index, 1);
        //
        //                 // Add the edited visit to previous visits
        //                 self.previousVisits.push(response.data);
        //             }
        //             if(outdatedVisitWasInUpcoming === false && updatedVisitWillBeInUpcoming === true) {
        //                 // Remove the old visit from upcoming
        //                 self.previousVisits.splice(index, 1);
        //
        //                 // Add the edited visit to previous visits
        //                 self.upcomingVisits.push(response.data);
        //             }
        //
        //             // Call the last sort after adding if there is one
        //             callLastSort(updatedVisitWillBeInUpcoming);
        //
        //             createAlert("success", "Successfully updated visit!");
        //         }, function () {
        //             createAlert("danger", "Failed to update visit!");
        //         });
        //     };
        // };

        // This function might not be necessary
        // self.resetForm = function() {
        //     // Reset the Add Visit Form to default functionality
        //     $('#visitForm')[0].reset();
        //
        //     // Restore default button name
        //     $('#submit_button').text("Add New Visit");
        //
        //     // Hide the cancel button
        //     $('#cancel_button').css("visibility", "hidden");
        //
        //     // Restore default functionality of form submit
        //     self.submit = function () {
        //         var data = {
        //             date: getCurrentDate(),
        //             description: self.desc,
        //             practitionerId: self.practitionerId,
        //             status: true
        //         };
        //
        //         var billData = {
        //             ownerId: $stateParams.ownerId,
        //             date: getCurrentDate(),
        //             visitType : $("#selectedVisitType").val()
        //         }
        //
        //         $http.post(postURL, data).then(function(response) {
        //             let currentDate = getCurrentDate();
        //
        //             // Add the visit to one of the lists depending on its date
        //             let isForUpcomingVisitsTable = Date.parse(response.data.date) >= currentDate;
        //             if(isForUpcomingVisitsTable) {
        //                 self.upcomingVisits.push(response.data);
        //             } else {
        //                 self.previousVisits.push(response.data);
        //             }
        //
        //             // Call the last sort after adding if there is one
        //             callLastSort(isForUpcomingVisitsTable);
        //
        //             createAlert("success", "Successfully created visit!");
        //         },function () {
        //             createAlert("danger", "Failed to add visit!");
        //         });
        //
        //         $http.post(billsUrl, billData).then(function () {
        //
        //         }, function () {
        //             console.log("Failed to create corresponding bill!");
        //         });
        //     }
        //
        //     return false;
        // }


        // This value will be set depending on what was last clicked
        let lastSort = "";
        let dateSortName = "Date";
        let descSortName = "Description";
        // let vetSortName = "Veterinarian";
        // Change to owner Sorting
        let ownerSortName = "Owner";
        let statusSortName = "Status";

        // This function will call the last sorted option without changing ascending or descending
        function callLastSort(isForUpcoming) {
            switch (lastSort) {
                case dateSortName:
                    self.SortTableByDate(isForUpcoming, false);
                    break;
                case descSortName:
                    self.SortTableByDesc(isForUpcoming, false);
                    break;
                case ownerSortName:
                    self.SortTableByOwner(isForUpcoming, false);
                    break;
                case statusSortName:
                    self.SortTableByStatus(isForUpcoming, false);
                    break;
            }
        }


        let ResetSortButtonArrows = function(isForUpcoming) {
            if(isForUpcoming) {
                $('#sortByDateButtonUpcomingVisits').text("Sort by date ⇅");
                $('#sortByDescButtonUpcomingVisits').text("Sort by description ⇅");
                // $('#sortByVetButtonUpcomingVisits').text("Sort by veterinarian ⇅");
                $('#sortByOwnerButtonUpcomingVisits').text("Sort by veterinarian ⇅");
                $('#sortByStatusButtonUpcomingVisits').text("Sort by status ⇅");
            } else {
                $('#sortByDateButtonPreviousVisits').text("Sort by date ⇅");
                $('#sortByDescButtonPreviousVisits').text("Sort by description ⇅");
                // $('#sortByVetButtonPreviousVisits').text("Sort by veterinarian ⇅");
                $('#sortByOwnerButtonPreviousVisits').text("Sort by owner ⇅");
                $('#sortByStatusButtonPreviousVisits').text("Sort by status ⇅");
            }
        }

        //Sort by Date
        let sortTableDateAscendingUpcomingVisits = false;
        let sortTableDateAscendingPreviousVisits = false;
        self.SortTableByDate = function(isForUpcoming, flipSortingBool = true) {
            lastSort = dateSortName;
            ResetSortButtonArrows(isForUpcoming);

            if(isForUpcoming) {
                if(flipSortingBool) {
                    sortTableDateAscendingUpcomingVisits = !sortTableDateAscendingUpcomingVisits;
                }

                if(sortTableDateAscendingUpcomingVisits) {
                    self.upcomingVisits.sort(function (a, b) {
                        return Date.parse(b.date) - Date.parse(a.date);
                    });
                    $('#sortByDateButtonUpcomingVisits').text("Sort by date ↓")
                } else {
                    self.upcomingVisits.sort(function (a, b) {
                        return Date.parse(b.date) + Date.parse(a.date);
                    });
                    $('#sortByDateButtonUpcomingVisits').text("Sort by date ↑")
                }
            } else {
                if(flipSortingBool) {
                    sortTableDateAscendingPreviousVisits = !sortTableDateAscendingPreviousVisits;
                }

                if(sortTableDateAscendingPreviousVisits) {
                    self.previousVisits.sort(function (a, b) {
                        return Date.parse(b.date) - Date.parse(a.date);
                    });
                    $('#sortByDateButtonPreviousVisits').text("Sort by date ↓")
                } else {
                    self.previousVisits.sort(function (a, b) {
                        return Date.parse(b.date) + Date.parse(a.date);
                    });
                    $('#sortByDateButtonPreviousVisits').text("Sort by date ↑")
                }
            }
        }

        // Sort by Description
        let sortDescriptionAscendingUpcomingVisits = false;
        let sortDescriptionAscendingPreviousVisits = false;
        self.SortTableByDesc = function(isForUpcoming, flipSortingBool = true) {
            lastSort = descSortName;
            ResetSortButtonArrows(isForUpcoming);

            if(isForUpcoming) {
                if(flipSortingBool) {
                    sortDescriptionAscendingUpcomingVisits = !sortDescriptionAscendingUpcomingVisits;
                }

                if(sortDescriptionAscendingUpcomingVisits) {
                    self.upcomingVisits.sort(function (a, b) {
                        a = a.description.toLowerCase();
                        b = b.description.toLowerCase();

                        return a < b ? -1 : a > b ? 1 : 0;
                    });
                    $('#sortByDescButtonUpcomingVisits').text("Sort by description ↓")
                } else {
                    self.upcomingVisits.sort(function (a, b) {
                        a = a.description.toLowerCase();
                        b = b.description.toLowerCase();

                        return a > b ? -1 : a < b ? 1 : 0;
                    });
                    $('#sortByDescButtonUpcomingVisits').text("Sort by description ↑")
                }
            } else {
                if(flipSortingBool) {
                    sortDescriptionAscendingPreviousVisits = !sortDescriptionAscendingPreviousVisits;
                }

                if(sortDescriptionAscendingPreviousVisits) {
                    self.previousVisits.sort(function (a, b) {
                        a = a.description.toLowerCase();
                        b = b.description.toLowerCase();

                        return a < b ? -1 : a > b ? 1 : 0;
                    });
                    $('#sortByDescButtonPreviousVisits').text("Sort by description ↓")
                } else {
                    self.previousVisits.sort(function (a, b) {
                        a = a.description.toLowerCase();
                        b = b.description.toLowerCase();

                        return a > b ? -1 : a < b ? 1 : 0;
                    });
                    $('#sortByDescButtonPreviousVisits').text("Sort by description ↑")
                }
            }
        }

        // Sort by Owner Name
        let sortOwnerAscendingUpcomingVisits = false;
        let sortOwnerAscendingPreviousVisits = false;
        self.SortTableByOwner = function(isForUpcoming, flipSortingBool = true) {
            lastSort = ownerSortName;
            ResetSortButtonArrows(isForUpcoming);

            if(isForUpcoming) {
                if(flipSortingBool) {
                    sortOwnerAscendingUpcomingVisits = !sortOwnerAscendingUpcomingVisits;
                }

                if(sortVetAscendingUpcomingVisits) {
                    self.upcomingVisits.sort(function (a, b) {
                        a = self.getOwnerName(a.ownerId).toLowerCase();
                        b = self.getOwnerName(b.ownerId).toLowerCase();

                        return a < b ? -1 : a > b ? 1 : 0;
                    });
                    $('#sortByOwnerButtonUpcomingVisits').text("Sort by owner ↓")
                } else {
                    self.upcomingVisits.sort(function (a, b) {
                        a = self.getOwnerName(a.ownerId).toLowerCase();
                        b = self.getOwnerName(b.ownerId).toLowerCase();

                        return a > b ? -1 : a < b ? 1 : 0;
                    });
                    $('#sortByOwnerButtonUpcomingVisits').text("Sort by owner ↑")
                }
            } else {
                if(flipSortingBool) {
                    sortOwnerAscendingPreviousVisits = !sortOwnerAscendingPreviousVisits;
                }

                if(sortOwnerAscendingPreviousVisits) {
                    self.previousVisits.sort(function (a, b) {
                        a = self.getOwnerName(a.ownerId).toLowerCase();
                        b = self.getOwnerName(b.ownerId).toLowerCase();

                        return a < b ? -1 : a > b ? 1 : 0;
                    });
                    $('#sortByOwnerButtonPreviousVisits').text("Sort by owner ↓")
                } else {
                    self.previousVisits.sort(function (a, b) {
                        a = self.getOwnerName(a.ownerId).toLowerCase();
                        b = self.getOwnerName(b.ownerId).toLowerCase();

                        return a > b ? -1 : a < b ? 1 : 0;
                    });
                    $('#sortByOwnerButtonPreviousVisits').text("Sort by owner ↑")
                }
            }
        }

        //Sort by Vet Name
        let sortVetAscendingUpcomingVisits = false;
        let sortVetAscendingPreviousVisits = false;
        self.SortTableByVet = function(isForUpcoming, flipSortingBool = true) {
            lastSort = vetSortName;
            ResetSortButtonArrows(isForUpcoming);

            if(isForUpcoming) {
                if(flipSortingBool) {
                    sortVetAscendingUpcomingVisits = !sortVetAscendingUpcomingVisits;
                }

                if(sortVetAscendingUpcomingVisits) {
                    self.upcomingVisits.sort(function (a, b) {
                        a = self.getPractitionerName(a.practitionerId).toLowerCase();
                        b = self.getPractitionerName(b.practitionerId).toLowerCase();

                        return a < b ? -1 : a > b ? 1 : 0;
                    });
                    $('#sortByVetButtonUpcomingVisits').text("Sort by veterinarian ↓")
                } else {
                    self.upcomingVisits.sort(function (a, b) {
                        a = self.getPractitionerName(a.practitionerId).toLowerCase();
                        b = self.getPractitionerName(b.practitionerId).toLowerCase();

                        return a > b ? -1 : a < b ? 1 : 0;
                    });
                    $('#sortByVetButtonUpcomingVisits').text("Sort by veterinarian ↑")
                }
            } else {
                if(flipSortingBool) {
                    sortVetAscendingPreviousVisits = !sortVetAscendingPreviousVisits;
                }

                if(sortVetAscendingPreviousVisits) {
                    self.previousVisits.sort(function (a, b) {
                        a = self.getPractitionerName(a.practitionerId).toLowerCase();
                        b = self.getPractitionerName(b.practitionerId).toLowerCase();

                        return a < b ? -1 : a > b ? 1 : 0;
                    });
                    $('#sortByVetButtonPreviousVisits').text("Sort by veterinarian ↓")
                } else {
                    self.previousVisits.sort(function (a, b) {
                        a = self.getPractitionerName(a.practitionerId).toLowerCase();
                        b = self.getPractitionerName(b.practitionerId).toLowerCase();

                        return a > b ? -1 : a < b ? 1 : 0;
                    });
                    $('#sortByVetButtonPreviousVisits').text("Sort by veterinarian ↑")
                }
            }
        }

        //Sort by Status
        let sortStatusAscendingUpcomingVisits = false;
        let sortStatusAscendingPreviousVisits = false;
        self.SortTableByStatus = function(isForUpcoming, flipSortingBool = true) {
            lastSort = statusSortName;
            ResetSortButtonArrows(isForUpcoming);

            if(isForUpcoming) {
                if(flipSortingBool) {
                    sortStatusAscendingUpcomingVisits = !sortStatusAscendingUpcomingVisits;
                }

                if(sortStatusAscendingUpcomingVisits) {
                    self.upcomingVisits.sort(function (a, b) {
                        a = self.getStatus(a.status, a.date).toLowerCase();
                        b = self.getStatus(b.status, b.date).toLowerCase();

                        return a < b ? -1 : a > b ? 1 : 0;
                    });
                    $('#sortByStatusButtonUpcomingVisits').text("Sort by status ↓")
                } else {
                    self.upcomingVisits.sort(function (a, b) {
                        a = self.getStatus(a.status, a.date).toLowerCase();
                        b = self.getStatus(b.status, b.date).toLowerCase();

                        return a > b ? -1 : a < b ? 1 : 0;
                    });
                    $('#sortByStatusButtonUpcomingVisits').text("Sort by status ↑")
                }
            } else {
                if(flipSortingBool) {
                    sortStatusAscendingPreviousVisits = !sortStatusAscendingPreviousVisits;
                }

                if(sortStatusAscendingPreviousVisits) {
                    self.previousVisits.sort(function (a, b) {
                        a = self.getStatus(a.status, a.date).toLowerCase();
                        b = self.getStatus(b.status, b.date).toLowerCase();

                        return a < b ? -1 : a > b ? 1 : 0;
                    });
                    $('#sortByStatusButtonPreviousVisits').text("Sort by status ↓")
                } else {
                    self.previousVisits.sort(function (a, b) {
                        a = self.getStatus(a.status, a.date).toLowerCase();
                        b = self.getStatus(b.status, b.date).toLowerCase();

                        return a > b ? -1 : a < b ? 1 : 0;
                    });
                    $('#sortByStatusButtonPreviousVisits').text("Sort by status ↑")
                }
            }
        }
        // END SORT FUNCTIONS

        // Might be necessary
        // self.submit = function () {
        //     var data = {
        //         date: $filter('date')(self.date, "yyyy-MM-dd"),
        //         description: self.desc,
        //         practitionerId: self.practitionerId,
        //         status: true
        //     };
        //
        //     var billData = {
        //         ownerId: $stateParams.ownerId,
        //         date: $filter('date')(self.date, "yyyy-MM-dd"),
        //         visitType : $("#selectedVisitType").val()
        //     }
        //
        //     $http.post(postURL, data).then(function(response) {
        //         let currentDate = getCurrentDate();
        //
        //         // Add the visit to one of the lists depending on its date
        //         let isForUpcomingVisitsTable = Date.parse(response.data.date) >= currentDate;
        //         if(isForUpcomingVisitsTable) {
        //             self.upcomingVisits.push(response.data);
        //         } else {
        //             self.previousVisits.push(response.data);
        //         }
        //
        //         // Call the last sort after adding if there is one
        //         callLastSort(isForUpcomingVisitsTable);
        //
        //         createAlert("success", "Successfully created visit!");
        //     },function () {
        //         createAlert("danger", "Failed to add visit!");
        //     });
        //
        //     $http.post(billsUrl, billData).then(function () {
        //
        //     }, function () {
        //         console.log("Failed to create corresponding bill!");
        //     });
        // }

        self.getStatus = function (status, date) {
            //Initializing variable for status
            var statusText = "";

            //Retrieving the current date
            let currentDate= getCurrentDate();
            //Parsing the visit date for comparison
            let visitDate = Date.parse(date);

            //Checking to see if the visit has been canceled
            if(status === false){
                statusText = "Canceled";
            }
            else{
                //Old status message
                // statusText = "Not Canceled"

                if(visitDate > currentDate){
                    //Display if visit is in the future
                    statusText = "Scheduled";
                }
                else if(visitDate == currentDate){
                    //Display if visit is today
                    statusText = "Today";
                }
                else if(visitDate < currentDate){
                    //Display if visit is in the past
                    statusText = "Billed";
                }
                else{
                    //Troubleshooting text in case of exception
                    statusText = "No comparison working";
                }
            }

            //Return text to the view
            return statusText;
        };
    }]);
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
'use strict'

angular.module('petDetails')
    .controller('PetDetailsController', ['$http', '$scope', '$state', '$stateParams', function ($http, $scope, $state, $stateParams) {
        var self = this


        $http.get("api/gateway/owners/" + $stateParams.ownerId + "/pets/" + $stateParams.petId).then(function (resp){
                self.pet = resp.data;

        })

        self.submitPetDetailsForm = function (){


            var uri = "api/gateway/owners/" + $stateParams.ownerId + "/pets/" + $stateParams.petId
            $http.put(uri, self.pet)

        }


    }])
'use strict'

angular.module('petDetails')
    .controller('PetDetailsController', ['$http', '$state', '$stateParams', function ($http, $state, $stateParams) {
        var self = this


        $http.get("api/gateway/owners/" + $stateParams.ownerId + " /pets/" + $stateParams.petId).then(function (resp){
                self.pet = resp.data;
        })


    }])
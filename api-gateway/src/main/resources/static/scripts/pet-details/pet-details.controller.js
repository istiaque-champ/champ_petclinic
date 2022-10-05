'use strict'

angular.module('petDetails')
    .controller('PetDetailsController', ['$http', '$state', '$stateParams', function ($http, $state, $stateParams) {
        var self = this


        $http.get("owners/" + $stateParams.ownerId + " /pets/" + $stateParams.petId).then(function (resp){
                self.pet = resp.data;
        })


    }])
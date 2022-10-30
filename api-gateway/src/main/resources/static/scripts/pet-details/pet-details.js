'use strict'

angular.module('petDetails', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider){
       $stateProvider
           .state('petDetails', {
               parent: 'app',
               url: '/pets/:petId/:ownerId',
               template:'<pet-details></pet-details>'
           })
    }])
'use strict';

angular.module('visitVetList', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('vetVisits', {
                parent: 'app',
                url: '/visits/vets',
                template: '<visit-vetlist></visit-vetlist>'
            })
            .state('chosen',{
                parent: 'app',
                url: '/visits/vets/:practitionerId',
                template: '<visit-vetlist></visit-vetlist>'
            })
    }]);

'use strict';

angular.module('visitVetlist', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('visits', {
                parent: 'app',
                url: '/vets/visits',
                template: '<visit-vetlist></visit-vetlist>'
            })
    }]);

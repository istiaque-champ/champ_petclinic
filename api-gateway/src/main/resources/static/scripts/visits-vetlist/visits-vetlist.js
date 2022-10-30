'use strict';

angular.module('visitsVetList', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('VisitsVet', {
                parent: 'app',
                url: '/visits/vets',
                template: '<visits-vetlist></visits-vetlist>'
            })
    }]);

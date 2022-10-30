'use strict';

//Setting up the angular module for Visits-Vetlist.
//1. Use the same name as the module used in component file.
angular.module('visitsVetList', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            //2. Create new state for State Provider
            .state('VisitsVet', {
                //3. Define name of parent
                parent: 'app',
                //4. Define path for page
                url: '/visits/vets',
                //5. Define template headers
                template: '<visits-vetlist></visits-vetlist>'
            })
    }]);

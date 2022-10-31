'use strict';

//Setting up the angular module for visits
//1. Use the same name as the module used in component file.
angular.module('visits', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            //2. Create new state for State Provider
            .state('visits', {
                //3. Define name of parent
                parent: 'app',
                //4. Define path for page
                url: '/owners/:ownerId/pets/:petId/visits',
                //5. Define template headers
                template: '<visits></visits>'
            })
    }]);

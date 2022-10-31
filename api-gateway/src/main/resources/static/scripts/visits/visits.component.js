'use strict';

//This is the file for the creation of the angular module used.
//1. the name for the module is defined.
angular.module('visits')
    //2. the component for the module is defined.
    .component('visits', {
        //3. the patch for the template is defined.
        templateUrl: 'scripts/visits/visits.template.html',
        //4. the name of the controller is defined.
        controller: 'VisitsController'
    });

'use strict';

//This is the file for the creation of the angular module used.
//1. The name for the module is defined.
angular.module('visitsVetList')
    //2. The component for the module is defined.
    .component('visitsVetList', {
        //3. The path for the template is defined.
        templateUrl: 'scripts/visits-vetlist/visits-vetlist.template.html',
        //4.The name of the controller is defined.
        controller: 'VisitsVetListController'
    });

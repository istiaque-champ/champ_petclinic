'use strict';

angular.module('billHistory', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('bills', {
                parent: 'app',
                url: '/bills',
                template: '<bill-history></bill-history>'
            })
            .state('deleteBill', {
                parent: 'app',
                url: '/bills/:billId/deleteBill',
                template: '<bill-history></bill-history>'
            })
            .state('billsByFilter', {
                parent: 'app',
                url: '/bills/:filterType/:filterId',
                params: {filterType: null, filterId: null},
                template: '<bill-history></bill-history>'
            })
    }]);
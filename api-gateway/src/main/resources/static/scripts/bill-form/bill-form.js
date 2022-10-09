'use strict';

angular.module('billForm', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('addBill', {
                parent: 'app',
                url: '/billing/:billingId/billings/add-bill',
                template: '<bill-form></bill-form>'
            })
            .state('editBill', {
                parent: 'app',
                url: '/billing/:billingId/billings/:billId/:method',
                template: '<bill-form></bill-form>'
            })
    }]);

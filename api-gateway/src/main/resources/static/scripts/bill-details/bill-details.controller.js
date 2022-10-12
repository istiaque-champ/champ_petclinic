'use strict';

angular.module('billDetails')
    .controller('BillDetailsController', ['$http', '$stateParams', function ($http, $stateParams) {
        var self = this
        //var newBillId = this

        $http.get('api/gateway/bills/' + $stateParams.billId).then(function (resp) {
            self.bills = resp.data;
        })

       /* $http.post('api/gateway/bills/' + $stateParams.newId).then(function (createBillId){
            newBillId.bills = createBillId.data;
        })*/

        self.submitBillDetailsForm = function (){

            var uri = 'api/gateway/bills/' + $stateParams.billId
            $http.put(uri, self.bills)
        }

       /* self.AddBillForm = function (){
            var uri = 'api/gateway/bills/' + $$stateParams.newId
            $http.post(uri, newBillId.bills)
        }*/
    }])

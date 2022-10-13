'use strict';

angular.module('billDetails')
    .controller('BillDetailsController', ['$http', '$scope', '$stateParams', function ($http, $scope, $stateParams) {
        var self = this
        var createBill = $stateParams.billId == null

        if (!createBill){
            $http.get('api/gateway/bills/' + $stateParams.billId).then(function (resp) {
                self.bills = resp.data;
            })
        }

        $http.get('api/gateway/owners').then(function (resp){
            self.owners = resp.data;
           if(!createBill){
                for (let i = 0; i < self.owners.length; i++){
                    if(self.owners[i].customerId == self.bills.ownerID){
                        self.selectedOwner = self.owners[i];
                        break;
                    }
                }
            }
        })

        /*Pet doesn't work*/
        $http.get('api/gateway/owners').then(function (resp){
            self.owners = resp.data;
            if(!createBill){
                for (let i = 0; i < self.owners.pet.length; i++){
                    if(self.owners.pet[i] == self.bills.ownerID){
                        self.selectedPet = self.owners.pet[i];
                        break;
                    }
                }
            }
        })

        $http.get('api/gateway/vets').then(function (resp){
            self.vets = resp.data;
            if(!createBill){
                for (let i = 0; i < self.vets.length; i++){
                    if(self.vets[i].vetId == self.bills.vet.ID){
                        self.selectedVet = self.vets[i];
                        break;
                    }
                }
            }
        })

        self.submitBillDetailsForm = function (){

            if (createBill){
                console.log('Creating new bill');
                console.log(self.bills);
                var uri = 'api/gateway/bills';
                $http.post(uri, self.bills)
            }else {
                var uri = 'api/gateway/bills/' + $stateParams.billId;
                $http.put(uri, self.bills)
            }
        }

        self.printPage = function (){
            window.print();
        }

    }])

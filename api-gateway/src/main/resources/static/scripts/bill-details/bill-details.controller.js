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
            self.selectedOwner = self.owners[0];
            self.selectedPet = self.selectedOwner.pets[0];
           if(!createBill){
                for (let i = 0; i < self.owners.length; i++){
                    if(self.owners[i].id == self.bills.customerId){
                        self.selectedOwner = self.owners[i];
                        for (let j = 0; j < self.selectedOwner.pets.length; j++){
                            if(self.selectedOwner.pets[j].id == self.bills.petId){
                                self.selectedPet = self.selectedOwner.pets[j];
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        })

        $http.get('api/gateway/vets').then(function (resp){
            self.vets = resp.data;
            self.selectedVet = self.vets[0];
            if(!createBill){
                for (let i = 0; i < self.vets.length; i++){
                    if(self.vets[i].vetId == self.bills.vetId){
                        self.selectedVet = self.vets[i];
                        break;
                    }
                }
            }
        })

        self.submitBillDetailsForm = function (){

            self.bills.customerId = self.selectedOwner.id;
            self.bills.vetId = self.selectedVet.vetId;
            self.bills.petId = self.selectedPet.id;

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
            var prtContent = document.getElementById("details");
            var WinPrint = window.open('', '', 'left=0,top=0,width=800,height=900,toolbar=0,scrollbars=0,status=0');
            WinPrint.document.write(prtContent.innerHTML);
            WinPrint.document.close();
            WinPrint.focus();
            WinPrint.print();
            WinPrint.close();
        }

    }])

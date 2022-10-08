'use strict';


angular.module('billHistory')
    .controller('BillHistoryController', ['$http', '$scope', '$stateParams', function ($http, $scope, $stateParams) {
        var self = this;

        let requestUrl = "api/gateway/bills/";

        if($stateParams.filterId != null ){
            requestUrl += $stateParams.filterType + "/" + $stateParams.filterId;
        }

        console.log(requestUrl);
        //############ API ############
        //get all bills
        $http.get(requestUrl).then(async (resp) => {
            self.billHistory = resp.data;

            let billDetailedData = [];

            //TODO: optimize these incredibly slow API calls
            await self.billHistory.forEach(async (bill) => {
                let res = await $http.get('api/gateway/owners/' + bill.customerId);

                // console.log(res);

                billDetailedData.push({"customerId": bill.customerId, "ownerInfo": res});
            })

            self.billDetailedData = billDetailedData;
        });

        //delete bill by ID
        $scope.deleteBill = function (billId) {
            let varIsConf = confirm('You are about to delete billId ' + billId + '. Is it what you want to do ? ');
            if (varIsConf) {

                $http.delete('api/gateway/bills/' + billId)
                    .then(successCallback, errorCallback)

                function successCallback(response) {
                    $scope.errors = [];
                    alert(billId + " bill was deleted successfully");
                    console.log(response, 'res');
                    //refresh list
                    $http.get('api/gateway/bills').then(function (resp) {
                        self.billHistory = resp.data;
                        arr = resp.data;
                    });
                }

                function errorCallback(error) {
                    alert(data.errors);
                    console.log(error, 'Could not receive data');
                }
            }
        };

        //######### Rendering #########
        this.show = ($event, billId) => {
            let parent = $event.target //document.getElementsByClassName("t-id" + billId)[0];
            let child = document.getElementsByClassName("m" + billId)[0];

            let parentRect = parent.getBoundingClientRect();
            let right = parentRect.right;
            let top = parentRect.y;

            child.classList.remove("modalOff");
            child.classList.add("modalOn");

            child.style.top = `${top}px`;
            // child.style.left = `${right + width}px`;
            child.style.left = `${right}px`;
            // child.innerHTML = `${width}`;
        }
        this.hide = ($event, billId) => {
            let child = document.getElementsByClassName("m" + billId)[0];
            child.classList.remove("modalOn");
            child.classList.add("modalOff");
        }

        this.getFullName = (customerData) => {
            let obj = customerData[0];
            let fullName = obj["ownerInfo"]["data"]["firstName"] + obj["ownerInfo"]["data"]["lastName"];
            console.log(fullName);
            return fullName;
        }
    }]);

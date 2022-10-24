'use strict';
angular.module('adminPanel')
    .controller('AdminPanelController', ['$http', '$scope', 'authProvider', '$location',  function ($http, $scope, authProvider, $location) {

        var self = this;

        $http.get('api/gateway/users').then(function (resp) {
            self.users = resp.data;
        });

        $scope.removeUser = function (userid) {
            $http.delete('api/gateway/users/' + userid).then(function () {
                $http.get('api/gateway/users').then(function (resp) {
                    self.users = resp.data;
                });
            });
        };
    }


    ]);







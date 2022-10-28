<!--        * Created by IntelliJ IDEA.-->
<!--        * User: @JordanAlbayrak-->
<!--        * Date: 22/09/21-->
<!--        * Ticket: feat(AUTH-CPC-102)_signup_user-->
'use strict';

angular.module('signupForm')
    .controller('SignupFormController', ['$http', '$scope', "$location", "$stateParams", function ($http, $scope, $location, $stateParams) {

        self.updating = $stateParams.userId != null;

        let signup = window.location.href === "http://localhost:8080/#!/signup"

        if(signup){
            const el = document.getElementById("userType");
            el.style.display = "none";
        }


        this.add = function() {
            if(!self.updating){
                $http.post('/api/gateway/users/', {
                    username: $scope.signup.username,
                    password: $scope.signup.password,
                    email: $scope.signup.email,
                    userType: signup ? 2 : $scope.signup.userType,
                    userTypeId: 1
                })
                    .then(() => $location.path("/adminPanel"))
                    .catch(n => {
                        $scope.errorMessages = n.data.message.split`\n`;
                        console.log(n);
                    });
            } else {
                $http.put("/api/gateway/users/" + $stateParams.userId, {
                    username: $scope.signup.username,
                    password: $scope.signup.password,
                    email: $scope.signup.email,
                    userType: signup ? 2 : $scope.signup.userType,
                    userTypeId: 1
                })
                    .then(() => {
                        $location.path("/adminPanel");
                    })
                    .catch(n => {
                        $scope.errorMessages = n.data.message.split`\n`;
                        console.log(n);
                    });

            }

        }

        this.keypress = ({ originalEvent: { key } }) => key === 'Enter' && this.add()
    }]);
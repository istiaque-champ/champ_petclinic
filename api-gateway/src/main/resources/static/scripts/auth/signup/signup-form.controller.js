<!--        * Created by IntelliJ IDEA.-->
<!--        * User: @JordanAlbayrak-->
<!--        * Date: 22/09/21-->
<!--        * Ticket: feat(AUTH-CPC-102)_signup_user-->
'use strict';

angular.module('signupForm')
    .controller('SignupFormController', ['$http', '$scope', "$location", "$stateParams", function ($http, $scope, $location, $stateParams) {

        var signup = $stateParams.method === "signupForm";
        self.signup = signup;

        this.update = () => $http.put("/api/gateway/users/" + $stateParams.id, {
            username: $scope.signup.username,
            password: $scope.signup.password,
            email: $scope.signup.email,
        })
            .then(() => {
                $location.path("/adminPanel");
            })
            .catch(n => {
                $scope.errorMessages = n.data.message.split`\n`;
                console.log(n);
            });

        this.add = () => $http.post('/api/gateway/users/', {
            username: $scope.signup.username,
            password: $scope.signup.password,
            email: $scope.signup.email,
        })
            .then(() => $location.path(signup ? "/login" : "/adminPanel"))
            .catch(n => {
                $scope.errorMessages = n.data.message.split`\n`;
                console.log(n);
            });

        this.keypress = ({ originalEvent: { key } }) => key === 'Enter' && this.add()
    }]);
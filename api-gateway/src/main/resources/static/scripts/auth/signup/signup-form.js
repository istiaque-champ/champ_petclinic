<!--        * Created by IntelliJ IDEA.-->
<!--        * User: @JordanAlbayrak-->
<!--        * Date: 22/09/21-->
<!--        * Ticket: feat(AUTH-CPC-102)_signup_user-->
'use strict';

angular.module('signupForm', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('signupForm', {
                parent: 'app',
                url: '/signup',
                template: '<signup-form></signup-form>'
            })
            .state('createUser', {
                parent: 'app',
                url: "/users",
                template: '<signup-form></signup-form>'
            })
            .state('updateUser', {
                parent: 'app',
                url: "/users/:id",
                template: '<signup-form></signup-form>'
            })
    }]);

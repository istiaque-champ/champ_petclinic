'use strict';
// Whitelist for all things related to auth and Q 401/403 handling

//Can erase everything that is commented under. Its all for internationalization but it didnt work
const whiteList = new Set([
    'login',
    'signup',
]);

/* App Module */
var petClinicApp = angular.module('petClinicApp', [
    'ui.router', 'layoutNav', 'layoutFooter', 'layoutWelcome', 'petDetails' , 'ownerList', 'ownerDetails', 'ownerForm', 'petForm'
    , 'visits', 'vetList','vetForm','vetDetails', 'loginForm', 'rolesDetails', 'signupForm', 'billDetails', 'billHistory'
    , 'verification' , 'adminPanel']);
   /* module name for translation: 'pascalprecht.translate'*/
petClinicApp.factory("authProvider", ["$window", function ($window) {

    return {
        setUser: ({ token, username, email }) => {
            $window.localStorage.setItem("token", token)
            $window.localStorage.setItem("username", username)
            $window.localStorage.setItem("email", email)
        },
        getUser: () => ({
            token: $window.localStorage.getItem("token"),
            username: $window.localStorage.getItem("username"),
            email: $window.localStorage.getItem("email"),
        }),
        purgeUser: () => {
            $window.localStorage.removeItem("token")
            $window.localStorage.removeItem("username")
            $window.localStorage.removeItem("email")
        },
        isLoggedIn: () => !!$window.localStorage.getItem("token")
    }
}]);

petClinicApp.factory("httpErrorInterceptor", ["$q", "$location", "authProvider", function ($q, $location, authProvider) {
    return {
        // NOTE: This is not the correct way to do this
        // This method completely disregards whoever was subscribed to this promise and just cancels it
        // I just do not care to implement it correctly because it is more complex
        responseError: rej => {
            if (!whiteList.has($location.path().substring(1)) && (rej.status === 401 || rej.status === 403)) {
                authProvider.purgeUser();
                $location.path('/login');
                return $q(() => null)
            }
            return $q.reject(rej);
        }
    }
}]);

petClinicApp.run(['$rootScope', '$location', 'authProvider', function ($rootScope, $location, authProvider) {
    $rootScope.$on('$locationChangeSuccess', function (event) {

        if(whiteList.has($location.path().substring(1))) {
            return console.log("WHITE LISTED: Ignoring");
        }

        if (!authProvider.isLoggedIn()) {
            console.log('DENY : Redirecting to Login');
            event.preventDefault();
            $location.path('/login');
        }
        else {
            console.log('ALLOW');
        }
    });
}])

//Translation table
/*
var visits_translations = {
    UPCOMING_VISITS: 'Upcoming Visits:',
    PREVIOUS_VISITS: 'Previous Visits:',
    VISITS: "Visits",
    PHONE_NUMBER: "Phone Number",
    EMAIL_ADDRESS: "Email Address" ,
    SPECIALITIES: "Specialities",
    WORKDAYS: "Workdays",
    DATE: "date",
    DESCRIPTION: "Description",
    VISITS_TYPE:'Visits Type'
};
console.table(visits_translations);

var translations = {
    BILL_HISTORY: 'Bill History :',
    BILL_ID: 'Bill Id:',
    OWNER: "Owner",
    VISIT_TYPE: "Visit Type",
    NAMESPACE: {
        BILL_ID: 'Bill Id in namespace'
    }
};
console.table(translations);

*/

petClinicApp.config(['$stateProvider', '$urlRouterProvider', '$locationProvider', '$httpProvider', function (
    /*config name for tanslation : '$translateProvider'*/
    $stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {
    
    /*config name for translation: $translateProvider*/

    // safari turns to be lazy sending the Cache-Control header
    $httpProvider.defaults.headers.common["Cache-Control"] = 'no-cache';

    $locationProvider.hashPrefix('!');

    $urlRouterProvider.otherwise('/welcome');
    $stateProvider
        .state('app', {
            abstract: true,
            url: '',
            template: '<ui-view></ui-view>'
        })
        .state('welcome', {
            parent: 'app',
            url: '/welcome',
            template: '<layout-welcome></layout-welcome>'
        });

    $httpProvider.interceptors.push('httpErrorInterceptor');

    /*
    console.log("HELLO FROM CONFIG");
    $translateProvider.translations('en', visits_translations);
    $translateProvider.preferredLanguage('en');

     */

}]);

['welcome', 'nav', 'footer'].forEach(function (c) {
    var mod = 'layout' + c.toUpperCase().substring(0, 1) + c.substring(1);
    const controller = mod+"Controller";

    angular.module(mod, []);
    angular.module(mod)
        .controller(controller, ['$rootScope', '$scope', 'authProvider', function ($rootScope, $scope, authProvider) {

            const load = () => {
                $scope.isLoggedIn = authProvider.isLoggedIn();
                if(!$scope.isLoggedIn)return;

                const { email, username } = authProvider.getUser();
                $scope.email = email;
                $scope.username = username;
            }

            $rootScope.$on('$locationChangeSuccess', load);
            load();
    }]);

    angular.module(mod).component(mod, {
        templateUrl: "scripts/fragments/" + c + ".html",
        controller,
    });
});
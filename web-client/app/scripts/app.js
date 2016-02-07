'use strict';

/**
 * @ngdoc overview
 * @name canvass
 * @description
 * # canvass
 *
 * Main module of the application.
 */
angular
  .module('canvass', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.bootstrap'
  ])
  .constant('apiUrl', 'http://localhost:8090/api/canvass')
  .config(function ($routeProvider) {

    /**
     * Reusable auth check function, that will check if a user is logged in before a route
     * can be accessed.
     * TODO in future the authService.test() method might take a role or permission
     */
    var authCheck = ["$q", "authService", "$location", "$rootScope", function ($q, authService, $location, $rootScope) {
      var deferred = $q.defer();
      authService.test()
        .then(function (response) {
          $rootScope.currentUser = response.data;
          deferred.resolve(response);
        })
        .catch(function () {
          $rootScope.currentUser = null;
          deferred.reject();
          $location.path('/login');
        });
      return deferred.promise;
    }];

    $routeProvider
      .when('/dashboard', {
        templateUrl: 'views/dashboard.html',
        resolve: {
          auth: authCheck
        }
      })
      .when('/canvass', {
        templateUrl: 'views/canvass.html',
        controller: 'canvassGeneratorController',
        resolve: {
          auth: authCheck
        }
      })
      .when('/admin', {
        templateUrl: 'views/admin.html',
        resolve: {
          auth: authCheck
        }
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'loginController'
      })
      .otherwise({
        redirectTo: '/dashboard'
      });
  });

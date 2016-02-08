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

    var authByRoute = [
      {route: '/dashboard', role: 'USER'},
      {route: '/canvass', role: 'USER'},
      {route: '/admin', role: 'ADMIN'},
      {route: '/users', role: 'ADMIN'}
    ];

    /**
     * Reusable auth check function, that will check if a user is logged in before a route
     * can be accessed.
     */
    var userAuth = function ($q, authService, $location, $rootScope) {
      var deferred = $q.defer(),
        route = _.find(authByRoute, {route: $location.path()});

      authService.test(route.role)
        .then(function (response) {
          $rootScope.currentUser = response.data;
          deferred.resolve(response);
        })
        .catch(function () {
          $rootScope.currentUser = null;
          deferred.reject();
          if ($location.path() === '/dashboard') {
            $location.path('/login');
          } else {
            $location.path('/dashboard')
          }

        });
      return deferred.promise;
    };

    $routeProvider
      .when('/dashboard', {
        templateUrl: 'views/dashboard.html',
        resolve: {
          auth: userAuth
        }
      })
      .when('/canvass', {
        templateUrl: 'views/canvass.html',
        controller: 'canvassGeneratorController',
        resolve: {
          auth: userAuth
        }
      })
      .when('/admin', {
        templateUrl: 'views/admin.html',
        resolve: {
          auth: userAuth
        }
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'loginController'
      })
      .when('/users', {
        templateUrl: 'views/users.html',
        controller: 'adminUserController',
        resolve: {
          auth: userAuth
        }
      })
      .otherwise({
        redirectTo: '/dashboard'
      });
  });

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
    'ui.bootstrap',
    'angular-loading-bar'
  ])
  .constant('config', {
    apiUrl: 'http://localhost:8080/api/canvass',
    supportEmail: 'canvassappsupport@voteleave.uk'
  })
  .run(function($rootScope, config) {
    $rootScope.supportEmail = config.supportEmail;
  })
  .config(function ($routeProvider) {

    var authByRoute = [
      {route: '/dashboard', role: 'USER'},
      {route: '/canvass', role: 'USER'},
      {route: '/profile', role: 'USER'},
      {route: '/recordvote', role: 'USER'},
      {route: '/admin', role: 'ADMIN'},
      {route: '/associations', role: 'ADMIN'},
      {route: '/users', role: 'ADMIN'}
    ];

    /**
     * Reusable auth check function, that will check if a user is logged in before a route
     * can be accessed. This check will use a web service to validate that a user has a certain
     * role before displaying the page.  Any future api authentication is also checked on the
     * server using session tokens.
     */
    var userAuth = function ($q, authService, $location, $rootScope) {
      var deferred = $q.defer(),
        route = _.find(authByRoute, function(route) {
          return _.startsWith($location.path(), route.route);
        });

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
            $location.path('/dashboard');
          }

        });
      return deferred.promise;
    };

    $routeProvider
      .when('/dashboard', {
        templateUrl: 'views/dashboard.html',
        controller: 'dashboardController',
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
      .when('/recordvote', {
        templateUrl: 'views/recordvote.html',
        controller: 'recordVoteController',
        resolve: {
          auth: userAuth
        }
      })
      .when('/canvassinput', {
        templateUrl: 'views/canvassinput.html',
        controller: 'canvassInputController',
        resolve: {
          auth: userAuth
        }
      })
      .when('/associations/:id', {
        templateUrl: 'views/associations.html',
        controller: 'associationsController',
        resolve: {
          auth: userAuth
        }
      })
      .when('/profile', {
        templateUrl: 'views/profile.html',
        controller: 'profileController',
        resolve: {
          auth: userAuth
        }
      })
      .otherwise({
        redirectTo: '/dashboard'
      });
  });

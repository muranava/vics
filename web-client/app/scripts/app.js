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
    $routeProvider
      .when('/dashboard', {
        templateUrl: 'views/dashboard.html'
      })
      .when('/canvass', {
        templateUrl: 'views/canvass.html',
        controller: 'canvassGeneratorCtrl'
      })
      .when('/admin', {
        templateUrl: 'views/admin.html'
      })
      .when('/stats', {
        templateUrl: 'views/stats.html'
      })
      .when('/login', {
        templateUrl: 'views/login.html'
      })
      .otherwise({
        redirectTo: '/dashboard'
      });
  });

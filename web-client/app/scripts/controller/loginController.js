angular
  .module('canvass')
  .controller('loginController', function ($scope, $http, $location, authService, $window, $animate) {
    $animate.enabled(false);
    $scope.images = [
      "https://d3n8a8pro7vhmx.cloudfront.net/voteleave/pages/2187/attachments/original/1457722450/splash-1.jpg",
      "https://d3n8a8pro7vhmx.cloudfront.net/voteleave/pages/2187/attachments/original/1457722446/splash-2.jpg",
      "https://d3n8a8pro7vhmx.cloudfront.net/voteleave/pages/2187/attachments/original/1457722443/splash-3.jpg",
      "https://d3n8a8pro7vhmx.cloudfront.net/voteleave/pages/2187/attachments/original/1457722430/splash-5.jpg",
      "https://d3n8a8pro7vhmx.cloudfront.net/voteleave/pages/2187/attachments/original/1457722423/splash-6.jpg",
      "https://d3n8a8pro7vhmx.cloudfront.net/voteleave/pages/2187/attachments/original/1457661894/o11o.jpg"
    ];
    $scope.failedLogin = false;
    $scope.credentials = {};

    $scope.login = function () {
      authService.login($scope.credentials.username, $scope.credentials.password)
        .success(function () {
          $location.path('/dashboard');
        })
        .error(function (err) {
          if (err && err.type === 'LoginFailure') {
            $scope.badCredentials = true;
          } else {
            $scope.networkError = true;
          }
        });
    };

    $scope.logout = function () {
      authService.logout();
    };

    $scope.onCloseEgg = function () {
      $('#egg').hide();
    };

    var egg = new Egg();
    egg.addCode("up,up,down,down,left,right,left,right,b,a", function () {
      })
      .addHook(function () {
        $('#egg').show();
        $('#closeBtn').show();

        var game = $('.game');
        game.blockrain({
          showFieldOnStart: false,
          playText: $window.atob("Q3JlYXRlZCBieSBTdGVpbiBGbGV0Y2hlcg==")
        });

      }).listen();
  });

angular
  .module('canvass')
  .controller('loginController', function ($scope, $http, $location, authService) {

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

    $scope.onCloseEgg = function() {
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
          playText: 'Let\'s play some Tetris'
        });

      }).listen();
  });

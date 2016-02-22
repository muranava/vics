angular
  .module('canvass')
  .controller('dashboardController', function ($interval, $scope) {
    var referendumDate = new Date(2016, 5, 23, 8, 0);

    function updateCountdown() {
      // get total seconds between the times
      var delta = Math.abs(referendumDate - new Date()) / 1000;

      // calculate (and subtract) whole days
      $scope.days = Math.floor(delta / 86400);
      delta -= $scope.days * 86400;

      // calculate (and subtract) whole hours
      $scope.hours = Math.floor(delta / 3600) % 24;
      delta -= $scope.hours * 3600;

      // calculate (and subtract) whole minutes
      $scope.minutes = Math.floor(delta / 60) % 60;
      delta -= $scope.minutes * 60;

      // what's left is seconds
      $scope.seconds = delta % 60;
    }

    updateCountdown();
    $interval(updateCountdown, 999, 0, true);
  });

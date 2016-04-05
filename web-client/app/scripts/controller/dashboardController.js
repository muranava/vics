angular
  .module('canvass')
  .controller('dashboardController', function ($interval, $scope, statsService) {
    var referendumDate = new Date(2016, 5, 23, 22, 0),
      secondsInDay = 86400,
      secondsInHour = 3600;

    statsService.topCanvassers()
      .success(function (response) {
        $scope.topCanvassers = response;
      });

    statsService.topWards()
      .success(function (response) {
        $scope.topWards = _.orderBy(response, function(e) {
          return e.count;
        }, 'desc');
      });

    statsService.topConstituencies()
      .success(function (response) {
        $scope.topConstituencies = response;
      });

    function updateCountdown() {
      // get total seconds between the times
      var delta = Math.abs(referendumDate - new Date()) / 1000;

      // calculate (and subtract) whole days
      $scope.days = Math.floor(delta / secondsInDay);
      delta -= $scope.days * secondsInDay;

      // calculate (and subtract) whole hours
      $scope.hours = Math.floor(delta / secondsInHour) % 24;
      delta -= $scope.hours * secondsInHour;

      // calculate (and subtract) whole minutes
      $scope.minutes = Math.floor(delta / 60) % 60;
      delta -= $scope.minutes * 60;

      // what's left is seconds
      $scope.seconds = delta % 60;
    }

    updateCountdown();
    $interval(updateCountdown, 450, 0, true);
  });

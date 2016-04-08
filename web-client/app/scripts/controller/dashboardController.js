angular
  .module('canvass')
  .controller('dashboardController', function ($interval, $scope, statsService, toastr, geoService) {
    var referendumDate = new Date(2016, 5, 23, 22, 0),
      secondsInDay = 86400,
      secondsInHour = 3600;

    $scope.currentTab = 'activists';
    $scope.constituencyName = '';
    $scope.wardName = '';

    $scope.mapData = {
      location: {
        lat: 52.400251,
        lng: -1.4497,
        zoom: 12
      }
    };

    statsService.allStats()
      .success(function (response) {
        $scope.stats = response;
      });

    $scope.changeLeaderboardTab = function(tabName) {
      $scope.currentTab = tabName;
    };

    $scope.findWard = function() {
      if (_.isEmpty($scope.postCode)) {
        return;
      }

      geoService.findWardFromPostCode($scope.postCode)
        .success(function(response) {
          if (response && response.wgs84_lat) {
            $scope.constituencyName = response.areas[response.shortcuts.WMC].name
            if (_.isObject(response.shortcuts.ward)) {
              $scope.wardName = response.areas[response.shortcuts.ward.district].name;
            } else {
              $scope.wardName = response.areas[response.shortcuts.ward].name;
            }

            $scope.mapData = {
              location: {
                lat: response.wgs84_lat,
                lng: response.wgs84_lon,
                zoom: 12
              },
              markers: {
                m1: {
                  lat: response.wgs84_lat,
                  lng: response.wgs84_lon
                }
              }
            };
          }
        })
        .error(function() {
          toastr.info('We could not find a location for that post code', 'Sorry');
        });
    };

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
    $interval(updateCountdown, 250, 0, true);
  });

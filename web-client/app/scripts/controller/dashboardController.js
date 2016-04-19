angular
  .module('canvass')
  .controller('dashboardController', function ($interval, $scope, statsService, toastr, geoService) {
    var referendumDate = new Date(2016, 5, 23, 22, 0),
      secondsInDay = 86400,
      secondsInHour = 3600;

    $scope.currentTab = 'activists';
    $scope.constituencyName = '';
    $scope.wardName = '';

    $scope.map = {
      center: {
        latitude: 52.400251,
        longitude: -1.4497
      },
      zoom: 12
    };
    $scope.markers = [];

    $scope.$on('$viewContentLoaded', function() {
      $("#postcodeMap .angular-google-map-container").height(250);
    });

    statsService.allStats()
      .success(function (response) {
        $scope.stats = response;
      });

    $scope.changeLeaderboardTab = function (tabName) {
      $scope.currentTab = tabName;
    };

    $scope.findWard = function (postCode, suppressError) {
      if (_.isEmpty(postCode)) {
        return;
      }

      geoService.findWardFromPostCode(postCode)
        .success(function (response) {
          if (response && response.wgs84_lat) {
            $scope.constituencyName = response.areas[response.shortcuts.WMC].name;
            if (_.isObject(response.shortcuts.ward)) {
              $scope.wardName = response.areas[response.shortcuts.ward.district].name;
            } else {
              $scope.wardName = response.areas[response.shortcuts.ward].name;
            }

            $scope.map = {
              center: {
                latitude: response.wgs84_lat,
                longitude: response.wgs84_lon
              },
              zoom: 12
            };
            $scope.markers[0] = {id: 1, latitude: response.wgs84_lat, longitude: response.wgs84_lon};
          }
        })
        .error(function () {
          if (!suppressError) {
            toastr.info('We could not find a location for that post code', 'Sorry');
          }
        });
    };

    /**
     * Attempt to set the area to the users location
     */
    function getCurrentLocation() {
      var options = {
        enableHighAccuracy: true
      };

      if (navigator && navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(pos) {
            $scope.position = new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
            var point = JSON.parse(JSON.stringify($scope.position));
            if (_.isObject(point)) {
              $scope.map.center = {
                latitude: point.lat,
                longitude: point.lng
              };
              geoService.findPostCodeFromCoordinates(point.lat, point.lng)
                .success(function(response) {
                  var result = _.head(response.result);
                  if (result) {
                    $scope.findWard(result.postcode, true);
                  }

                });
            }
          },
          function() {}, options);
      }
    }

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

    getCurrentLocation();
  });

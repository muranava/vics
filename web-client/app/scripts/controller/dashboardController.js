angular
  .module('canvass')
  .controller('dashboardController', function ($interval, $scope, statsService, toastr, geoService, calendar, $uibModal, wardService, constituencyService) {
    var referendumDate = new Date(2016, 5, 23, 22, 0),
      secondsInDay = 86400,
      secondsInHour = 3600,
      searchLimit = 15;

    $scope.currentTab = 'activists';
    $scope.constituencyName = '';
    $scope.wardName = '';
    $scope.showCanvassedGraph = false;
    $scope.graphLabel = "All";

    $scope.map = {
      center: {
        latitude: 52.400251,
        longitude: -1.4497
      },
      control: {
        zoomControl: true,
        mapTypeControl: false,
        scaleControl: false,
        streetViewControl: false,
        rotateControl: false,
        fullscreenControl: false
      },
      zoom: 12
    };
    $scope.markers = [];

    $scope.$on('$viewContentLoaded', function () {
      $("#postcodeMap .angular-google-map-container").height(250);
    });

    statsService.allStats()
      .success(function (response) {
        $scope.stats = response;
        $scope.data = [{
          key: "Total Canvassed",
          values: mapWeeklyCanvassStatsToCalendar($scope.stats.recordContactByDate)
        }];
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
        navigator.geolocation.getCurrentPosition(function (pos) {
            $scope.position = new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
            var point = JSON.parse(JSON.stringify($scope.position));
            if (_.isObject(point)) {
              $scope.map.center = {
                latitude: point.lat,
                longitude: point.lng
              };
              geoService.findPostCodeFromCoordinates(point.lat, point.lng)
                .success(function (response) {
                  var result = _.head(response.result);
                  if (result) {
                    $scope.findWard(result.postcode, true);
                  }

                });
            }
          },
          function () {
          }, options);
      }
    }

    $scope.onWardInputKeypress = function () {
      wardService.search($scope.wardSearchModel, searchLimit)
        .success(function (response) {
          $scope.wards = response;
        });
    };

    $scope.onSetWard = function () {
      if ($scope.wardSearchModel && $scope.wardSearchModel.id) {
        statsService.canvassedByWeekAndConstituency($scope.wardSearchModel.code)
          .success(function (response) {
            $scope.onShowWeeklyStatsConfig();
            var data = [{
              key: "Total Canvassed",
              values: mapWeeklyCanvassStatsToCalendar(response)
            }];
            _.defer(function () {
              $scope.api.updateWithData(data);
            });
            $scope.graphLabel = $scope.wardSearchModel.name;
            $scope.wardSearchModel = null;
          });
      } else {
        $scope.invalidWard = true;
      }
    };

    $scope.onSetConstituency = function () {
      if ($scope.constituencySearchModel && $scope.constituencySearchModel.id) {
        statsService.canvassedByWeekAndConstituency($scope.constituencySearchModel.code)
          .success(function (response) {
            $scope.onShowWeeklyStatsConfig();
            var data = [{
              key: "Total Canvassed",
              values: mapWeeklyCanvassStatsToCalendar(response)
            }];
            _.defer(function () {
              $scope.api.updateWithData(data);
            });
            $scope.graphLabel = $scope.constituencySearchModel.name;
            $scope.constituencySearchModel = null;
          });
      } else {
        $scope.invalidConstituency = true;
      }
    };

    $scope.onConstituencyInputKeypress = function () {
      $scope.invalidConstituency = false;
      constituencyService.search($scope.constituencySearchModel, searchLimit)
        .success(function (response) {
          $scope.constituencies = response;
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

    getCurrentLocation();

    function mapWeeklyCanvassStatsToCalendar(canvassedByWeek) {
      var stats = _.map(canvassedByWeek, function (e) {
        return {
          count: _.parseInt(e[0]),
          week: _.parseInt(e[1])
        };
      });

      return calendar.campaignWeeks().map(function (calendarWeek) {
        var currWeek = _.find(stats, function (stat) {
          return stat.week === calendarWeek.week;
        });
        return {
          week: ('' + calendarWeek.week).substring(4),
          start: calendarWeek.start,
          count: _.isUndefined(currWeek) ? 0 : currWeek.count
        };
      });
    }

    $scope.onShowWeeklyStatsConfig = function () {
      $scope.showCanvassedGraph = !$scope.showCanvassedGraph;
    };

    $scope.options = {
      chart: {
        type: 'discreteBarChart',
        height: 250,
        "color": [
          "#C5443B"
        ],
        x: function (d) {
          return d.week;
        },
        y: function (d) {
          return d.count;
        },
        tooltip: {
          contentGenerator: function (e) {
            var series = e.series[0];
            if (series.value === null) {
              return;
            }

            if (e.series) {
              var rows =
                "<tr>" +
                "<td class='key'>" + 'Start: ' + "</td>" +
                "<td class='x-value'>" + e.data.start + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td class='key'>" + 'Total Canvassed: ' + "</td>" +
                "<td class='x-value'><strong>" + series.value + "</strong></td>" +
                "</tr>";

              var header =
                "<thead>" +
                "<tr>" +
                "<td class='legend-color-guide'><div style='background-color: " + series.color + ";'></div></td>" +
                "<td class='key'>Week&nbsp;<strong>" + e.data.week + "</strong></td>" +
                "</tr>" +
                "</thead>";

              return "<table>" +
                header +
                "<tbody>" +
                rows +
                "</tbody>" +
                "</table>";
            }
          }
        },
        useInteractiveGuideline: true,
        showValues: false,
        xAxis: {
          axisLabel: 'Week'
        },
        yAxis: {
          axisLabel: 'Canvassed',
          axisLabelDistance: -10,
          tickFormat: _.identity
        }
      }
    };
  });

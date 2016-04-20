angular
  .module('canvass')
  .controller('gotvController', function ($scope, wardService, $filter, geoService, electorService, $window, toastr, $uibModal) {

    $scope.numStreetsSelected = 0;
    $scope.validationErrors = [];
    $scope.currentSort = "Priority DESC";

    function createRadioItem(name) {
      return {
        name: name,
        enabled: false,
        value: null,
        handler: function(value) {
          this.value = value;
        }
      };
    }

    $scope.radios = [
      createRadioItem('wantsPV'),
      createRadioItem('hasPV'),
      createRadioItem('lift'),
      createRadioItem('poster'),
      createRadioItem('canvassed')
    ];

    function validateFlagsRadios() {
      var errors = [];

      $scope.radios.map(function (radio) {
        if (radio.enabled && radio.value === null) {
          errors.push("Radio for " + radio.name + " is not selected");
        }
      });

      return errors;
    }

    function defaultSliderOptions() {
      return {
        minValue: 3,
        maxValue: 5,
        options: {
          floor: 1,
          ceil: 5,
          showTicks: true,
          showTicksValues: true
        }
      };
    }

    function fire() {
      $scope.showSubMenu = $window.scrollY > 100;
      if (!$scope.$$phase) {
        $scope.$apply();
      }
    }
    var debounce = _.debounce(fire, 1, false);
    $("#canvassinputcontent").on('mousewheel', function () {
      debounce();
    });

    $scope.intentionSlider = defaultSliderOptions();
    $scope.likelihoodSlider = defaultSliderOptions();

    $scope.onSelectConstituency = function () {
      resetErrors();
      $scope.streets = [];
      $scope.numStreetsSelected = 0;
    };

    function scrollToPrintSection() {
      _.defer(function() {
        $("html, body").animate({scrollTop: $('#printCards').offset().top - 75}, 500);
      });
    }

    /**
     * Sorts the streets by the given field
     * @param field - either [mainStreet, numVoters, numCanvassed]
     * @param direction - either asc or desc
     * @param label - visible label to display in the dropdown
     */
    $scope.sortStreets = function (field, direction, label) {
      $scope.currentSort = label;
      $scope.streets = _.orderBy($scope.streets, field, direction);
    };

    $scope.onLoadedConstituencies = function (constituencies) {
      if (_.isEmpty(constituencies)) {
        $scope.userHasNoAssociations = true;
      }
      $scope.contentLoaded = true;
    };

    $scope.onSelectWard = function (directiveModel) {
      $scope.ward = directiveModel.ward;
      $scope.constituency = directiveModel.constituency;
      $scope.numStreetsSelected = 0;

      wardService.findStreetsByWard($scope.ward.code)
        .success(function (streets) {
          $scope.streets = _.orderBy(streets.streets, 'priority', 'desc');
          scrollToPrintSection();
        });
    };

    $scope.getNumStreetsSelected = function () {
      if ($scope.streets && $scope.streets.length) {
        $scope.numStreetsSelected = _.size(_.filter($scope.streets, function (s) {
          return s.selected;
        }));
      }
    };

    $scope.onPrintSelected = function () {
      $scope.errorLoadingData = null;
      var selected = _.filter($scope.streets, function (s) {
        return s.selected;
      });
      doPrint($scope.ward.code, selected, false);
    };

    $scope.onPrintAll = function () {
      $scope.errorLoadingData = null;
      doPrint($scope.ward.code, $scope.streets, false);
    };

    $scope.onPrintLabels = function() {
      var selected = _.filter($scope.streets, function (s) {
        return s.selected;
      });
      doPrint($scope.ward.code, selected, true);
    };

    function doPrint(wardCode, streets, isLabels) {
      $scope.validationErrors = validateFlagsRadios();
      if (_.isEmpty($scope.validationErrors)) {
        var data = buildRequest(streets);
        electorService.retrievePdfOfElectorsByStreets(wardCode, data, isLabels)
          .success(function (response) {
            var file = new Blob([response], {type: 'application/pdf'});
            saveAs(file, $scope.ward.code + '.pdf');
          })
          .error(function(error) {
            $scope.errorLoadingData = error.message;
          });
      }
    }

    function buildRequest(streets) {
      var flags = getActiveOptionalFlags();
      flags.intentionFrom = $scope.intentionSlider.minValue;
      flags.intentionTo = $scope.intentionSlider.maxValue;
      flags.likelihoodFrom = $scope.likelihoodSlider.minValue;
      flags.likelihoodTo = $scope.likelihoodSlider.maxValue;
      return {
        streets: streets,
        flags: flags
      };
    }

    function getActiveOptionalFlags() {
      var flags = {};
      $scope.radios.map(function (radio) {
        if (radio.enabled && radio.value !== null) {
          flags[radio.name] = radio.value;
        }
      });
      return flags;
    }

    $scope.getRatioCanvassed = function (numCanvassed, numVoters) {
      if (numCanvassed === 0 && numVoters === 0) {
        return 100;
      }
      return Math.round(numCanvassed / numVoters * 100);
    };

    $scope.onShowPopupMap = function (street) {
      var streetLabel = $filter('streetSingleLineFilter')(street);
      geoService.reverseLookupAddresses(extractStreetInfoForGeocoding([street]))
        .success(function (response) {
          if (!_.isEmpty(response) && !_.isEmpty(response[0].results)) {
            var geom = response[0].results[0].geometry;
            var map = {
              center: {
                latitude: geom.location.lat,
                longitude: geom.location.lng
              },
              options: {
                fullscreenControl: true,
                fullscreenControlOptions: {
                  position: google.maps.ControlPosition.RIGHT_TOP
                },
                rotateControl: true,
                scaleControl: true
              },
              zoom: 17,
              street: streetLabel,
              markers: [{
                id: 1,
                latitude: geom.location.lat,
                longitude: geom.location.lng,
                show: true,
                title: streetLabel
              }]
            };

            $uibModal.open({
              animation: true,
              templateUrl: 'mapModal.html',
              controller: 'mapModalInstanceCtrl',
              resolve: {
                mapData: function () {
                  return map;
                }
              }
            });
          } else {
            toastr.error('Sorry we were unable to geocode that street based on the address', 'No coordinates for address');
          }
        });
    };

    function extractStreetInfoForGeocoding(streets) {
      function extractPostcode(postcode) {
        if (!_.isEmpty(postcode.length)) {
          return _.head(postcode);
        } else {
          return "";
        }
      }

      return _.map(streets, function (street) {
        var postcode = extractPostcode(street.postcode);
        return [street.mainStreet, postcode, street.postTown, " UK"].join(" ");
      });
    }

    function resetErrors() {
      $scope.failedToLoadStreets = null;
      $scope.errorLoadingData = null;
    }
  });

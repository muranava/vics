angular
  .module('canvass')
  .controller('gotvController', function ($scope, wardService, electorService, $window) {

    $scope.numStreetsSelected = 0;
    $scope.validationErrors = [];

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

    /**
     * Model for radio buttons (it should be easier than this.
     * I was able to almost render the UI dynamically from a model, but wan't able to
     * get it to work with default values, so I went back to the primitive version - time).
     */
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

    $scope.onSearch = function () {
      wardService.findStreetsByWard($scope.ward.code)
        .success(function (streets) {
          $scope.streets = streets;
        });
    };

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
          $scope.streets = streets;
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
      doPrint($scope.ward.code, selected);
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
            console.log(error);
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

    function resetErrors() {
      $scope.failedToLoadStreets = null;
      $scope.errorLoadingData = null;
    }
  });

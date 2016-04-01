/**
 * Controller for the main canvass generator
 */
angular
  .module('canvass')
  .controller('canvassGeneratorController', function ($window, geoService, $scope, $timeout, $uibModal, wardService, constituencyService, electorService, $filter) {

      $scope.wards = [];
      $scope.constituencySearchModel = '';
      $scope.wardSearchModel = '';
      $scope.numStreetsSelected = 0;
      $scope.errorLoadingData = false;
      var streetsContainerPos = null;
      $scope.currentSort = "Street asc";

      $scope.onSelectConstituency = function () {
        resetErrors();
        $scope.streets = [];
      };

      $scope.onLoadedConstituencies = function (constituencies) {
        if (_.isEmpty(constituencies)) {
          $scope.userHasNoAssociations = true;
        }
        $scope.contentLoaded = true;
      };

      function determineIfPrintMenuDisplayed() {
        $scope.showSubMenu = streetsContainerPos && $window.scrollY > streetsContainerPos;
        if (!$scope.$$phase) {
          $scope.$apply();
        }
      }

      var debounce = _.debounce(determineIfPrintMenuDisplayed, 1, false);
      $("#canvassinputcontent").on('mousewheel', function () {
        debounce();
      });

      $scope.onSelectWard = function (model) {
        resetErrors();
        $scope.streets = [];
        $scope.numStreetsSelected = 0;

        $scope.wardSearchModel = model.ward;
        $scope.constituencySearchModel = model.constituency;

        wardService.findStreetsByWard($scope.wardSearchModel.code)
          .success(function (streets) {
            $scope.streets = _.map(streets, function (street) {
              street.numNotCanvassed = street.numVoters - street.numCanvassed;
              return street;
            });
            scrollToPrintSection();
            $timeout(function () {
              $('[data-toggle="tooltip"]').tooltip();
            });
          })
          .error(function () {
            $scope.errorLoadingStreets = true;
            scrollToNoVoters();
          });
      };

      function scrollToPrintSection() {
        _.defer(function () {
          streetsContainerPos = $('#streetsList').offset().top;
          $("html, body").animate({scrollTop: $('#printCards').offset().top - 140}, 500);
        });
      }

      function scrollToNoVoters() {
        _.defer(function () {
          $("html, body").animate({scrollTop: $('#noVoters').offset().top - 140}, 500);
        });
      }

      $scope.onNoAssociations = function () {
        $scope.userHasNoAssociations = true;
      };

      $scope.getNumStreetsSelected = function () {
        if ($scope.streets && $scope.streets.length) {
          $scope.numStreetsSelected = _.size(_.filter($scope.streets, function (s) {
            return s.selected;
          }));
        }
      };

      $scope.onShowMap = function (street) {
        var streetLabel = $filter('streetSingleLineFilter')(street);
        geoService.reverseLookupAddress(street.mainStreet + " " + street.postTown + " UK")
          .success(function (response) {

            if (!_.isEmpty(response.results)) {
              var geom = response.results[0].geometry;

              var mapData = {
                street: streetLabel,
                location: {
                  lat: geom.location.lat,
                  lng: geom.location.lng,
                  zoom: 16
                },
                markers: {
                  m1: {
                    lat: geom.location.lat,
                    lng: geom.location.lng
                  }
                }
              };

              $uibModal.open({
                animation: true,
                templateUrl: 'mapModal.html',
                controller: 'modalInstanceCtrl',
                resolve: {
                  mapData: function () {
                    return mapData;
                  }
                }
              });
            }

          });
      };

      $scope.onPrintSelected = function () {
        resetErrors();
        var selected = _.filter($scope.streets, function (s) {
          return s.selected;
        });
        electorService.retrievePdfOfElectorsByStreets($scope.wardSearchModel.code, {streets: selected})
          .success(function (response) {
            var file = new Blob([response], {type: 'application/pdf'});
            saveAs(file, $scope.wardSearchModel.code + '.pdf');
          })
          .error(function (error) {
            handleErrorResponse(error);
          });
      };

      /**
       * Sorts the streets by the given field
       * @param field - either [mainStreet, numVoters, numCanvassed]
       * @param direction - either asc or desc
       */
      $scope.sortStreets = function (field, direction, label) {
        $scope.currentSort = label;
        $scope.streets = _.orderBy($scope.streets, field, direction);
      };

      $scope.getRatioCanvassed = function(numCanvassed, numVoters) {
        if (numCanvassed === 0 && numVoters === 0) {
          return 100;
        }
        return Math.round(numCanvassed / numVoters * 100);
      };

      function handleErrorResponse(error) {
        if (error) {
          $scope.errorLoadingData = error.message;
        } else {
          $scope.errorLoadingData = "Failed to contact server";
        }
      }

      $scope.onPrintAll = function () {
        $scope.errorLoadingData = null;
        electorService.retrievePdfOfElectorsByStreets($scope.wardSearchModel.code, {streets: $scope.streets})
          .success(function (response) {
            var file = new Blob([response], {type: 'application/pdf'});
            saveAs(file, $scope.wardSearchModel.code + '.pdf');
          })
          .error(function (error) {
            handleErrorResponse(error);
          });
      };

      function resetErrors() {
        $scope.failedToLoadStreets = null;
        $scope.errorLoadingData = null;
      }
    }
  );

/**
 * Display map modal
 */
angular
  .module('canvass')
  .controller('modalInstanceCtrl', function ($scope, $uibModalInstance, mapData) {
    $scope.mapData = mapData;

    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };
  });

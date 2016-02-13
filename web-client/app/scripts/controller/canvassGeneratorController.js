
/**
 * Controller for the main canvass generator
 */
angular
  .module('canvass')
  .controller('canvassGeneratorController', function ($scope, wardService, constituencyService, apiUrl, electorService, electorTable, $filter) {

    $scope.wards = [];
    $scope.constituencySearchModel = '';
    $scope.wardSearchModel = '';
    $scope.numStreetsSelected = 0;
    $scope.errorLoadingData = false;

    $scope.loadingConstituencies = true;
    constituencyService.retrieveByUser()
      .success(function(response) {
        $scope.constituencies = response.constituencies;
      })
      .error(function() {
        $scope.errorLoadingData = true;
      });

    $scope.onSelectConstituency = function() {
      $scope.wardSearchModel = "";
      $scope.errorLoadingData = false;
      $scope.selectedConstituency = _.find($scope.constituencies, {name: $scope.constituencySearchModel});
      if (!$scope.selectedConstituency) {
        $scope.wards = [];
      } else {
        reloadWardsByConstituency();
      }
    };

    function reloadWardsByConstituency() {
      $scope.errorLoadingData = false;
      wardService.findWardsWithinConstituency($scope.selectedConstituency.id)
        .success(function (response) {
          $scope.wards = response.wards;
        })
        .error(function () {
          $scope.errorLoadingData = true;
        });
    }

    /**
     * Prints the electors in the selected wards and constituency
     */
    $scope.onPrintElectors = function () {
      $scope.wardNotSelectedError = false;
      var selectedWard = _.find($scope.wards, {name: $scope.wardSearchModel});
      if (selectedWard) {
        electorService.retrieveLocalElectorsByWards(selectedWard.code)
          .success(function (electors) {
            if (!_.isEmpty(electors)) {
              createPdf(electors);
            }
          });
      } else {
        $scope.wardNotSelectedError = true;
      }
    };



    $scope.onSearch = function() {
      $scope.errorLoadingData = false;
      $scope.streets = [];
      $scope.wardNotSelectedError = false;
      $scope.constituencyNotSelectedError = false;
      $scope.selectedWard = _.find($scope.wards, {name: $scope.wardSearchModel});
      $scope.selectedConstituency = _.find($scope.constituencies, {name: $scope.constituencySearchModel});
      if (!$scope.selectedConstituency) {
        $scope.constituencyNotSelectedError = true;
      } else {
        if ($scope.selectedWard) {
          wardService.findStreetsByWard($scope.selectedWard.code)
            .success(function (streets) {
              $scope.streets = streets;
            })
            .error(function(err) {
              $scope.errorLoadingData = true;
            })
        } else {
          $scope.wardNotSelectedError = true;
        }
      }
    };

    $scope.getNumStreetsSelected = function() {
      if ($scope.streets && $scope.streets.length) {
        $scope.numStreetsSelected = _.size(_.filter($scope.streets, function (s) {
          return s.selected;
        }));
      }
    };

    $scope.onPrintSelected = function() {
      $scope.errorLoadingData = false;
      var selected = _.filter($scope.streets, function (s) {
        return s.selected;
      });
      electorService.retrieveElectorsByStreets($scope.selectedWard.code, selected)
        .success(function(response) {
          createPdf(response);
        })
        .error(function() {
          $scope.errorLoadingData = true;
        });
    };

    $scope.onPrintAll = function() {
      $scope.errorLoadingData = false;
      electorService.retrieveElectorsByStreets($scope.selectedWard.code, $scope.streets)
        .success(function(response) {
          createPdf(response);
        })
        .error(function() {
          $scope.errorLoadingData = true;
        });
    };

    /**
     * Generates the pdf of the electors
     * @param {Object[]} electors - the electors in a given ward(s)
     */
    function createPdf(electors) {
      var doc = new jsPDF('l', 'pt');

      var currentPage = 1;
      _.forOwn(electors, function (electorsInStreet) {
        doc = electorTable.renderPdfTable({
          electors: electorsInStreet,
          wardName: $scope.selectedWard.name,
          wardCode: $scope.selectedWard.code,
          constituencyName: $scope.selectedConstituency.name,
          street: $filter('streetSingleLineFilter')(_.head(electorsInStreet.properties)),
          currentPage: currentPage
        }, doc);
        doc.addPage();
        currentPage += 1;
      });

      doc.save('wards.pdf');
    }
  });

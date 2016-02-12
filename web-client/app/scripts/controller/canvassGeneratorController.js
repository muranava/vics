
/**
 * Controller for the main canvass generator
 */
angular
  .module('canvass')
  .controller('canvassGeneratorController', function ($scope, wardService, constituencyService, apiUrl, electorService, electorTable) {

    $scope.wards = [];
    $scope.constituencySearchModel = '';
    $scope.wardSearchModel = '';
    $scope.numStreetsSelected = 0;

    constituencyService.retrieveByUser()
      .success(function(response) {
        $scope.constituencies = response.constituencies;
      });

    $scope.onSelectConstituency = function() {
      $scope.wardSearchModel = "";
      $scope.selectedConstituency = _.find($scope.constituencies, {name: $scope.constituencySearchModel});
      if (!$scope.selectedConstituency) {
        $scope.wards = [];
      } else {
        reloadWardsByConstituency();
      }
    };

    function reloadWardsByConstituency() {
      wardService.findWardsWithinConstituency($scope.selectedConstituency.id)
        .success(function(response) {
          $scope.wards = response.wards;
        })
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
              console.error(err);
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
      var selected = _.filter($scope.streets, function (s) {
        return s.selected;
      });
      electorService.retrieveElectorsByStreets(selected)
        .success(function(response) {
          console.log(response);
        });
    };

    /**
     * Generates the pdf of the electors
     * @param {Object[]} electors - the electors in a given ward(s)
     */
    function createPdf(electors) {
      var electorsGroupByStreet = _.groupBy(electors, function (e) {
        return e.street;
      });

      var doc = new jsPDF('l', 'pt');

      var currentPage = 1,
        totalPages = Object.keys(electorsGroupByStreet).length;
      _.forOwn(electorsGroupByStreet, function (electorsInStreet, street) {
        doc = electorTable.renderPdfTable({
          electors: electorsInStreet,
          wardName: 'Aldgate',
          constituencyName: 'Cities of London and Westminster',
          street: street,
          currentPage: currentPage,
          totalPages: totalPages
        }, doc);
        if (currentPage < totalPages) {
          doc.addPage();
        }
        currentPage += 1;
      });

      doc.save('wards.pdf');
    }
  });

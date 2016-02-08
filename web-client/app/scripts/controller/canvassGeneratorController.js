
/**
 * Controller for the main canvass generator
 */
angular
  .module('canvass')
  .controller('canvassGeneratorController', function ($scope, wardService, apiUrl, constituencies, electorService, electorTable) {
    "use strict";

    $scope.wards = [];
    $scope.constituencySearchModel = '';
    $scope.wardSearchModel = '';

    $scope.printSummaryCardModel = {
      constituency: '',
      wards: [],
      enabled: false
    };

    constituencies.findAllConstituencies()
      .success(function(response) {
        $scope.constituencies = response;
      });

    /**
     * Triggered when the the user matches a constituency (in the typeahead).
     * The action is to reload the wards to keep the control ward/const data in sync.
     */
    $scope.onSelectConstituency = function () {
      reloadWards();
    };

    function reloadWards() {
      wardService.findWardsWithinConstituency($scope.constituencySearchModel)
        .success(function (response) {
          $scope.wards = response;
        })
        .error(function (error) {
          console.error(error);
        });
    }

    /**
     * Searches for wards within the selected constituency
     */
    $scope.onSearchWards = function () {
      var selectedWard = _.find($scope.wards, {wardName: $scope.wardSearchModel});
      electorService.previewVotersByWards($scope.constituencySearchModel, [selectedWard.wardCode])
        .success(function (electors) {
          if (!_.isEmpty(electors)) {
            buildPreviewPanel(electors);
          } else {
            // TODO handle did not find wards
          }
        });
    };

    /**
     * Shows the selected wards and constituency in the preview panel
     */
    function buildPreviewPanel(wards) {
      $scope.printSummaryCardModel.enabled = true;
      var firstItem = _.head(wards);
      $scope.printSummaryCardModel.constituency = firstItem.constituencyCode;
    }

    /**
     * Prints the electors in the selected wards and constituency
     */
    $scope.onPrintElectors = function () {
      var wardCodes = _.map($scope.wards, function (ward) {
        return ward.wardCode;
      });

      electorService.retrieveLocalElectorsByWards(wardCodes)
        .success(function (electors) {
          if (!_.isEmpty(electors)) {
            createPdf(electors);
          } else {
            // TODO handle no results
          }
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

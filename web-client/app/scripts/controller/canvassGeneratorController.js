
/**
 * Controller for the main canvass generator
 */
angular
  .module('canvass')
  .controller('canvassGeneratorController', function ($scope, wardService, apiUrl, electorService, electorTable) {

    $scope.wards = [];
    $scope.constituencySearchModel = '';
    $scope.wardSearchModel = '';

    wardService.findAll()
      .success(function(response) {
        $scope.wards = response.wards;
        $scope.constituencies = _.uniqBy(_.map($scope.wards, function(ward) {
          return ward.constituency;
        }), 'name');
        prepareDefaultFormInputs();
      });

    function prepareDefaultFormInputs() {
      if ($scope.constituencies.length === 1) {
        $scope.constituencySearchModel = _.head($scope.constituencies).name;
        $scope.readOnlyConstituency = true;
      }
      if ($scope.wards.length === 1) {
        $scope.wardSearchModel = _.head($scope.wards).name;
        $scope.readOnlyWard = true;
      }
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

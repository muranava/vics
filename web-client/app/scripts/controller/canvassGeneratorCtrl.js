/**
 * Controller for the main canvass generator
 */
angular
    .module('canvass')
    .controller('canvassGeneratorCtrl', function ($scope, wardService, apiUrl, constituencies, electorService, electorTable) {

            $scope.constituencies = constituencies;
            $scope.wards = [];
            $scope.constituencySearchModel = '';
            $scope.wardSearchModel = '';

            $scope.printSummaryCardModel = {
                constituency: '',
                wards: [],
                enabled: false
            };

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
                electorService.previewVotersByWards($scope.constituencySearchModel, [$scope.wardSearchModel])
                    .success(function (preview) {
                        if (!_.isEmpty(preview.wards)) {
                            buildPreviewPanel(preview);
                        } else {
                            // TODO handle did not find wards
                        }
                    });
            };

            /**
             * Shows the selected wards and constituency in the preview panel
             */
            function buildPreviewPanel(preview) {
                var wards = preview.wards;
                $scope.printSummaryCardModel.enabled = true;
                $scope.printSummaryCardModel.wards = preview.wards;
                var firstItem = _.head(wards);
                $scope.printSummaryCardModel.constituency = firstItem.constituencyCode + ' - ' + firstItem.constituencyName;
            }

            /**
             * Prints the electors in the selected wards and constituency
             */
            $scope.onPrintElectors = function () {
                var wardCodes = _.map($scope.printSummaryCardModel.wards, function (ward) {
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
                var electorsGroupByStreet = _.groupBy(electors, function(e) {
                    return e.street;
                });

                var wardNames = _.map($scope.printSummaryCardModel.wards, function(ward) {
                    return ward.wardName;
                });

                var doc = new jsPDF('l', 'pt'),
                    wardName = wardNames.join(',');

                var currentPage = 1,
                    totalPages = Object.keys(electorsGroupByStreet).length;
                _.forOwn(electorsGroupByStreet, function(electorsInStreet, street) {
                    doc = electorTable.renderPdfTable({
                        electors: electorsInStreet,
                        wardName: wardName,
                        wardCode: $scope.printSummaryCardModel.wards[0].wardCode,
                        constituencyName: $scope.printSummaryCardModel.constituency,
                        street: street,
                        currentPage: currentPage,
                        totalPages: totalPages
                    }, doc);
                    if (currentPage < totalPages) {
                        doc.addPage();
                    }
                    currentPage += 1;
                });

                doc.save('merged.pdf');
            }
        });

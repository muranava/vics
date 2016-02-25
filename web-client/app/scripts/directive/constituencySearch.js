/**
 * Directive that searches constituencies based on user input bound to a model
 * (uses a typeahead internally)
 */
angular
  .module('canvass')
  .directive('constituencySearch', function (constituencyService, wardService) {
    return {
      templateUrl: 'views/partials/constituencysearch.html',
      restrict: 'EA',
      scope: {
        onSelect: '&'
      },
      link: function (scope) {

        constituencyService.retrieveByUser()
          .success(function (response) {
            scope.constituencies = response.constituencies;
          });

        scope.onSelect = scope.onSelect();

        scope.onSelectInternal = function () {
          wardService.findWardsWithinConstituency(scope.directiveModel.constituency.id)
            .success(function (response) {
              scope.wards = response.wards;
            });
        };
      }
    };
  });

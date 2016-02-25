
/**
 * Directive that searches constituencies then wards based on user input bound to a model
 * (uses a typeahead internally)
 */
angular
  .module('canvass')
  .directive('wardSearch', function (constituencyService, wardService) {
    return {
      templateUrl: 'views/partials/wardsearch.html',
      restrict: 'EA',
      scope: {
        onSelect: '&'
      },
      link: function (scope) {
        scope.onSelect = scope.onSelect();

        // preload the constituencies associated to the active user
        constituencyService.retrieveByUser()
          .success(function (response) {
            scope.constituencies = response.constituencies;
          });

        /**
         * Reloads wards when a constituency typeahead is matched
         */
        scope.onSelectConstituencyInternal = function () {
          wardService.findWardsWithinConstituency(scope.directiveModel.constituency.id)
            .success(function (response) {
              scope.wards = response.wards;
            });
        };

        /**
         * Triggered when the user selects searched
         */
        scope.onSelectWardInternal = function() {
          if (scope.directiveModel.ward && scope.directiveModel.ward.id) {
            scope.onSelect(scope.directiveModel.ward);
          }
        };

      }
    };
  });

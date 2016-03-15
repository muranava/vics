
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
        onSelect: '&',
        onLoadedConstituencies: '&'
      },
      link: function (scope) {
        scope.onSelect = scope.onSelect();
        scope.onLoadedConstituencies = scope.onLoadedConstituencies();

        // preload the constituencies associated to the active user
        constituencyService.retrieveByUser()
          .success(function (response) {
            scope.constituencies = response.constituencies;
            scope.onLoadedConstituencies(scope.constituencies);
          });

        /**
         * Reloads wards when a constituency typeahead is matched
         */
        scope.onSelectConstituencyInternal = function () {
          scope.directiveModel.ward = '';
          wardService.findWardsWithinConstituency(scope.directiveModel.constituency.id)
            .success(function (response) {
              scope.wards = response.wards;
              $('#wardInputID').focus();
            });
        };

        /**
         * Triggered when the user selects searched
         */
        scope.onSelectWardInternal = function() {
          if (scope.directiveModel.ward && scope.directiveModel.ward.id) {
            scope.onSelect(scope.directiveModel);
          }
        };

      }
    };
  });

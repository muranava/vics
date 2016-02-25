
angular
  .module('canvass')
  .controller('gotvController', function($scope, config) {
    var apiUrl = config.apiUrl;

    $scope.constituencyId = '0d338b99-3d15-44f7-904f-3ebc18a7ab4a';

    $scope.onConstituencySelect = function(c) {
      console.log(c);
    };

  });

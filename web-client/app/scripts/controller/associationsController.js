angular
  .module('canvass')
  .controller('associationsController', function ($routeParams, $scope, $location, userService, constituencyService, wardService) {
    var searchLimit = 50;

    if (!$routeParams.id) {
      $location.path("/dashboard");
    }

    function loadUser() {
      userService.findByID($routeParams.id)
        .success(function(response) {
          $scope.user = response;
        })
        .error(function() {
          $location.path("/dashboard");
        });
    }
    loadUser();

    $scope.onConstituencyInputKeypress = function() {
      $scope.invalidConstituency = false;
      constituencyService.search($scope.constituencySearchModel, searchLimit)
        .success(function(response) {
          $scope.constituencies = response;
        });
    };

    $scope.onWardInputKeypress = function() {
      $scope.invalidWard = false;
      wardService.search($scope.wardSearchModel, searchLimit)
        .success(function(response) {
          $scope.wards = response;
        });
    };

    $scope.onAddConstituency = function() {
      clearMessages();

      var selected = _.find($scope.constituencies, function(c) {
        return c.id === $scope.constituencySearchModel.id;
      });

      if (selected) {
        constituencyService.associateToUser(selected.id, $scope.user.id)
          .success(function() {
            $scope.constituencySearchModel = "";
            loadUser();
          })
          .error(function() {
            $scope.failedToAssociateUser = true;
          });
      } else {
        $scope.invalidConstituency = true;
      }
    };

    $scope.onAddWard = function() {
      clearMessages();

      var selectedWard = _.find($scope.wards, function(ward) {
        return ward.id === $scope.wardSearchModel.id;
      });
      if (selectedWard) {
        wardService.associateToUser(selectedWard.id, $scope.user.id)
          .success(function() {
            $scope.wardSearchModel = "";
            loadUser();
          })
          .error(function() {
            $scope.failedToAssociateWard = true;
          });
      } else {
        $scope.invalidWard = true;
      }
    };

    $scope.onDeleteConstituency = function(id) {
      clearMessages();
      constituencyService.removeUserAssociation(id, $scope.user.id)
        .success(function() {
          loadUser();
        })
        .error(function() {
          $scope.failedToDeleteConstituency = true;
        });
    };

    $scope.onDeleteWard = function(id) {
      clearMessages();
      wardService.removeUserAssociation(id, $scope.user.id)
        .success(function() {
          loadUser();
        })
        .error(function() {
          $scope.failedToDeleteWard = true;
        });
    };

    function clearMessages() {
      $scope.invalidWard = false;
      $scope.invalidConstituency = false;
      $scope.failedToDeleteConstituency = false;
      $scope.failedToDeleteWard = false;
    }
  });

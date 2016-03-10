angular
  .module('canvass')
  .controller('csvUploadController', function ($scope, userService, util) {

    $scope.csvUser = {
      content: null,
      header: true,
      headerVisible: true,
      separator: ',',
      separatorVisible: true,
      result: null,
      encoding: 'ISO-8859-1'
    };

    $scope.onCreateUsers = function () {
      if (!_.isEmpty($scope.csvUser.result)) {
        _.each($scope.csvUser.result, function(elem) {
          var modelWithPassword = mapToRequest(elem);

          userService.create(modelWithPassword)
            .success(function() {
              var found = _.find($scope.csvUser.result, {username: modelWithPassword.username});
              found.outcome = modelWithPassword.password;
            })
            .error(function() {
              var found = _.find($scope.csvUser.result, {username: modelWithPassword.username});
              found.outcome = "Failed";
              if (!$scope.$$phase) {
                $scope.$apply();
              }
            });
        });
      }
    };

    function mapToRequest(model) {
      var password = util.generatePassword();
      return {
        username: model.username,
        firstName:  model.first_name,
        lastName:  model.last_name,
        password: password,
        repeatPassword:  password,
        role: "USER",
        writeAccess:  model.write_access ? true : false
      };
    }
  });


angular
  .module('canvass')
  .controller('adminUserController', function (util, $timeout, $scope, userService, $location) {

    $scope.editMode = false;
    $scope.createdUserEmail = '';
    $scope.validationErrors = [];

    function initCreateUserModel() {
      return {
        username: '',
        firstName: '',
        lastName: '',
        role: 'USER',
        password: '',
        repeatPassword: '',
        writeAccess: false
      };
    }
    $scope.createUserModel = initCreateUserModel();

    $scope.setCreateUserMode = function(enabled) {
      $scope.createUserMode = enabled;
    };

    _.defer(function() {
      $(function () {
        $('[data-toggle="tooltip"]').tooltip();
      });
    });

    $scope.onConfirmDeleteUser = function (user) {
      clearMessages();
      if (user.role === 'ADMIN') {
        return;
      }

      userService.delete(user.id)
        .success(function() {
          loadUsers();
        })
        .error(function() {
          $scope.deleteUserFailed = true;
        });
    };

    $scope.onCsvUpload = function() {
      $location.path('/csvupload');
    };

    $scope.onEditUser = function(userID) {
      clearMessages();
      $scope.editMode = true;
      $scope.editUser = $.extend(true, {}, _.find($scope.users, {id: userID}));
      $scope.editUser.writeAccess = $scope.editUser.writeAccess || false;
    };

    $scope.onSaveEdit = function(user) {
      clearMessages();
      $scope.editErrors = validateCreateUserForm(user, false);
      if (!$scope.editErrors.length) {
        userService
          .update(user)
          .success(function () {
            $scope.editUser = null;
            $scope.editMode = false;
            loadUsers();
          })
          .error(function (err) {
            if (err && _.includes(err.message, 'exists')) {
              $scope.userExistsError = true;
            }
          });
      }
    };


    $scope.onCancelEdit = function() {
      $scope.editMode = false;
      $scope.editUser = null;
    };

    $scope.onCreateUser = function (user) {
      clearMessages();
      $scope.validationErrors = validateCreateUserForm(user, true);
      if (!$scope.validationErrors.length) {
        userService
          .create(user)
          .success(function () {
            $scope.createUserModel = initCreateUserModel();
            $scope.successfullyCreatedUser = true;
            $scope.createdUserEmail = user.username;
            loadUsers();
          })
          .error(function (err) {
            if (err && _.includes(err.message, 'exists')) {
              $scope.userExistsError = true;
            }
          });
      }
    };

    $scope.preDeleteUser = function(user) {
      $scope.userToDelete = user;
    };

    function clearMessages() {
      $scope.deleteUserFailed = false;
      $scope.successfullyCreatedUser = false;
      $scope.userExistsError = false;
    }

    function validateCreateUserForm(user, includePasswords) {
      var errors = [];

      if (includePasswords && (user.password !== user.repeatPassword)) {
        errors.push('Passwords do not match');
      }

      if (includePasswords && user.password.length < 12) {
        errors.push('Password must be at least 12 characters');
      }

      if (includePasswords && !(/\d/.test(user.password))) {
        errors.push('Password must contain a number');
      }

      if (includePasswords && !(/[A-Z]/.test(user.password))) {
        errors.push('Password must contain an uppercase character');
      }

      if (!util.isValidEmail(user.username)) {
        errors.push('Email is not valid');
      }

      if (_.isEmpty(user.firstName)) {
        errors.push('First name must not be empty');
      }

      return errors;
    }

    function loadUsers() {
      userService.retrieveAllUsers()
        .success(function (response) {
          $scope.users = response;
        });
    }

    $scope.randomPassword = function (user) {
      var pw = util.generatePassword();
      user.password = pw;
      user.repeatPassword = pw;
    };

    loadUsers();
  });


angular
  .module('canvass')
  .controller('adminUserController', function (util, $timeout, $scope, userService, plugins) {

    $scope.createdUserEmail = '';
    $scope.createUserModel = initCreateUserModel();
    $scope.validationErrors = [];

    function initCreateUserModel() {
      return {
        email: '',
        firstName: '',
        lastName: '',
        role: 'USER',
        wardIDs: [],
        constituencyIDs: [],
        password: '',
        repeatPassword: '',
        writeAccess: false
      };
    }

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

    $scope.onCreateUser = function (user) {
      clearMessages();
      $scope.validationErrors = validateCreateUserForm(user);
      if (!$scope.validationErrors.length) {
        userService
          .create(user)
          .success(function () {
            $scope.createUserModel = initCreateUserModel();
            $scope.successfullyCreatedUser = true;
            $scope.createdUserEmail = user.email;
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

    function validateCreateUserForm(user) {
      var errors = [];

      if (user.password !== user.repeatPassword) {
        errors.push('Passwords do not match');
      }

      if (user.password.length < 8) {
        errors.push('Password must be at least 8 characters');
      }

      if (!(/\d/.test(user.password))) {
        errors.push('Password must contain a number');
      }

      if (!(/[A-Z]/.test(user.password))) {
        errors.push('Password must contain an uppercase character');
      }

      if (!util.isValidEmail(user.email)) {
        errors.push('Email is not valid')
      }

      if (_.isEmpty(user.firstName)) {
        errors.push('First name must not be empty')
      }

      return errors;
    }

    function loadUsers() {
      userService.retrieveAllUsers()
        .success(function (response) {
          $scope.users = response;
        });
    }

    $scope.randomPassword = function () {
      var pw = util.generatePassword();
      $scope.createUserModel.password = pw;
      $scope.createUserModel.repeatPassword = pw;

      // workaround for triggering floating labels with angular
      $("#pw1").addClass('dirty');
      $("#pw2").addClass('dirty');
    };

    loadUsers();

    plugins.initFloatingLabels();
    plugins.initValidation();
  });

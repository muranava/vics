/**
 * Filter to perform text presentation layer transformations
 */
angular
  .module('canvass')
  .filter('wardFormat', function () {
    return function (ward) {
      if (_.isEmpty(ward)) {
        return '';
      }
      return ward.wardCode + ' - ' + ward.wardName;
    };
  })
  .filter('userRoleLabel', function () {
    return function(user) {
      if (_.isEmpty(user)) {
        return '';
      }
      if (_.some(user.authorities, {authority: 'ADMIN'})) {
        return 'Administrator';
      } else {
        return '';
      }
    };
  });

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
    return function (user) {
      if (_.isEmpty(user)) {
        return '';
      }
      if (_.some(user.authorities, {authority: 'ADMIN'})) {
        return 'Administrator';
      } else {
        return '';
      }
    };
  })
  .filter('streetSingleLineFilter', function () {
    function toTitleCase(str) {
      return str.replace(/\w\S*/g, function (txt) {
        return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
      });
    }

    return function (address) {
      if (_.isEmpty(address)) {
        return '';
      }
      var parts = [address.mainStreet, address.dependentStreet, address.dependentLocality, toTitleCase(address.postTown.toLowerCase())];
      return _
        .chain(parts)
        .filter(function (part) {
          return !_.isEmpty(part);
        })
        .join(', ')
        .value();
    };
  });

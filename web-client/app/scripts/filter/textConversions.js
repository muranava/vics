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
  .filter('voterAddressFormat', function() {
    return function(voter) {
      var address = voter.address;
      var parts = [address.sub_building_name, address.building_name, address.dependent_street, address.main_street, _.capitalize(address.post_town)];
      var presentParts =_.filter(parts, function(part) {
        return !_.isEmpty(part);
      });
      return presentParts.join(", ");
    };
  })
  .filter('ernToShortForm', function() {
    return function (longForm) {
      if (_.isString(longForm)) {
        var parts = longForm.split("-");
        parts.shift();
        return parts.join("-");
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

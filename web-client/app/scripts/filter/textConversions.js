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
        }
    });

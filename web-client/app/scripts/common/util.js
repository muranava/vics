angular
  .module('canvass')
  .service('util', function () {
    var api = {};

    api.uuid = function () {
      function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
          .toString(16)
          .substring(1);
      }

      return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
        s4() + '-' + s4() + s4() + s4();
    };

    api.isValidEmail = function (email) {
      var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    };

    api.generatePassword = function () {
      var chars = "abcdefghjknopqrstuvwxyz=@£",
        uppers = "ABCDEFGHJKMNPQRSTUVWXYZ",
        numbers = "23456789",
        password = "";
      for (var i = 0; i < 3; ++i) {
        password += _.sample(chars);
      }
      for (var k = 0; k < 3; ++k) {
        password += _.sample(uppers);
      }
      for (var j = 0; j < 2; j++) {
        password += _.sample(numbers);
      }
      return _.shuffle(password).join("");
    };

    return api;
  });

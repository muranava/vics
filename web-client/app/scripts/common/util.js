angular
  .module('canvass')
  .service('util', function () {
    var api = {},
    passwordLength = 12,
      chars = "abcdefghjknopqrstuvwxyz=@£!&%",
      uppers = "ABCDEFGHJKMNPQRSTUVWXYZ",
      numbers = "23456789",
      emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;


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
      return emailRegex.test(email);
    };

    api.generatePassword = function () {
      var password = "";
      password += _.sample(chars);
      password += _.sample(uppers);
      password += _.sample(numbers);

      var all = chars + uppers + numbers;
      for (var i = 0; i < passwordLength - 3; ++i) {
        password += _.sample(all);
      }
      return _.shuffle(password).join("");
    };

    return api;
  });

module.exports = function Routes(providers, config) {

	var User = require("./user.js");
	var Util = require("./util.js");

    this.user = new User(providers.users);
	this.util = new Util();
};

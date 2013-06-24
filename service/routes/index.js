module.exports = function Routes(providers, config) {

	var User = require("./user.js").User;
	var Util = require("./util.js").Util;

    this.user = new User(providers.users);
	this.util = new Util();
};

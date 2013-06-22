
function User(userProvider) {
	this.userProvider = userProvider;
};

User.prototype.authorize = function (request, response) {
	var username = request.body.username;
	var code = request.body.code;
	var password = request.body.password;
	this.userProvider.findByUsername(username, function (error, user) {
		if (error) console.log(error);
		else {
			if (user.code == code) {
				// TODO: generate the token from the salt and hash of the password
				// TODO: store the token in the db.tokens collection and assign expiration date
				var token = '8675309';
				var salt = 'fresh from the sea of random';
				var hash = 'computed from password plus salt';
				user.salt = salt;
				user.hash = hash;
				this.userProvider.update(user, function (error) {
					if (error) console.log(error);
				});

				response.statusCode = 200;
				response.setHeader('Content-Type', 'application/json');
				response.end(JSON.stringify({ token: token }));
			} else {
				response.statusCode = 401;
				response.setHeader('WWW-Authenticate', 'Basic realm="Secure Area"');
				response.end();
			}
		}
	});
};

User.prototype.create = function (request, response) {
	// TODO: check for admin authorization
	var user = request.body.user;
	// TODO: validate the JSON against the schema
	this.userProvider.create(user, function (error, result) {
		if (error) console.log(error);
		else {
			// TODO: stringify the result._id 
			var id = result._id;
			console.log("User.create: id = " + id);
			//response.redirect('/users/' + id);
		}
	});
	console.log("User.create: exit");
};

User.prototype.load = function (request, response, next) {
	// TODO: check for authorization
	var username = request.params.username;
	this.userProvider.findByUsername(username, function (error, user) {
		if (error) console.log(error);
		else {
			request.user = user;
		}
	});
	next();
};

User.prototype.view = function (request, response) {
	var user = request.user;
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify(user));
};

User.prototype.edit = function (request, response) {
	var user = request.user;
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify(user));
};

User.prototype.update = function (request, response) {
	var user = request.body.user;
	// TODO: validate the JSON against the schema
	this.userProvider.update(user, function (error) {
		if (error) console.log(error);
	});
	request.user = user;
	
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
	response.redirect('back');
};

User.prototype.remove = function (request, response) {
	// TODO: check for admin authorization
	var user = request.user;
	this.userProvider.remove(user, function (error) {
		if (error) console.log(error);
	});

	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
};

module.exports = User;

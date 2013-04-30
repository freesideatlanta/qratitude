exports.authorize = function (request, response) {
	// TODO: generate the token from the salt and hash of the password
	response.send("8675309");
};

exports.create = function (request, response) {
	// TODO: check for admin authorization
	var user = request.body.user;
	// TODO: validate attributes
	// TODO: store in database via the data provider
};

exports.load = function (request, response, next) {
	// TODO: check for authorization
	var id = request.params.id;
	// TODO: fetch the user from the data provider
	request.user = { id: 0, username: 'zerocool', password: 'GOD', name: 'Dade Murphy', email: 'zerocool@hackerspaces.org' };
	next();
};

exports.view = function (request, response) {
	var user = request.user;
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify(user));
};

exports.edit = function (request, response) {
	var user = request.user;
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify(user));
};

exports.update = function (request, response) {
	var user = request.body.user;
	// TODO: pass in the whole JSON update object to the data provider
	request.user.username = user.username;
	request.user.password = user.password;
	request.user.name = user.name;
	request.user.email = user.email;
	
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
	response.redirect('back');
};

exports.remove = function (request, response) {
	// TODO: check for admin authorization
	var user = request.user;
	// TODO: delete the user via the data provider
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
};

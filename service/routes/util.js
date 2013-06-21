
function Util() { };

Util.prototype.ensureAuthenticated = function (request, response, next) {
	if (request.isAuthenticated()) {
		return next();
	}
	response.redirect('/login');
};

Util.prototype.login = function (request, response) {
	if (!request.user) {
		response.redirect('/login');
	} else {
		var id = request.user.id;
		response.redirect('/users' + id);
	}
};

Util.prototype.authenticate = function (request, response) {
	passport.authenticate('local', { successRedirect: '/', failureRedirect: '/login' });
};

Util.prototype.logout = function (request, response) {
	request.logout();
	response.redirect('/');
};

module.exports = Util;

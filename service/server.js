var restify = require('restify');
var passport = require('passport');
var LocalStrategy = require('passport-local').Strategy;

function Server(config, routes, providers) {

    var srv = restify.createServer();

    this.routes     = routes;
    this.providers  = providers;
    this.port       = config.port;
    this.srv        = srv;

    // Routes
	var util = routes.util;
	var user = routes.user;

    // Plugins
    srv.use(restify.queryParser());
    srv.use(restify.bodyParser());

	srv.use(passport.initialize());
	srv.use(passport.session());

    // Common handlers
    srv.use(function (request, response, next) {
        next();
    });

	srv.get('/login', [
			function (request, response, next) {
				providers.users.findByUsername("emptyset");
				next();
			},
			util.login]);
	srv.post('/login', util.authenticate);
	srv.get('/logout', util.logout);

	srv.get('/users', [
			function (request, response, next) { 
				console.log("setting the body user parameter"); 
				request.body.user = { username: 'emptyset', password: 'temp' }; 
				console.log("user.create = " + user.create);
				console.log("routes.user = " + routes.user);
				console.log("providers.users = " + providers.users);
				next(); 
			}, 
			user.create
		]
	);
	srv.get('/users/:id/', [util.ensureAuthenticated, user.load, user.view]);

}

Server.prototype.listen = function(callback) {
    this.srv.listen(process.env.PORT || this.port, callback);
}

module.exports = Server;

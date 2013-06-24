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
	/*
    var user  = routes.user
      , asset = routes.asset
      , photo = routes.photo;
	*/
	var util = routes.util;
	var user = routes.user;

    // Plugins
    srv.use(restify.queryParser());
    srv.use(restify.bodyParser());

	srv.use(passport.initialize());
	srv.use(passport.session());

    // Common handlers
    srv.use(function (request, response, next) {
        request.assetProvider = providers.assets;
        next();
    });

	srv.get('/login', [
			providers.users.findByUsername("emptyset"),
			util.login]);
	srv.post('/login', util.authenticate);
	srv.get('/logout', util.logout);

	srv.get('/users', [
			function (request, response, next) { 
				console.log("setting the body user parameter"); 
				request.body.user = { username: 'emptyset', password: 'temp'  }; 
				next(); }, 
			user.create]);
	srv.get('/users/:id/', [util.ensureAuthenticated, user.load, user.view]);

/*
    srv.post('/user', user.create);
    srv.post('/user/authorize', user.authorize);
    srv.get('/user/:username/view', [user.load, user.view]);
    srv.get('/user/:id/edit', [user.load, user.edit]);
    srv.put('/user/:id/edit', [user.load, user.update]);
    srv.del('/user/:id', [user.load, user.remove]);

    srv.post('/asset', asset.create);
    srv.get('/asset/search/:tag', asset.search);
    srv.get('/asset/:id', [asset.load, asset.view]);
    srv.get('/asset/:id/view', [asset.load, asset.view]);
    srv.get('/asset/:id/edit', [asset.load, asset.edit]);
    srv.put('/asset/:id/edit', [asset.load, asset.update]);
    srv.del('/asset/:id', [asset.load, asset.remove]);

    srv.post('photo', photo.create);
    srv.get('photo/:id', [photo.load, photo.view]);
    srv.get('photo/:id/view', [photo.load, photo.view]);
    srv.del('/photo/:id', [photo.load, photo.remove]);
*/
}

Server.prototype.listen = function(callback) {
    this.srv.listen(process.env.PORT || this.port, callback);
}

module.exports = Server;

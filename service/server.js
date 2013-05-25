var restify = require('restify');

function Server(config, routes, providers) {

    var srv = restify.createServer();

    this.routes     = routes;
    this.providers  = providers;
    this.port       = config.port;
    this.srv        = srv;

    // Routes
    var user  = routes.user
      , asset = routes.asset
      , photo = routes.photo;

    // Plugins
    srv.use(restify.queryParser());
    srv.use(restify.bodyParser());

    // Common handlers
    srv.use(function (request, response, next) {
        request.assetProvider = providers.assets;
        next();
    });

    srv.post('/user', user.create);
    srv.post('/user/authorize', user.authorize);
    srv.get('/user/:username/view', [user.load, user.view]);
    srv.get('/user/:id/edit', [user.load, user.edit]);
    srv.put('/user/:id/edit', [user.load, user.update]);
    srv.del('/user/:id', [user.load,user.remove]);

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
}

Server.prototype.listen = function(callback) {
    this.srv.listen(process.env.PORT || this.port, callback);
}

module.exports = Server;

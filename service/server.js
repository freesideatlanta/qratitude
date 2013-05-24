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


    // Request handlers
    srv.use(function (request, response, next) {
        request.assetProvider = providers.assets;
        next();
    });


    srv.post('/user/authorize', user.authorize);
    srv.post('/user', user.create);

    // Restify server has no method 'all'. See Alan.
    // srv.all('/user/:username/:op?', user.load);
    
    srv.get('/user/:username/view', user.view);
    srv.get('/user/:id/edit', user.edit);
    srv.put('/user/:id/edit', user.update);
    srv.del('/user/:id', user.remove);

    srv.get('/asset/search/:tag', asset.search);
    srv.post('/asset', asset.create);

    // See above
    // srv.all('/asset/:id/:op?', asset.load);
    
    srv.get('/asset/:id', asset.view);
    srv.get('/asset/:id/view', asset.view);
    srv.get('/asset/:id/edit', asset.edit);
    srv.put('/asset/:id/edit', asset.update);
    srv.del('/asset/:id', asset.remove);

    srv.post('photo', photo.create);
    // srv.all('photo/:id/:op?', photo.load);
    srv.get('photo/:id', photo.view);
    srv.get('photo/:id/view', photo.view);
    srv.del('/photo/:id', photo.remove);
}

Server.prototype.listen = function(callback) {
    this.srv.listen(process.env.PORT || this.port, callback);
}

module.exports = Server;

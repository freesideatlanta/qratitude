
var express = require('express')
  , routes = require('./routes')
  , photo = require('./routes/photo')
  , http = require('http')
  , path = require('path')
  , fs = require('fs')
  , util = require('util')
  , filepath = __dirname;

var Config = require('./config');

var UserProvider = require('./userProvider').UserProvider;
var userProvider = new UserProvider(Config.db.name, Config.db.host, Config.db.port);
var AssetProvider = require('./assetProvider');
var assetProvider = new AssetProvider(Config.db.name, Config.db.host, Config.db.port);

var User = require('./routes/user').User;
var user = new User(userProvider);
var Asset = require('./routes/asset');
var asset = new Asset();

var app = express();

// all environments
app.set('port', process.env.PORT || 3000);
app.set('views', __dirname + '/views');
app.set('view engine', 'jade');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(express.cookieParser('taxidermy nutria'));
app.use(express.session());
app.use(function (request, response, next) {
	request.assetProvider = assetProvider;
	next();
});
app.use(app.router);
app.use(require('stylus').middleware(__dirname + '/public'));
app.use(express.static(path.join(__dirname, 'public')));

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

// user web methods
app.post('/user/authorize', user.authorize);
app.post('/user', user.create);
app.all('/user/:username/:op?', user.load);
app.get('/user/:username/view', user.view);
app.get('/user/:id/edit', user.edit);
app.put('/user/:id/edit', user.update);
app.delete('/user/:id', user.remove);

// asset web methods
app.get('/asset/search/:tag', asset.search);
app.post('/asset', asset.create);
app.all('/asset/:id/:op?', asset.load);
app.get('/asset/:id', asset.view);
app.get('/asset/:id/view', asset.view);
app.get('/asset/:id/edit', asset.edit);
app.put('/asset/:id/edit', asset.update);
app.delete('/asset/:id', asset.remove);

// photo web methods
app.post('photo', photo.create);
app.all('photo/:id/:op?', photo.load);
app.get('photo/:id', photo.view);
app.get('photo/:id/view', photo.view);
app.delete('/photo/:id', photo.remove);

app.get('/', routes.index);

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});

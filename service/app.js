
var express = require('express')
  , routes = require('./routes')
  , asset = require('./routes/asset')
  , photo = require('./routes/photo')
  , http = require('http')
  , path = require('path')
  , fs = require('fs')
  , util = require('util')
  , filepath = __dirname;

var Config = require('./config');

var UserProvider = require('./userProvider').UserProvider;
var userProvider = new UserProvider(Config.db.name, Config.db.host, Config.db.port);
var AssetProvider = require('./assetProvider').AssetProvider;
var assetProvider = new AssetProvider(Config.db.name, Config.db.host, Config.db.port);

var User = require('./routes/user').User;
var user = new User(userProvider);
var Asset = require('./routes/asset').Asset;
var asset = new Asset(assetProvider);

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
app.use(app.router);
app.use(require('stylus').middleware(__dirname + '/public'));
app.use(express.static(path.join(__dirname, 'public')));

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

// user web methods
app.post('/user/authorize', user.authorize);

// asset web methods
app.get('/asset/:tag', asset.search);
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
app.delete('/asset/:id', photo.remove);

app.get('/', routes.index);

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});

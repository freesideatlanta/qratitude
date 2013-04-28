
var express = require('express')
  , routes = require('./routes')
  , user = require('./routes/user')
  , asset = require('./routes/asset')
  , http = require('http')
  , path = require('path')
  , fs = require('fs')
  , util = require('util')
  , filepath = __dirname;
var ProductProvider = require('./productprovider-memory').ProductProvider;

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

var productProvider = new ProductProvider();

app.get('/', function(req, res) {
	productProvider.findAll(function (error, docs) {
		res.send(docs);
	});
})

app.get('/auth', function (request, response) {
	response.send("login page");
})
app.post('/auth', function (request, response) {
	console.log("sending back the token on the response...");
	response.send("8675309");
})

app.post('/asset', asset.create);
app.all('/asset/:id/:op?', asset.load);
app.get('/asset/:id', asset.view);
app.get('/asset/:id/view', asset.view);
app.get('/asset/:id/edit', asset.edit);
app.put('/asset/:id/edit', asset.update);
app.delete('/asset/:id', asset.remove);

/*
app.get('/asset', function (request, response) {
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ name: 'Blue Thing', description: 'This thing is particularly blue' }));
	response.send();
})
app.post('/asset', function (request, response) {
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
	response.send();
})
app.put('/asset', function (request, response) {
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
	response.send();
})
app.delete('/asset', function (request, response) {
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
	response.send();
});
*/

app.get('/photo', function (request, response) {
	var f = filepath + '/gentleman.jpg';
	response.writeHead(200, { 'Content-Type': 'image/jpeg' });
	fs.readFile(f, function (err, data) { 
		if (err) throw err;
		response.write(data);
	});
	response.send();
})
app.post('/photo', function (request, response) {
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
	response.send();
});
app.delete('/photo', function (request, response) {
	response.writeHead(200, { 'Content-Type': 'application/json' });
	response.write(JSON.stringify({ status: 'OK' }));
	response.send();
});

app.get('/', routes.index);
app.get('/users', user.list);

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});

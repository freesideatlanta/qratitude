var restify = require('restify');
var json    = require('./asset.json');

var client = restify.createJsonClient({
    url: 'http://localhost:3000'
});

client.post('/asset', json, function(err, req, res, obj) {
    assert.ifError(err);
    console.log('%d -> %j', res.statusCode, res.headers);
    console.log('%j', obj);
});

client.get('/asset', function(err, req, res, obj){
    assert.ifError(err);
    console.log(JSON.stringify(obj,null,2));
});

var restify = require('restify');
var assert = require('assert');

var client = restify.createJsonClient({
	url: 'http://localhost:3000',
	version: '*'
});

describe('service: /login endpoint', function() {

});

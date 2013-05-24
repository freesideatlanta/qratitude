var productCounter = 1;

ProductProvider = function() {};
ProductProvider.prototype.dummyData = [];

ProductProvider.prototype.findAll = function(callback) {
	callback(null, this.dummyData)
};

ProductProvider.prototype.findById = function(id, callback) {
	var result = null;
	for (var i = 0; i < this.dummyData.length; i++) {
		if (this.dummyData[i].product_id == id) {
			result = this.dummyData[i];
			break;
		}
	}
	callback(null, result);
};

ProductProvider.prototype.save = function(products, callback) {
	var product = null;

	if (typeof(products.length) == "undefined")
		products = [products];

	for (var i = 0; i < products.length; i++) {
		product = products[i];
		product.product_id = productCounter++;
		product.created_at = new Date();

		if (product.comments === undefined)
			product.comments = [];

		for (var j = 0; j < product.comments.length; j++) {
			product.comments[j].created_at = new Date();
		}
		this.dummyData[this.dummyData.length] = product;
	}
	callback(null, products);
};

new ProductProvider().save([
		{ name: 'abc widget', description: 'orange thing' },
		{ name: 'def widget', description: 'green thing' },
		{ name: 'ghi widget', description: 'purple thing' },
	], function(error, products){});

exports.ProductProvider = ProductProvider;

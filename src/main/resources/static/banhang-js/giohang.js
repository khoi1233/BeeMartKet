
app.controller('giohang-controller', function($scope, $http, $window) {
	$scope.cartItems = [];

	$http.get('/rest/{maGioHang}/chitietgiohang').then(function(response) {
		$scope.cartItems = response.data;
	});

	$scope.removeFromCart = function(itemId) {
		$http.delete('/api/cart/' + itemId).then(function() {
			// Logic to remove the item from the cartItems array
		});
	};
	$scope.addToCart = function(masp) {
		alert(masp)
		let item = this.item.find(item => item.id == id)
	}


});
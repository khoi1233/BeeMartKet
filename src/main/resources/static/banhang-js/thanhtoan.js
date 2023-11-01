app.controller('giohang-controller', function($scope, $http, $window) {
	$scope.cartItems = [];
	
	$http.get('/rest/3/chitietgiohang').then(function(response) {
		$scope.cartItems = response.data;
	});

	
});
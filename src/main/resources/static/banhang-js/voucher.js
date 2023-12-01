
app.controller('voucher-controller', function($scope, $http) {
	$scope.vouchers = [];
	$http.get('/rest/voucher')
		.then(function(response) {
			$scope.vouchers = response.data;
		})
		.catch(function(error) {
			console.error('Lỗi khi lấy mã giỏ hàng:', error);
		});

});
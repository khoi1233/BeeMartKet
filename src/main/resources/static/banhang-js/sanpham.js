var app = angular.module('my-app', []);

app.controller('sanpham-controller', function($scope, $http, $window) {
	$scope.sanpham = [];

	$http.get('/rest/sanpham')
		.then(function(response) {
			$scope.sanpham = response.data;
		});

	 $scope.goToSinglePage = function(maSanPham) {		
		window.location.href = `/user/product_detail/${maSanPham}`;
		alert("123");
	};


});
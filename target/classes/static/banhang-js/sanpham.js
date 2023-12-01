var app = angular.module('my-app', []);
app.filter('unsafeHtml', ['$sce', function($sce) {
	return function(val) {
		return $sce.trustAsHtml(val);
	};
}]);
app.controller('sanpham-controller', function($scope, $http, $window, $sce) {
	$scope.sanpham = [];
	$scope.sanpham = {};

	$http.get('/rest/sanpham')
		.then(function(response) {
			$scope.sanpham = response.data;
	$scope.sanphamNoiBat = response.data.filter(item => item.noiBat === true);
			
        

		});

	$scope.goToSinglePage = function(maSanPham) {
			window.location.href = `/user/product_detail/${maSanPham}`;
	};

	$scope.loadChiTietSanPham = function(maSanPham) {
		$http.get('/rest/' + maSanPham).then(function(response) {

			$scope.sanpham = response.data;
		})
			.catch(function(error) {
				console.error("hhhhh", error);
			})

	};
	window.onload = function() {
		var maSanPham = window.location.pathname.split('/').pop();
		console.log('maSanPham:', maSanPham);
		if (maSanPham) {
			$scope.loadChiTietSanPham(maSanPham);
		}
	}

});
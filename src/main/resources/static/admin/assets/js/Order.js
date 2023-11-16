var app = angular.module('my-admin', []);
app.controller('order-controller', function($scope, $http, $window, $sce) {
	$scope.sanpham = [];
	$scope.sanpham = {};
	$scope.sanphamThoiGian = [];
	$scope.phieuhang = [];
	$scope.loaiSanPhams = [];
	$scope.donvitinhs = [];
	$scope.hoadon = [];
	$scope.hoadonct = [];
	$scope.hinh = [];

	$http.get('/rest/sanpham')
		.then(function(response) {
			$scope.sanpham = response.data;
			$scope.sanphamNoiBat = response.data.filter(item => item.noiBat === true);
		});

	$http.get('/rest/loaisanpham')
		.then(function(response) {
			$scope.loaiSanPhams = response.data;
		});

	$http.get('/rest/donvitinh')
		.then(function(response) {
			$scope.donvitinhs = response.data;
		});

	$http.get('/rest/phieuhang')
		.then(function(response) {
			$scope.phieuhang = response.data;
			console.log(response.data)
		});


	$http.get('/rest/hoadon')
		.then(function(response) {
			$scope.hoadon = response.data;
		});

	$scope.goToSingOrder = function(maHoaDon) {
		window.location.href = `/admin/hoaDon/${maHoaDon}`;
	};
	$scope.loadOrder = function(maHoaDon) {
			$http.get('/rest/hoadon/' + maHoaDon)
			.then(function(response) {
				$scope.hoadonct = response.data;
			})
			.catch(function(error) {
				console.error('Lỗi Hóa Đơn:', error);
			});
	};

	window.onload = function() {
		var maHoaDon = window.location.pathname.split('/').pop();
		console.log('maHoaDon:', maHoaDon);
		if (maHoaDon) {
			$scope.loadOrder(maHoaDon);
		}
	}

});
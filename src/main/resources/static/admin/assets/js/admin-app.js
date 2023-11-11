var app = angular.module('my-admin', []);
app.controller('sanphamAdmin-controller', function($scope, $http, $window, $sce) {
	$scope.sanpham = [];
	$scope.sanpham = {};
	$scope.sanphamThoiGian = [];
	$scope.phieuhang = [];
	$scope.loaiSanPhams = [];
	$scope.donvitinhs = [];

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

	$scope.goToSinglePage = function(maSanPham) {
		window.location.href = `/admin/product_detail/${maSanPham}`;
	};
	
	$scope.goTodetailCoupon = function(maPhieuNhap) {
		window.location.href = `/admin/coupon_detail/${maPhieuNhap}`;
	};

	$scope.loadChiTietSanPham = function(maSanPham) {
		$http.get('/rest/' + maSanPham).then(function(response) {

			$scope.sanpham = response.data;
			console.log('maSanPham:', response.data);
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
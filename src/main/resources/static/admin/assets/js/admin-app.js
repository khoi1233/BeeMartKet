var app = angular.module('my-admin', []);
app.controller('sanpham-controller', function($scope, $http, $window, $sce) {
	$scope.sanpham = [];
	$scope.sanpham = {};
	$scope.sanphamThoiGian = [];
	

	$http.get('/rest/sanpham')
		.then(function(response) {
			$scope.sanpham = response.data;
			$scope.sanphamNoiBat = response.data.filter(item => item.noiBat === true);
			// Duyệt qua danh sách sản phẩm để tính toán thời gian tạo
        for (var i = 0; i < $scope.sanpham.length; i++) {
            var ngayTao = new Date($scope.sanpham[i].ngayTao); // Chuyển ngày tạo sang đối tượng Date
            var now = new Date(); // Lấy thời gian hiện tại
            var timeDiff = now - ngayTao; // Tính thời gian chênh lệch (đơn vị mili giây)

            // Chuyển thời gian chênh lệch sang phút
            var thoiGianTaoTheoPhut = Math.floor(timeDiff / (1000 * 60));                 
            // Kiểm tra nếu thời gian tạo dưới 2000 phút, thì thêm vào mảng $scope.sanphamThoiGian
            if (thoiGianTaoTheoPhut > 2000) {
                $scope.sanphamThoiGian.push($scope.sanpham[i]);
            }
        }
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
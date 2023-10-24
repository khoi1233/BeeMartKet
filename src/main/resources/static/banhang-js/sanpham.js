var app = angular.module('my-app', []);

app.controller('sanpham-controller', function($scope, $http, $window) {
	$scope.sanpham = [];

	$http.get('/rest/sanpham')
		.then(function(response) {
			$scope.sanpham = response.data;

		});

	 $scope.goToSinglePage = function(maSanPham) {
		
		window.location.href = `/khachhang/chitietsanpham/${maSanPham}`;
		alert("123");
	};

	// Quan ly gio hang
    $scope.cartItems = [];
    $scope.addToCart = function (masp) {
        var cart = this.cartItems.find(cart => cart.maSanPham == masp);
        if (cart) {
            cart.qty++;
            $scope.saveToLocalStorage();
        } else {
            var url = `/rest/product/${masp}`;
            $http.get(url).then(resp => {
                resp.data.qty = 1;
                $scope.cartItems.push(resp.data);
                $scope.cartItemCount = $scope.getcount(); // Cập nhật biến trong $scope
                $scope.saveToLocalStorage();
                // Sử dụng $timeout để đảm bảo cập nhật $scope sẽ xảy ra sau khi hoàn tất tác vụ bất đồng bộ
                $timeout(function () {
                    $scope.$apply();
                });
            })
        }
        swal("Thành công", "Thêm sản phẩm vào giỏ hàng thành công!", "success")
        // Bắt buộc AngularJS cập nhật giao diện
		
    }


});